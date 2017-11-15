package gov.nara.das.chunk;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import gov.nara.das.chunk.xml.MessageProcessor;
import gov.nara.das.chunk.xml.MessageProcessorImpl;
import gov.nara.das.chunk.xml.STAXParser;
import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.JobActionHistory;
import gov.nara.das.common.db.ingest.dao.JobActionTypesDAO;
import gov.nara.das.common.db.ingest.dao.impl.JobActionHistoryTemplate;
import gov.nara.das.common.db.ingest.dao.impl.JobTemplate;

/**
 * @formatter:off
 * @brief
 * The main class for this application.
 * This monitors the job queue. When a new message is found, a new processing thread is launched,
 * to process the message. Before the thread is launched the number of concurrent processes is checked 
 * against consurrentCount. If it is less than concurrentCount, the new thread is launched.
 * Otherwise, the main thread will wait, monitoring the number or processing threads until the
 * condition is met.
 * 
 * This class currently requires the two queue urls and the S3 bucket name to be env variables.
 * It would be simple to inject these instead. 
 * 
 * The processing threads use a STAX parser. First the thread copies the S3 file to temp then
 * calls the STAX parser.
 *
 * @author Matthew Mariano
 *
 * Change Log
 *
 * date       contributor             comments
 * 2017-08-08 Matthew Mariano       : created
 *
 * @formatter:on
 */
@EnableAutoConfiguration
@Component
@ComponentScan({"gov.nara.das.common.db","gov.nara.das.chunk","gov.nara.das.chunk.xml"})
public class Chunker {
	
	/**
	 * a random number generator for creating temp file names.
	 */
	Random random=new Random(System.currentTimeMillis());
	
	@Autowired
	private JobActionHistoryTemplate jobActionHistoryTemplate;
	
	@Autowired
	private JobTemplate jobTemplate;
	
	/**
	 * the number of concurrent requests.
	 * the process method will read the queue for new ingest requests and
	 * launch a new thread for each message. This parameter limits the number 
	 * of threads that will be launched. 
	 */
    @Value("${concurrent.count}")
    public static int concurrentCount;
    
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	 
    /**
     * according to the documentation, for queues with a small # of messages,
     * the receive messages may return 0 and the work-around is simply to retry.
     * so we introduce a small # of retries
     */
	
    public static final int RETRY_COUNT = Integer.MAX_VALUE;
    
    private AmazonSQS sqsClient;

    private String ingestQueueURL;


    private String[] args;


    private boolean logToConsole=false;
    private volatile Long threadIndex=0L;

    private Map<Future, MyCallable> futureCallableMap = new TreeMap<Future, MyCallable>();
    private ArrayList<MyFuture> futures = new ArrayList<MyFuture>();
    //
    private AmazonS3 s3Client;
    private String bucketName;
    private long totalProcessed;
    private String descriptionQueuURL;

    //
    public class XMLFileFilter implements FilenameFilter {


        public boolean accept(File dir, String name) {
            return name.endsWith("xml");
        }

    }


    public class MyFuture implements Future, Comparable {

        private final Future future;

        private final MyCallable myCallable;

        private final String id;

        public String getTag() {
            return id;
        }

