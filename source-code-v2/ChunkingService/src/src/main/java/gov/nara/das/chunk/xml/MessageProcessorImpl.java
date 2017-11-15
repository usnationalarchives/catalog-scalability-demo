package gov.nara.das.chunk.xml;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
/**
 * @brief
 * Sends messages to SQS. Supports batching wherein more than one description xml may be
 * sent as one message. 
 * 
 * @author matthew mariano
 *
 * Ticket   Date       Contributor                     comments
 *          2017-10-24 Matthew Mariano                 created
 */
public class MessageProcessorImpl  implements MessageProcessor{
	
	@Autowired
	@Value("{batch.size}")
	private  int batchSize;
	
	private final long jobId;
	private final AmazonSQS sqsClient;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final String descriptionQueueURL;
	List<InternalMessage> messageList=new ArrayList<InternalMessage>();
	private class InternalMessage{
		private final String messageData;
		private final int messageIndex;
		public InternalMessage(String data, int index){
			messageData=data;
			messageIndex=index; 
		}
	}
	public MessageProcessorImpl(long aJobId,String queueURL, AmazonSQS client){
		sqsClient=client;
		descriptionQueueURL=queueURL;
		jobId=aJobId;
	}
	@Override
    public int processMessage(IParser parser , String messageData,long jobId, int messageIndex, String type) {
        if(messageData!=null && messageData.length() > 100){
        	//log.debug(messageData.substring(0, 99)+".........");
        }
        messageList.add(new InternalMessage(messageData, messageIndex));
        log.debug("job id="+parser.getJobId()+", total="+ parser.getTotal()+", chunking message id="+parser.getCurrentMessageId());

        if(messageList.size() >= batchSize){
        	sendAllMessages();
        }

        // delete the tmp file
        
        return 0;
    }
	protected void sendAllMessages(){
		System.err.println("sendAllMessages");
		StringBuffer b=new StringBuffer();
		StringBuffer b2=new StringBuffer();
		StringBuffer b3=new StringBuffer();
		StringBuffer blengths=new StringBuffer();
		int index=0;
		for(InternalMessage m:messageList){
			if(index>0){
				b.append(",");
				b2.append(",");
				blengths.append(",");
			}
			b.append(index);
			b2.append(m.messageIndex);
			b3.append(m.messageData);
			blengths.append(m.messageData.length());
			index+=m.messageData.length();
		}
		try{
			System.err.println("sendAllMessages 2");
			Map<String, MessageAttributeValue> messageAttributes = new HashMap<String, MessageAttributeValue>();
	          ByteArrayOutputStream bout = new ByteArrayOutputStream();
	          //DeflaterOutputStream dout = new DeflaterOutputStream(bout);
	          bout.write(b3.toString().getBytes());
	          //dout.finish();
	          //dout.close();
	          String zippedMessage64=Base64.getEncoder().encodeToString(bout.toByteArray());
	        SendMessageRequest smr=new SendMessageRequest(descriptionQueueURL, zippedMessage64);
	        MessageAttributeValue mv=new MessageAttributeValue();
	        //
	        mv=new MessageAttributeValue().withDataType("String").withStringValue(""+jobId);
	        messageAttributes.put("jobId", mv);
	        //
	        mv=new MessageAttributeValue().withDataType("String").withStringValue(""+b.toString());
	        messageAttributes.put("indices", mv);
	        //
	        mv=new MessageAttributeValue().withDataType("String").withStringValue(""+blengths.toString());
	        messageAttributes.put("lengths", mv);
	        //	    
	        System.err.println("sendAllMessages 3");
	        smr.withMessageAttributes(messageAttributes);
	        sqsClient.sendMessage(smr);
	        System.err.println("sendAllMessages 4");

		}catch(Exception e){
			System.err.println("sendAllMessages "+e+", "+e.getMessage());
			log.debug(e.getMessage());
		}finally{
			messageList.clear();
		}
	}
	@Override
	public void cleanup(long jobId) {
		if(messageList.size() >0) sendAllMessages();
		
	}

}
