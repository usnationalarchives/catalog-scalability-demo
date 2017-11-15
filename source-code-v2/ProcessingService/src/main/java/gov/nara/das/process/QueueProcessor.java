package gov.nara.das.process;

import static gov.nara.das.common.utils.Utils.date2String;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

import gov.nara.das.common.db.ingest.MessageStatus;
import gov.nara.das.common.db.ingest.dao.impl.MessageStatusTemplate;
import gov.nara.das.common.response.CreateDescriptionResponse;


/**
 * @formatter:off
 * @brief
 * The main worker thread for this app. This monitors the single description queue and
 * launches new processing threads to process messages as needed
 * 		
 *
 *
 * @author Matthew Mariano
 *
 *
 * Change Log
 *
 * date       contributor                 comments 
 * 2017-10-24 Matthew Mariano           : created
 *
 * @formatter:on
 */

public class QueueProcessor implements Runnable{
	
	private boolean logResponses;

	private boolean forwardBatches;
	
	private boolean verbose;
	
	private boolean active=true;
	private volatile boolean finished=false;
	private final int id;
	
	//@Autowired
	private MessageStatusTemplate messageStatusTemplate;
	
	//@Resource(name="HTTPService")
	private HTTPService httpService;
	

	
	
	private final MessageListener listener;
	public final  int concurrentCount;
	
	public static final int RETRY_COUNT = 5;
	private String[] args;
	
	private long startTime;
	private long endTime;
	private int lastMessageCount = 0;

	private volatile long totalProcessed;
	
	private final String queueUrl;
	//
	
	private boolean logToConsole = false;
	private volatile Long threadIndex = 0L;

	private AmazonSQS sqsClient;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private Map<Future, MyCallable> futureCallableMap = new TreeMap<Future, MyCallable>();
	private ArrayList<MyFuture> futures = new ArrayList<MyFuture>();
	private volatile Map<String,Long> messageMap=new TreeMap<String,Long>();
	//
	long storeTimeCount=0;
	long storeTimeIndex=0;
	long[] storeTimeArray=new long[100];
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
		private String message;
		private StringBuffer buffer = new StringBuffer();
		private Long threadId;
		private Long friendlyThreadId;
		private int jobId = -1;
		private String sqsMessageId = "";
		private final String xml0;
		List<Integer> indices=new ArrayList<Integer>();
		List<Integer> lengths=new ArrayList<Integer>();
		private final String indicesString;
		private final String lengthsString;
		/**
		 * @return the buffer
		 */
		public StringBuffer getBuffer() {
			return buffer;
		}

		public MyCallable(String aMessage, String messageId,String aIndicesString, String aLengthsString) {
			message = aMessage;
			sqsMessageId=messageId;
			indicesString=aIndicesString;
			lengthsString=aLengthsString;
			List<Integer> aIndices=unflattenIntegerString(indicesString);
			List<Integer> aLengths=unflattenIntegerString(lengthsString);
			indices=aIndices;
			lengths=aLengths;

			xml0=new String(Base64.getDecoder().decode(message.getBytes()));
			System.err.println("xml0="+xml0);
			friendlyThreadId = getNextIndexForThread();
			threadId = Thread.currentThread().getId();
		}

		@Override
		public Long call() throws Exception {
			mylog("inside call()");
			startTime = System.currentTimeMillis();
			processMessage();
			endTime = System.currentTimeMillis();
			long delta=endTime-startTime;
			storeTime(delta);
			return endTime;
		}