        /**
         * @brief custom Future class wraps Future
         * @param afuture
         *            - the Future this wraps
         * @param acallable
         *            - the MyCallable of the Future
         * @param anId
         *            -
         */
        public MyFuture(Future afuture, MyCallable acallable, String anId) {
            future = afuture;
            myCallable = acallable;
            id = anId;
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {

            return future.cancel(mayInterruptIfRunning);
        }

        @Override
        public Object get() throws InterruptedException, ExecutionException {

            return future.get();
        }

        @Override
        public Object get(long timeout, TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {

            return future.get(timeout, unit);
        }

        @Override
        public boolean isCancelled() {

            return future.isCancelled();
        }

        @Override
        public boolean isDone() {

            return future.isDone();
        }

        public Future getFuture() {
            return future;
        }

        public MyCallable getMyCallable() {
            return myCallable;
        }

        @Override
        public int compareTo(Object arg0) {
            return Integer.compare(hashCode(), arg0.hashCode());
        }

    }
    public final class MyCallable implements Callable<Long> {
        private boolean failed;
        private Long startTime = 0L;
        private Long endTime = 0L;
        private Exception exception;
        private Message message;
        private StringBuffer buffer = new StringBuffer();
        private Long threadId;
        private Long friendlyThreadId;
        private String objectKey;
        private String bucket;
        private final String sqsMessageId;

        /**
         * @return the buffer
         */
        public StringBuffer getBuffer() {
            return buffer;
        }

        public MyCallable(Message amessage) {
            message = amessage;
            sqsMessageId=message.getMessageId();
            friendlyThreadId = getNextIndexForThread();
            threadId = Thread.currentThread().getId();
        }

        @Override
        public Long call() throws Exception {
            mylog("inside call()");
            startTime = System.currentTimeMillis();
            processMessage();
            endTime = System.currentTimeMillis();
            return endTime;
        }

        private void processMessage() {
        	Job job=new Job();
        	JobActionHistory jh=new JobActionHistory();
        	FileOutputStream fout=null; // for tmp file
            try {
                String messageBody = message.getBody();
                log(messageBody);
                JSONObject j=new JSONObject(messageBody);
                long jobId=j.getLong("jobId");
                log("jobId="+jobId);
                String s3Url=j.getString("url");
                //https://s3.amazonaws.com/
                Pattern p= Pattern.compile("https://s3.amazonaws.com/(.*?)/(.*)");
                Matcher m=p.matcher(s3Url);
                if(m.find()){
                    bucketName=m.group(1);
                    objectKey=m.group(2);
                    log("bucketName="+bucketName);
                    log("objectKey="+objectKey);

                }else{
                    throw new RuntimeException("bad url for s3 object:"+s3Url);
                }
                ObjectMetadata omd = null;
                try{
                    omd = s3Client.getObjectMetadata(bucketName, objectKey);
                } catch(Exception e) {
                    log("Exception getting metadata for "+objectKey+": "+e.getMessage());
                }
                log.debug("bucketName="+bucketName+", objectKey="+objectKey);
                S3Object s3Object = s3Client.getObject(bucketName, objectKey);
                String xml=getXmlFromS3(s3Object);
                String fname=getTempFileName();
                fout=new FileOutputStream(fname);
                fout.write(xml.getBytes());
                fout.close();
                log("xml size="+xml.length());
                job.setJobId(jobId);
                File tmpFile=new File(fname);
                jobTemplate.updateCurrentAction(job,JobActionTypesDAO.ActionTypes.CHUNKING_STARTED.id);
                jobActionHistoryTemplate.insert(job, JobActionTypesDAO.ActionTypes.CHUNKING_STARTED.id);
                MessageProcessor mp=new MessageProcessorImpl(jobId, descriptionQueuURL, sqsClient);
                STAXParser parser =new STAXParser(tmpFile, mp,jobId);
                parser.processXML();
                tmpFile.delete();
                job.setTotalDescriptionCount(parser.getTotal());
                jobTemplate.updateTotalDescriptionCount(job);
                jobActionHistoryTemplate.insert(job, JobActionTypesDAO.ActionTypes.CHUNKING_COMPLETED.id);
            } catch (Exception e) {
                failed = true;
                exception = e;
                mylog("processing failed: " + e.getMessage(), e.getMessage());

            } finally {
                endTime = System.currentTimeMillis();
                mylog("___________________________________________");
                if(fout !=null){
                	try {
						fout.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
                }
            }
        }

        /**
         * for each object, print out id data for this callable followed by the
         * string version of the object
         *
         * @param a
         *            - and array or possibly one object
         */
        private void mylog(Object... a) {
            for (Object o : a) {
                buffer.append(
                        "\nMyCallable: friendlyThreadId=" + friendlyThreadId + ", threadId=" + threadId + ": " + o);
                log("\nMyCallable: friendlyThreadId=" + friendlyThreadId + ", threadId=" + threadId + ": " + o);
            }
        }

        /**
         *
         * @return - true if this callable failed for any reason
         */
        public boolean getFailed() {
            return failed;
        }

        /**
         *
         * @return - the exception if one exists and caused a failure or return
         *         null
         */
        public Exception getException() {
            return exception;
        }

        /**
         *
         * @return - the initial message that was processed by this callable
         */
        public Message getMessage() {
            return message;
        }
    }

    public static void usage() {
        System.out.println("Uage: java Chunker <files>"
                + "\nwhere <files> are 1 or more xml files or directories with xml files to process."
                + "\nif a directory is specified all files in that directory, with an xml extension, will be processed.");
    }

	public static void main(String[] args) {
		try {
			
			ConfigurableApplicationContext ctx = SpringApplication.run(Chunker.class, args);
			Chunker chunker = ctx.getBean(Chunker.class);
			chunker.init();
			chunker.process(args);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

    /**
     *  for Spring
     */
    public Chunker(){
    	
    }
    public Chunker(String[] aargs) {
        args = aargs;
        init();
    }
private void init(){
    ingestQueueURL = System.getenv("INGEST_QUEUE_URL");
    descriptionQueuURL=System.getenv("CHUNK_QUEUE_URL");
    log("ingestQueueURL="+ingestQueueURL);
    initQ();
    initS3();
}
    private void initS3() {
        ClientConfiguration config = new ClientConfiguration();
        config.setMaxConnections(100);
        s3Client = new AmazonS3Client(config);
        bucketName=System.getenv("INGEST_BUCKET");
        log("bucket="+bucketName);
    }
    private void initQ() {
        sqsClient = AmazonSQSClientBuilder.defaultClient();
        List<String> attributeNames = new ArrayList<String>();
    }
    public void process(String[] aargs) throws IOException {
    	args=aargs;
    	init();
    	process();
    }
    public void process() throws IOException {
    	log.debug("concurrentCount="+concurrentCount);
    	if(concurrentCount <=0){
    		log.debug("concurrentCount NOT found in properties. setting it to 5000");
    		concurrentCount=5000;
    	}else{
    		log.debug("found concurrentCount="+concurrentCount);
    	}
        ExecutorService executorService = Executors.newFixedThreadPool(concurrentCount);
        try {
            long processCount=0;
            int bad = 0;
            while (true) {
                ReceiveMessageResult rmr = null;
                try {
                    log("reading queue");
                    rmr = sqsClient.receiveMessage(ingestQueueURL);
                } catch(AmazonClientException e) {
                    log("Error reading from SQS at "+ ingestQueueURL);
                    e.printStackTrace();
                }
                if (rmr != null) {
                    List<Message> messages = rmr.getMessages();

                    if (messages.size() == 0) {
                        log("No messages found on queue. Sleeping for 5 seconds");
                        Thread.currentThread().sleep(5000);
                        if (bad++ >= RETRY_COUNT) {
                            break;
                        }
                        continue;
                    }
                    bad = 0;

                    for (Message message : messages) {
                        while (true) {
                            if (futures.size() < concurrentCount) {
                                MyCallable callable = new MyCallable(message);
                                Future future = executorService.submit(callable);
                                try {
                                    sqsClient.deleteMessage(new DeleteMessageRequest(ingestQueueURL, message.getReceiptHandle()));
                                } catch(AmazonClientException e) {
                                    e.printStackTrace();
                                    // try one more time...
                                    try {
                                        sqsClient.deleteMessage(new DeleteMessageRequest(ingestQueueURL, message.getReceiptHandle()));
                                    } catch(AmazonClientException f) {
                                        // print stack trace and continue without deleting...
                                        // downside is we will re-process this item again during a future poll.
                                        f.printStackTrace();
                                    }
                                }
                                if(processCount == Long.MAX_VALUE){
                                    processCount=0;
                                }
                                String tag = "process index=" + processCount++;
                                MyFuture future2 = new MyFuture(future, callable, tag);
                                futures.add(future2);
                                futureCallableMap.put(future2, callable);
                                break;
                            } else {
                                manageFutures(futures, concurrentCount - 1);
                            }
                        }
                    }
                }

            }
            // wait up to 3 minutes for any futures to finish.
            //
            manageFutures(futures, 0, 3 * 60 * 1000);
            executorService.shutdown();
            log("Proccessing completed");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void log(Object... objects){
        for(Object o:objects){
            System.err.println(o);
        }
    }

    /**
     *
     * @param futures
     *            the collection of Future objects to maintain
     * @param max
     *            the maximum size/capacity of the collection; if we are at
     *            capacity then sleep and try to remove some completed Future
     *            objects from the collection
     *
     * @return
     * @throws Exception
     */
    public boolean manageFutures(Collection<MyFuture> futures, int max) throws Exception {
        manageFutures(futures, max, Long.MAX_VALUE);
        return true;
    }

    /**
     *
     * @param futures
     *            - the collection of Future objects to manage
     * @param max
     *            - a threshold. If the collection size is over the threshold
     *            then perform a loop where we try to remove completed futures.
     * @param maxWaitTime
     *            - the maximum time in milliseconds this method should run.
     * @throws Exception
     */
    public boolean manageFutures(Collection<MyFuture> futures, int max, long maxWaitTime) throws Exception {
        long totalResult = 0l;
        long sleepTime = 20;
        log("manageFutures: concurrent process count: " + futures.size());
        while (futures.size() > max) {
            Thread.sleep(sleepTime);
            // use iterator
            Iterator<MyFuture> it = futures.iterator();
            while (it.hasNext()) {
                MyFuture future = it.next();
                if (future.isDone()) {
                    it.remove();
                    // futures.remove(future);
                    MyCallable c = futureCallableMap.get(future);
                    String message = c.getMessage().getBody();
                    if (message == null) {
                        message = "";
                    }
                    if (message.length() > 30) {
                        message = message.substring(0, 30);
                    }
                    log("manageFutures: message index=" + future.getTag() + ", friendlyThreadId=" + c.friendlyThreadId
                            + ",  objectKey=" + c.objectKey);
                    log("manageFutures: message index=" + future.getTag() + ", friendlyThreadId=" + c.friendlyThreadId
                            + ",  bucket=" + c.bucket);
                    log("manageFutures: message index=" + future.getTag() + ", friendlyThreadId=" + c.friendlyThreadId
                            + ", some text=" + message + ", start / end = " + date2String(c.startTime) + "/"
                            + date2String(c.endTime));
                    long seconds = (c.endTime - c.startTime) / 1000;
                    log("manageFutures: message index=" + future.getTag() + ", friendlyThreadId=" + c.friendlyThreadId
                            + ", total process time seconds=" + seconds);
                    log("buffer=" + c.getBuffer().toString());
                    if (c.failed) {
                        log("process failed: ", c.exception.getMessage(), c.exception.getMessage());
                    }
                }
            }
            if (sleepTime > maxWaitTime) {
                return false;
            }
        }
        return true;
    }

    public static String date2String(long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd~HH:mm:ss");
        return formatter.format(date);
    }

    public boolean isLogToConsole() {
        return logToConsole;
    }

    public void setLogToConsole(boolean logToConsole) {
        this.logToConsole = logToConsole;
    }

    public synchronized Long getNextIndexForThread() {
        return threadIndex++;
    }

    public String getXmlFromS3(S3Object o)throws IOException{
        InputStream is=o.getObjectContent();
        ByteArrayOutputStream bout=new ByteArrayOutputStream();
        int i=0;
        while(( i=is.read()) > -1){
            bout.write((byte) i);
        }
        return bout.toString();
    }


    private synchronized String getTempFileName(){
    	return ""+System.currentTimeMillis()+"-"+random.nextInt(Integer.MAX_VALUE);
    }

    @Value("${concurrent.count}")
    public void setConCurrentCount(int c) {
        concurrentCount = c;
    }


}