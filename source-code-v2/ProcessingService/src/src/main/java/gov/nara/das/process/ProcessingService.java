package gov.nara.das.process;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;

import gov.nara.das.common.db.ingest.dao.impl.MessageStatusTemplate;

/**
 * @formatter:off
 * @brief
 *
 * 		Usage:
 *
 *
 * @author Matthew Mariano
 *
 *
 * Change Log
 *
 * date       contributor                 comments 
 * 2017-08-08 Matthew Mariano           : created
 *
 * @formatter:on
 */
@EnableAutoConfiguration
@Component
@ComponentScan({ "gov.nara.das.common.db", "gov.nara.das.process" })
/**
 * @ComponentScan("gov.nara.das.common.db")
 * 
 * @author matthewmariano
 *
 */
public class ProcessingService implements MessageListener {
	
	@Value("${log.responses}")
	private boolean logResponses;
	
	@Value("${forward.batches}")
	private boolean forwardBatches;
	
	@Value("${queue.processor.verbose}")
	private boolean verbose;
	
	@Resource(name = "HTTPService")
	private HTTPService httpService;

	@Autowired
	private MessageStatusTemplate messageStatusTemplate;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	public static final int RETRY_COUNT = Integer.MAX_VALUE;

	@Value("${workers.concurrent.count}")
	private int workersConcurrentCount;

	private AmazonSQS sqsClient;
	
	private String[] args;
	
	/**
	 * environment variables
	 */
	private String queueUrl;
	//

	private boolean logToConsole = false;
	private volatile Long threadIndex = 0L;

	public static void usage() {
		System.out.println("Uage: java ProcessingService <files>"
				+ "\nwhere <files> are 1 or more xml files or directories with xml files to process."
				+ "\nif a directory is specified all files in that directory, with an xml extension, will be processed.");
	}

	public static void main(String[] args) {
		try {

			ConfigurableApplicationContext ctx = SpringApplication.run(ProcessingService.class, args);

			ProcessingService chunker = ctx.getBean(ProcessingService.class);

			chunker.init();
			
			chunker.process(args);
			
		} catch (Throwable t) {
			t.printStackTrace(System.err);
		}
	}

	/**
	 * for Spring
	 */
	public ProcessingService() {

	}

	public ProcessingService(String[] aargs) {
		args = aargs;
		init();
	}

	private void init() {
		initQ();
	}

	private void initQ() {
		queueUrl = System.getenv("CHUNK_QUEUE_URL");
		sqsClient = AmazonSQSClientBuilder.defaultClient();
		List<String> attributeNames = new ArrayList<String>();
		attributeNames.add("All");
		GetQueueAttributesRequest g = new GetQueueAttributesRequest(queueUrl, attributeNames);
		GetQueueAttributesResult queueAttributes = sqsClient.getQueueAttributes(g);
		Map<String, String> map = queueAttributes.getAttributes();
		long visibilityTimeout = -1;
		if (map.get("VisibilityTimeout") != null) {
			visibilityTimeout = Long.parseLong(map.get("VisibilityTimeout"));
		}

	}

	public void process(String[] aargs) throws IOException {
		args = aargs;
		init();
		process();
	}

	public void process() throws IOException {

		int workers = 1; // always use 1 when sharing the sqsClient
		System.err.println("workersCOncurrent=" + workersConcurrentCount);
		ExecutorService executorService = Executors.newFixedThreadPool(workers);
		queueUrl = queueUrl = System.getenv("CHUNK_QUEUE_URL");
		List<QueueProcessor> list = new ArrayList<QueueProcessor>();
		QueueProcessor p =null;
		for (int i = 0; i < workers; i++) {
			p= new QueueProcessor(httpService, messageStatusTemplate, this, i, queueUrl,
					workersConcurrentCount,verbose,forwardBatches,logResponses);
			list.add(p);
			executorService.execute(p);
		}
		executorService.shutdown();
		while (!executorService.isTerminated()) {
			try {
				log("sleeping 1");
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
			int totalProcessed = 0;

			while (true) {
				boolean finished = true;
				if(!p.isActive()){
					System.err.println("FATAL ERROR: queue processor is not active. exiting");
					System.exit(1);;
				}
				log("sleeping 2");
				try {
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
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

	@Override
	public void processMessage(String sqsMessageId) throws IOException {
		// TODO Auto-generated method stub

	}

	private void log(Object... objects) {
		for (Object o : objects) {
			String s = "";
			s = s + " " + o;
			System.err.println(s);
			log.debug("" + s);
		}
	}

	public static String stackTraceToString(Exception e) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bo);
		e.printStackTrace(ps);
		ps.close();
		return bo.toString();
	}
}