		private void processMessage() {
			boolean servicePassed=false;
			int statusCode=0;
			boolean insertFailed=false;
			boolean insertSucceeded=false;
			MessageStatus ms=new MessageStatus();
			String chunkingId="";
			long naid=-1;
			long messageIndex=-1;
			
            try {
            	System.err.println("forwardBatches="+forwardBatches);  
				if (forwardBatches) {
					System.err.println("posting newBatch");
					log.error("posting newBatch");
					String newMessage = "<meta><indices>" + indicesString + "</indices>" + "<lengths>" + lengthsString
							+ "</lengths>" + "</meta>" + message;
					ResponseEntity<CreateDescriptionResponse> r1 = httpService.postNewBatch(newMessage);
					System.err.println("response newBatch=" + r1);
					log.error("response newBatch=" + r1);
					return;
				}else{
					System.err.println("no batch processing.");
					log.error("no batch processing.");
				}
            	int i=0;
            	System.err.println("indices.size"+indices.size());
            	for(int index:indices){
	            	String xml=xml0.substring(index,index+lengths.get(i));
	            	System.err.println("loop i="+i);
	            	System.err.println("index="+index);
	            	i++;
					xml=replaceTaggedString(xml,"sqsMessageId",sqsMessageId);
					chunkingId=findTaggedString(xml, "chunkingId");
					messageIndex=findTaggedInt(xml, "messageIndex");
					log("sending message to api. sqsMessageId="+sqsMessageId+", first 150 chars of message= "
					+(xml+"                                "
						 + "                               "
						 + "                               "
						 + "                               "
						 + "                               ").substring(0,150));
					ms.setHttpPostRequestTime(new Timestamp(System.currentTimeMillis()));
					log.error("sending http request to api");
					ResponseEntity<String> r=httpService.postNew(xml);
					statusCode=r.getStatusCodeValue();
					String json=r.getBody();
					try{
						JSONObject j=new JSONObject(json);
						naid=j.getLong("naId");
					}catch(Exception e){
						
					}
					//
					if(logResponses){
						log.error("response= \n"+json);
						System.err.println("2.response= \n"+json);
					}
					log("response received from api. naid=" +naid);
					System.err.println("response received from api. naid=" +naid);
					log.error("response received from api. naid=" +naid);
					System.err.println("response received. status code=" +statusCode);
					//
					log("statusCode="+statusCode);
					log.error("2statusCode="+statusCode);
					log.error("response received: "+System.currentTimeMillis() +" "+date2String(System.currentTimeMillis() ));
					if(statusCode == HttpStatus.CREATED.value()){
						insertSucceeded=true;
					}else{
						insertFailed=true;
					}
					servicePassed=true;
					//Pattern p= Pattern.compile("https://s3.amazonaws.com/(.*?)/(.*)");
					jobId = findTaggedInt(xml, "jobId");
					//		findTaggedString(xml, "sqsMessageId");
					log("thread completed");
            	}
			} catch (Exception e) {
				e.printStackTrace(System.err);
				log(stackTraceToString(e));
				failed = true;
				exception = e;
				log("ERROR: "+ e);
				mylog("processing failed: " + e.getMessage(), stackTraceToString(e));

			} finally {
				try{
// 2017-10-24 for now we don't upsert. this is handled in api
//					log("saving message status to db");
//					ms.setSqsMessageId(sqsMessageId);
//					ms.setChunkingId(chunkingId);
//					ms.setJobId(jobId);
//					ms.setNaid(naid);
//					ms.setMessageIndex(messageIndex);
//					ms.setHttpPostResponseTime(new Timestamp(System.currentTimeMillis()));
//					ms.setInsertFailed(insertFailed);
//					ms.setInsertSuccess(insertSucceeded);
//					messageStatusTemplate.upsert(ms);
//					log("SAVED message status to db");
				}catch(Exception e){
					e.printStackTrace(System.err);
					log.debug("insert to message_status failed: "+ e.getMessage());
					log(stackTraceToString(e));
				}
				endTime = System.currentTimeMillis();
				mylog(repeatString("_", 80,""));
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
				// log("\nMyCallable: friendlyThreadId=" + friendlyThreadId + ",
				// threadId=" + threadId + ": " + o);
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
		public String getMessage() {
			return xml0;
		}
	}
	public QueueProcessor( HTTPService http, MessageStatusTemplate temp,
			MessageListener m,int anId,String url, int concurrent
			, boolean aVerbose, boolean aForwardBatches,boolean aLogResponses){
		queueUrl=url;
		id=anId;
		if(concurrent<=0){
			concurrent=5;
		}
		concurrentCount=concurrent;
		listener=m;
		httpService=http;
		messageStatusTemplate=temp;
		verbose=aVerbose;
		forwardBatches=aForwardBatches;
		logResponses=aLogResponses;
	}
	private void initQ() {
		sqsClient = AmazonSQSClientBuilder.defaultClient();
		List<String> attributeNames = new ArrayList<String>();
		attributeNames.add("All");
		GetQueueAttributesRequest g = new GetQueueAttributesRequest(queueUrl, attributeNames);
		GetQueueAttributesResult queueAttributes = sqsClient.getQueueAttributes(g);
		Map<String, String> map = queueAttributes.getAttributes();

	}

	private void init(){
		initQ();
	}
	public void run()  {
		//args = aargs;
		init();
		try {
			process();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new RuntimeException(e);
		}
	}

	public void process() throws Exception  {
		startTime = System.currentTimeMillis();

		log.debug("concurrentCount=" + concurrentCount);
		if (concurrentCount <= 0) {
			log.error("ERROR concurrentCount="+concurrentCount);
			System.exit(1);
		} else {
			log.error("found concurrentCount=" + concurrentCount);
		}
		ExecutorService executorService = Executors.newFixedThreadPool(concurrentCount);
		try {
			long processCount = 0;
			int bad = 0;
			int logCount=0;
			List<Message> messages=new ArrayList<Message>();
			while (true) {
				ReceiveMessageResult rmr = null;
				try {
					messages=new ArrayList<Message>();
					log("reading queue");
					// attributes indices and lengths are used for batching.
					// they represent the location within the message of the current description xml
					// and the length of that description xml respectivelly.
					// notice the  .withAttributeNames("" ). This seemed necessary as of this writing
					ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl)
							.withMessageAttributeNames("indices")
							.withMessageAttributeNames("lengths")
				              .withAttributeNames("" ); 
					receiveMessageRequest.setMaxNumberOfMessages(10);
					rmr = sqsClient.receiveMessage(receiveMessageRequest);
					List<Message> m=rmr.getMessages();

					messages.addAll(m);
				} catch (AmazonClientException e) {
					log.error("Error reading from SQS at " + queueUrl);
					e.printStackTrace();
				}
				if (rmr != null) {

					Thread.currentThread().sleep(10);
					//List<Message> messages = rmr.getMessages();

					if (messages.size() == 0) {
						lastMessageCount = 0;
						// log.error("queueUrl="+queueUrl);
						log("No messages found on queue. Sleeping short time.");
						Thread.currentThread().sleep(2000);
						if(lastMessageCount==0 && logCount++ >=10){
							logCount=0;
							log.error("done sleeping");
						}
						if (bad++ >= RETRY_COUNT) {
							Thread.currentThread().sleep(2000);
						}
						continue;
					} else {
						logCount=0;
						log("found messages=" + messages.size());
					}
					bad = 0;
					lastMessageCount = messages.size();
					for (Message message : messages) {
				
						while (true) {
							if (futures.size() < concurrentCount) {
								MyCallable callable=null;
								Future future =null;
									callable = new MyCallable(message.getBody(), message.getMessageId(),
											getStringFromMessageAttributes(message, "indices")
											, getStringFromMessageAttributes(message, "lengths"));
									deleteMessage(message);
									future = executorService.submit(callable);
									System.err.println("creating future for sqsMessageId="+message.getMessageId());
									if (processCount == Long.MAX_VALUE) {
										processCount = 0;
									String tag = "process index=" + processCount++;
									MyFuture future2 = new MyFuture(future, callable, tag);
									futures.add(future2);
									futureCallableMap.put(future2, callable);
								}else{
									log.error("Message was not deleted. So it won't be processed");
								}
								break;
							} else {
								manageFutures(futures, concurrentCount - 1);
							}
						}
					}
				}

			}
			// wait up to 1 second for any futures to finish.
			//
//			manageFutures(futures, 0, 1000);
//			executorService.shutdown();
//			endTime = System.currentTimeMillis();
//			log.debug("Proccessing completed");
//			long delta = endTime - startTime;
//			double sec = delta / 1000d;
//			log("Start=" + getDate(startTime));
//			log("Start=" + getDate(startTime));
//			log("total messages=" + totalProcessed);
//			log("total time=" + sec + " (s)");
//			if (sec > 0) {
//				double ms = totalProcessed / sec;
//				log("messages/sec=" + ms);
//			}
//			finished=true;
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace(System.err);
			System.err.println("FATAL: throwing exception: "+e);
			active=false;
			throw(e);
		}
	}

