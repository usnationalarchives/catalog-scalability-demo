package gov.nara.das.monitor;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.*;

import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.JobActionHistory;
import gov.nara.das.common.db.ingest.dao.JobActionHistoryDAO;
import gov.nara.das.common.db.ingest.dao.JobActionTypesDAO;
import gov.nara.das.common.db.ingest.dao.impl.JobActionHistoryTemplate;
import gov.nara.das.common.db.ingest.dao.impl.JobTemplate;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 *         Change Log
 *
 *         date contributor comments 2017-08-08 Matthew Mariano : created
 *
 * @formatter:on
 */
@EnableAutoConfiguration
@Component
@ComponentScan({ "gov.nara.das.common.db" })
public class JobMonitor {

	Random random = new Random(System.currentTimeMillis());

	@Autowired
	private JobActionHistoryTemplate jobActionHistoryTemplate;

	@Autowired
	private JobTemplate jobTemplate;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private int concurrentCount = 30;

	private String[] args;

	private boolean logToConsole = false;
	private volatile Long threadIndex = 0L;

	private long scanStartThresholdSeconds;
	private long obsoleteThresholdSeconds;
	private long jobSleepBetweenStatusChecksMilliseconds;

	private Map<Future, MyCallable> futureCallableMap = new TreeMap<Future, MyCallable>();
	private ArrayList<MyFuture> futures = new ArrayList<MyFuture>();

	private Map<Long, Object> jobThreadMap = new TreeMap<Long, Object>();

	//

	//
	public class XMLFileFilter implements FilenameFilter {

		public boolean accept(File dir, String name) {
			return name.endsWith("xml");
		}

	}

	public class MyFuture implements Future, Comparable {

		private final Future future;

		private final MyCallable myCallable;

		private final Job job;

