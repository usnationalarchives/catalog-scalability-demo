package gov.nara.das.ingest.configs;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Configuration("config")

public class Config {
    /**
     * according to the documentation, for queues with a small # of messages,
     * the receive messages may return 0 and the work-around is simply to retry.
     * so we introduce a small # of retries
     */
	private volatile Map<String,Long> messageMap=new TreeMap<String,Long>();
    public static final int RETRY_COUNT = 3;
    public static int CONCURRENT_COUNT = -1;
    private AmazonSQS sqsClient;
    /**
     * environment variables
     */
    private String queueName;

    private String queueUrl;
    private String endpoint;
    //
    private long visibilityTimeout=30000;

    private void init(){

        queueUrl = System.getenv("INGEST_QUEUE_URL");
        endpoint = "https://sqs.us-east-1.amazonaws.com";
        initQ();
       // log(queueUrl);

    }
    public Config(){
        init();
    }

    private void initQ() {
    	sqsClient = AmazonSQSClientBuilder.defaultClient();
    }
    public AmazonSQS getSqsClient() {
        return sqsClient;
    }

    public void setSqsClient(AmazonSQS sqsClient) {
        this.sqsClient = sqsClient;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getQueueUrl() {
        return queueUrl;
    }

    public void setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public long getVisibilityTimeout() {
        return visibilityTimeout;
    }

    public void setVisibilityTimeout(long visibilityTimeout) {
        this.visibilityTimeout = visibilityTimeout;
    }
	public  synchronized boolean checkMessageMap(String jobIdSqsMessageId){
		try{
			if(messageMap.containsKey(jobIdSqsMessageId)){
				return false;
			}else{
				messageMap.put(jobIdSqsMessageId, System.currentTimeMillis());
				return true;
			}
		}catch(Exception e){

		}
		return true;
	}
	private synchronized void cleanMessageMap(){
		try{
			for(String key:messageMap.keySet()){
				Long time=messageMap.get(key);
				if(System.currentTimeMillis() - time > 600000){
					messageMap.remove(key);
				}
			}
		}catch(Exception e){
			
		}
	}
	public  synchronized void removeFromMessageMap(String jobIdSqsMessageId){
		try{
			if(messageMap.containsKey(jobIdSqsMessageId)){
				messageMap.remove(jobIdSqsMessageId);
			}
			cleanMessageMap();
		}catch(Exception e){
			
		}
	}
}