	private void log(Object... objects) {
		for (Object o : objects) {
			String s="Worker "+id;
			s=s+" "+o;
			//System.err.println(s);
			log.debug("" + s);
		}
	}

	/**
	 *
	 * @param toRepeat
	 * @param count
	 *            - count > 1
	 * @param delim
	 * @return
	 */
	public static String repeatString(String toRepeat, int count, String delim) {
		StringBuffer b = new StringBuffer();
		b.append(toRepeat);
		for (int i = 1; i < count; i++) {
			if (delim != null) {
				b.append(delim);
			}
			b.append(toRepeat);
		}
		return b.toString();
	}

	public static String stackTraceToString(Exception e) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bo);
		e.printStackTrace(ps);
		ps.close();
		return bo.toString();
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
					String message = c.getMessage();//getBody();
					if (message == null) {
						message = "";
					}
					if (message.length() > 30) {
						message = message.substring(0, 30);
					}
					long seconds = (c.endTime - c.startTime) / 1000;
					if(verbose){
					log("manageFutures: message index=" + future.getTag() + ", friendlyThreadId="
							+ c.friendlyThreadId + ", total process time seconds=" + seconds);
					}
					if (c.failed) {
						log("process failed: ", c.exception.getMessage(), stackTraceToString(c.exception));
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

	public String findTaggedString(String xml, String tag) {
		String value = null;
		Pattern p = Pattern.compile("<" + tag + ">(.*?)<\\/" + tag + ">");
		java.util.regex.Matcher m = p.matcher(xml);
		if (m.find()) {
			value = m.group(1);
		}
		return value;
	}
	private  String replaceTaggedString(String xml, String tag, String replaceData) {
		xml=xml.replaceFirst("<" + tag + ">(.*?)<\\/" + tag + ">", "<"+tag+">"+replaceData+"</"+tag+">");
		return xml;
	}
	public int findTaggedInt(String xml, String tag) {
		int value = -1;
		Pattern p = Pattern.compile("<" + tag + ">(\\d+)<\\/" + tag + ">");
		java.util.regex.Matcher m = p.matcher(xml);
		if (m.find()) {
			value = Integer.parseInt(m.group(1));
		}
		return value;
	}

	public static String getDate(long milli) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd~HH:mm:ss");
		Date date = new Date(milli);
		String s = formatter.format(date);
		return s;
	}
	public long getTotalProcessed(){
		return totalProcessed;
	}
	/**
	 * @return the finished
	 */
	public boolean isFinished() {
		return finished;
	}

	private synchronized void storeTime(long time){
		long avg=0;
		try{
			int index=(int) (storeTimeCount % storeTimeArray.length);
			storeTimeArray[index]=time;
			long total=0;
			for(long s:storeTimeArray){
				total+=s;
			}
			avg=total/storeTimeArray.length;
			if(storeTimeCount++ == Long.MAX_VALUE){
				storeTimeCount=0;
			}
		
		}catch(Exception e){
			// do nothing
		}
		log.error("average thread time="+avg+"(ms)");
		System.err.println("average thread time="+avg+"(ms)");
	}
	public static List<Integer> unflattenIntegerString(String s){
		List<Integer> indices=new ArrayList<Integer>();
		String[] a=s.split(",");
		for(String is: a){
			int index=Integer.parseInt(is);
			indices.add(index);
		}
		return indices;
	}