		public String getTag() {
			return "jobId=" + job.getJobId();
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
		public MyFuture(Future afuture, MyCallable acallable, Job aJob) {
			future = afuture;
			myCallable = acallable;
			job = aJob;
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
		private StringBuffer buffer = new StringBuffer();
		private Long threadId;
		private Long friendlyThreadId;
		private Job job;

		/**
		 * @return the buffer
		 */
		public StringBuffer getBuffer() {
			return buffer;
		}

		public MyCallable(Job aJob) {
			friendlyThreadId = getNextIndexForThread();
			threadId = Thread.currentThread().getId();
			job = aJob;
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
			try {
				while (true) {
					long milli =System.currentTimeMillis()-1000*job.getEpochSecondsCreated();
					boolean completed=false;
					try{
						completed=jobTemplate.getJobCompletedStatus(job.getJobId());
					}catch(Exception e){
						log.error("Exception caught while getting completion "
								+ "status for jobId="+job.getJobCreatedUser()
								+ " Exception="+e);
						
					}
					log.debug("completed ="+completed+" for jobId="+job.getJobId());
					if(completed){
						log.error("closing jobId="+job.getJobId());
						closeJob(job);
						break;
					}else if (milli / 1000 > obsoleteThresholdSeconds) {
						obsolesceJob(job);
						log.debug("obsolescing job with id="+job.getJobId());
						break;
					}
					Thread.currentThread().sleep(jobSleepBetweenStatusChecksMilliseconds);
				}
			} catch (Exception e) {
				failed = true;
				exception = e;
				log("processing failed: " + e.getMessage(), stackTraceToString(e));

			} finally {
				endTime = System.currentTimeMillis();
				mylog(repeatString("_", 80, ""));
			}
		}

		private void closeJob(Job job) {
			log.debug("closing job:" + job.getJobId());
			job.setPassed(true);
			job.setFailed(false);
            jobTemplate.updateCurrentAction(job,JobActionTypesDAO.ActionTypes.COMPLETED.id);
			jobTemplate.updatePassFailWithCheck(job);
			jobActionHistoryTemplate.insert(job, JobActionTypesDAO.ActionTypes.COMPLETED.id);;
		}

		private void obsolesceJob(Job job) {
			log.debug("failing job:" + job.getJobId());
			job.setPassed(false);
			job.setFailed(true);
			jobTemplate.updatePassFailWithCheck(job);
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
	}

	public static void usage() {
		System.out.println("Uage: java JobMonitor <files>"
				+ "\nwhere <files> are 1 or more xml files or directories with xml files to process."
				+ "\nif a directory is specified all files in that directory, with an xml extension, will be processed.");
	}

	public static void main(String[] args) {
		try {

			ConfigurableApplicationContext ctx = SpringApplication.run(JobMonitor.class, args);
			JobMonitor m = ctx.getBean(JobMonitor.class);
			m.init();
			m.process(args);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * for Spring
	 */
	public JobMonitor() {

	}

	public JobMonitor(String[] aargs) {
		args = aargs;
		init();
	}

	private void init() {

	}

	public void process(String[] aargs) throws IOException {
		args = aargs;
		init();
		process();
	}

	public void process() throws IOException {
		log.debug("concurrentCount=" + concurrentCount);
		ExecutorService executorService = Executors.newFixedThreadPool(concurrentCount);
		try {
			log("concurrentCount=" + concurrentCount);
			long processCount = 0;
			int bad = 0;
			while (true) {

				// get the jobs in process. concurrentCount is the limit of
				// records to get. //
				List<Job> jobs = jobTemplate.getJobsInProcess(scanStartThresholdSeconds, concurrentCount);
				log("jobs.size==" + jobs.size());
				log.error("jobs.size==" + jobs.size());
				for (Job j : jobs) {
					if (futures.size() < concurrentCount) {
						log("processing job=" + j);
						;
						log("retrieved_time=" + j.getRetrievedTime() + " for jobId=" + j.getJobId());
						log("total=" + j.getTotalDescriptionCount() + " for jobId=" + j.getJobId());
						log("success=" + j.getSuccessCount() + " for jobId=" + j.getJobId());
						log("failed=" + j.getFailureCount() + " for jobId=" + j.getJobId());
//
						log.error("processing job=" + j);
						log.error("retrieved_time=" + j.getRetrievedTime() + " for jobId=" + j.getJobId());
						log.error("total=" + j.getTotalDescriptionCount() + " for jobId=" + j.getJobId());
						log.error("success=" + j.getSuccessCount() + " for jobId=" + j.getJobId());
						log.error("failed=" + j.getFailureCount() + " for jobId=" + j.getJobId());
						
						// Thread.currentThread().sleep(10000);
						MyCallable callable = new MyCallable(j);
						Future future = executorService.submit(callable);
						MyFuture future2 = new MyFuture(future, callable, j);
						futures.add(future2);
						futureCallableMap.put(future2, callable);
						jobThreadMap.put(j.getJobId(), j);
					} else {
						manageFutures(futures, concurrentCount - 1);
					}
				}
				Thread.currentThread().sleep(5000);
			}
			// wait up to 3 minutes for any futures to finish.
			//
			// executorService.shutdown();
			// log("Proccessing completed");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String flatten(String[] a, String delim) {
		StringBuffer b = new StringBuffer();
		b.append(a[0]);
		for (int i = 1; i < a.length; i++) {
			b.append(".");
			b.append(a[i]);
		}
		return b.toString();
	}

	private synchronized void log(Object... objects) {
		for (Object o : objects) {
			System.err.println(o);
			log.error("" + o);
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

	private void logStacktrace(Exception e) {
		if (logToConsole) {
			System.out.println(stackTraceToString(e));
		}
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
					jobThreadMap.remove(future.myCallable.job.getJobId());
					// futures.remove(future);
					MyCallable c = futureCallableMap.get(future);
					log("manageFutures: " + future.getTag() + ", friendlyThreadId=" + c.friendlyThreadId
							+ ", start / end = " + date2String(c.startTime) + "/" + date2String(c.endTime));
					long seconds = (c.endTime - c.startTime) / 1000;
					log("manageFutures: message index=" + future.getTag() + ", friendlyThreadId=" + c.friendlyThreadId
							+ ", total process time seconds=" + seconds);
					log("buffer=" + c.getBuffer().toString());
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

	@Value("${concurrent.count}")
	public void setConcurrentCount(int count) {
		concurrentCount = count;
	}

	@Value("${scan.start.threshold.seconds}")
	public void setScanStartThresholdSeconds(long s) {
		scanStartThresholdSeconds = s;
	}

	@Value("${obsolete.threshold.seconds}")
	public void setObsoleteThresholdSeconds(long s) {
		obsoleteThresholdSeconds = s;
	}

	@Value("${job.sleep.between.status.check.milliseconds}")
	public void setJobSleepBetweenStatusChecksMilliseconds(long s) {
		jobSleepBetweenStatusChecksMilliseconds = s;
	}

}