public synchronized boolean  deleteMessage(Message message){
	boolean deleted=false;
	try {
		totalProcessed++;
		System.err.println("processMessage(); deleting message");
		
		sqsClient.deleteMessage(
				new DeleteMessageRequest(queueUrl, message.getReceiptHandle()));
		deleted=true;
		System.err.println("processMessage(); message DELETED");
	} catch (AmazonClientException e) {
		log.error("processMessage(): failed to delete message " + message.getMessageId());;
		e.printStackTrace(System.err);
		// try one more time...
		try {
			sqsClient.deleteMessage(
					new DeleteMessageRequest(queueUrl, message.getReceiptHandle()));
			deleted=true;
		} catch (AmazonClientException f) {
			log.error("failed to delete message " + message.getMessageId());;
			// print stack trace and continue
			// without deleting...
			// downside is we will re-process this
			// item again during a future poll.
			f.printStackTrace();
			log.error(e.getMessage());
		}
	}
	return deleted;
}
private String getStringFromMessageAttributes(Message m, String attributeName){
	String s="";
	MessageAttributeValue mv=m.getMessageAttributes().get(attributeName);
	if(mv !=null){
		s=mv.getStringValue();
	}else{
		System.err.println("DID NOT FIND integer list in attributes for key="+attributeName);
	}
	return s;
}
private List<Integer> getIntegerArrayListFromMessageAttributes(Message m, String attributeName){
	List<Integer>  list=new ArrayList<Integer>();
	String s=getStringFromMessageAttributes(m, attributeName);
	list=unflattenIntegerString(s);
	return list;
}
/**
 * @return the active
 */
public boolean isActive() {
	return active;
}
public final static String flatten(List<String> list, String delim) {
	String f = "";
	if (list.size() == 0) {
		return f;
	}
	f = f + list.get(0);
	for (int i = 1; i < list.size(); i++) {
		f = f + delim;
		f = f + list.get(i);
	}
	return f;
}
}
