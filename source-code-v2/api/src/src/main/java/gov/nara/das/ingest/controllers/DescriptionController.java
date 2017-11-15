package gov.nara.das.ingest.controllers;

import static gov.nara.das.common.elastic.ElasticSearchUtils.convertToElasticSearchJSON;
import static gov.nara.das.common.db.das.dao.FindingAidDAO.FindingAidType;
import static gov.nara.das.common.db.das.dao.FindingAidDAO.ObjectType;
import static gov.nara.das.common.db.das.dao.FindingAidDAO.FindingAidURL;
import static gov.nara.das.common.utils.Utils.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.nara.das.common.db.das.AccessRestriction;
import gov.nara.das.common.db.das.AuthorityList;
import gov.nara.das.common.db.das.Description;
import gov.nara.das.common.db.das.DigitalObject;
import gov.nara.das.common.db.das.FindingAid;
import gov.nara.das.common.db.das.SpecialProjectForDescription;
import gov.nara.das.common.db.das.dao.AuthorityListDAO;
import gov.nara.das.common.db.das.dao.DescriptionDAOInterface;
import gov.nara.das.common.db.das.dao.FindingAidDAO;
import gov.nara.das.common.db.das.dao.impl.AccessRestrictionTemplate;
import gov.nara.das.common.db.das.dao.impl.AuthorityListTemplate;
import gov.nara.das.common.db.das.dao.impl.DescriptionTemplate;
import gov.nara.das.common.db.das.dao.impl.DigitalObjectTemplate;
import gov.nara.das.common.db.das.dao.impl.FindingAidTemplate;
import gov.nara.das.common.db.das.dao.impl.SpecialProjectForDescriptionTemplate;
import gov.nara.das.common.db.das.jaxb.Import;
import gov.nara.das.common.db.das.jaxb.Import.RecordGroupArray.RecordGroup;
import gov.nara.das.common.db.das.jaxb.Import.RecordGroupArray.RecordGroup.SpecialProjectArray.SpecialProject;
import gov.nara.das.common.db.das.jaxb.Import.SeriesArray.Series;
import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.MessageStatus;
import gov.nara.das.common.db.ingest.dao.impl.JobActionHistoryTemplate;
import gov.nara.das.common.db.ingest.dao.impl.JobActionTypeTemplate;
import gov.nara.das.common.db.ingest.dao.impl.JobTemplate;
import gov.nara.das.common.db.ingest.dao.impl.MessageStatusTemplate;
import gov.nara.das.common.response.CreateDescriptionResponse;
import gov.nara.das.common.response.SingleDescriptionResponse;
import gov.nara.das.ingest.ElasticSearchHTTPService;
import gov.nara.das.ingest.configs.Config;
import gov.nara.das.util.ESResponse;

import static gov.nara.das.common.utils.JSONUtils.*;
import static gov.nara.das.common.utils.ResponseUtils.*;
@CrossOrigin(origins = "*")
@Controller
@ComponentScan({ "gov.nara.das.ingest","gov.nara.das.ingest.configs", "gov.nara.das.common.db" })
public class DescriptionController implements ApplicationContextAware {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private ApplicationContext applicationContext;
	//
	@Autowired
	public Config config;

	@Autowired
	private DBFacade dbf;

	@Autowired
	private DescriptionTemplate descTemplate;

	@Autowired
	private JobTemplate jobTemplate;

	@Autowired
	private MessageStatusTemplate messageStatusTemplate;
	
	@Autowired
	private JobActionTypeTemplate jobActionTypeTemplate;
	
	@Autowired
	private JobActionHistoryTemplate jobActionHistoryTemplate;
	     
	@Autowired
	private ElasticSearchHTTPService esService;
	
	@Autowired
	private AuthorityListTemplate authListTemplate;
	
	@Autowired
	private SpecialProjectForDescriptionTemplate spdTemplate;
	
	
	@Autowired
	private FindingAidTemplate findingAidTemplate;
	
	@Autowired
	private DigitalObjectTemplate digitalObjectTemplate;
	
	@Autowired
	private AccessRestrictionTemplate accessRestrictionTemplate;	
	
	@RequestMapping(value = "/v1/api/entity/description-test{naid}", method = RequestMethod.GET)
	public ResponseEntity<String> getDescriptionByNaidTest
	(@PathVariable Long naid) {
		Description description= null;
		ResponseEntity<String> response;
		SingleDescriptionResponse sdr=null;
		String json="";
		try{
			//DBFacade999 dbf=new DBFacade999();
			description=dbf.getDescriptionByNaid(naid);
			log.debug("description returned by db=" + description);
			json=convertDescriptionToJSON(description);
			json=removeNullValues(json);
			response=new ResponseEntity<String >(json,HttpStatus.OK);
		}catch(Exception e){
			log.debug(stackTraceToString(e));
			response=new ResponseEntity<String>(stackTraceToString(e)+"\noriginal json="+json,HttpStatus.EXPECTATION_FAILED);
			//String json=response.ge;
			//log.debug("returning json body="+json);
		}
		return response;
	}	
	
	@RequestMapping(value = "/v1/api/entity/description/{naid}", method = RequestMethod.GET)
	public ResponseEntity<String> getDescriptionByNaid(@PathVariable Long naid) {
		Description description= null;
		ResponseEntity<String> response;
		SingleDescriptionResponse sdr=null;
		String json=null;
		try{
			//DBFacade999 dbf=new DBFacade999();
			description=dbf.getDescriptionByNaid(naid);
			log.debug("description returned by db=" + description);
			if(description!=null){
				json=convertDescriptionToJSON(description);
				
				log.debug("json before="+json);
				json=removeNullValues(json);
				response=new ResponseEntity<String>(json,HttpStatus.OK);
			}else{
				response=new ResponseEntity<String>("description, "+naid+", not found.",HttpStatus.NOT_FOUND);
			}
		}catch(Exception e){
			String m=stackTraceToString(e);
			if(json !=null){
				m=m+"\n"+json;
				
			}
			log.debug(stackTraceToString(e));
			
			response=new ResponseEntity<String>(m,HttpStatus.EXPECTATION_FAILED);
		}
		return response;
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
	@RequestMapping(value = "/v1/api/entity/descriptionBatch", method = RequestMethod.POST)
	public ResponseEntity<String> postNewDescriptionBatch(@RequestBody String rawXML) {
		System.err.println("rawXML="+rawXML);
		int metaLength=rawXML.indexOf("</meta>")+"</meta>".length();
		String meta=rawXML.substring(0, metaLength);
		String indicesString=findTaggedString(meta, "indices");
		String lengthsString=findTaggedString(meta, "lengths");
		List<Integer> indices=unflattenIntegerString(indicesString);
		List<Integer> lengths=unflattenIntegerString(lengthsString);
		String message64=rawXML.substring(metaLength);
		String message=new String(Base64.getDecoder().decode(message64.getBytes()));
		int i=0;
		for(int index:indices){
			String messagei=message.substring(index,index+lengths.get(i));
			System.err.println("message "+i+"= "+messagei);
			log.debug("message "+i+"= "+messagei);
			postNewDescription(message);
			i++;
		}
		JSONObject j=new JSONObject();
		j.put("message", message);
		j.put("createdCount",indices.size());

		System.err.println("postNewDescriptionBatch: message="+message);
		System.err.println("indicesString="+indicesString);
		log.debug("indicesString="+indicesString);
		ResponseEntity<String> r=new ResponseEntity<String>(j.toString(3),HttpStatus.CREATED);
		return r;
	}
	@RequestMapping(value = "/v1/api/entity/description", method = RequestMethod.POST)
	public ResponseEntity<String> postNewDescription(@RequestBody String rawXML) {
		String flow = "";
		boolean created = false;
		Job job = new Job();
		Exception ex = null;
		log.debug("IngestController.postNewDescription(...) time=" + System.currentTimeMillis());
		String json=null;
		long createdNaid=-1;
		try {
			//
			//DBFacade999 dbf=new DBFacade999();
			Description description=dbf.createDescription(rawXML);
			if(description==null){
				throw new RuntimeException("description is null. DBFacade error message="+dbf.getError());
			}
			createdNaid=description.getDescNaId();
			System.err.println("json before="+convertToJSON(description));
			json=convertDescriptionToJSON(description);
			json=removeNullValues(json);
			log.debug("postNewDescription: sqsMessageId="+description.getSqsMessageId());
			//
			if (description.getJobId() > 0) {
				flow += ", found jobId=" + description.getJobId();
			} else {
				flow += ", NO jobId found";
			}
			//
			job.setLastChunkedGuId(description.getSqsMessageId());
			job.setJobId(description.getJobId());
			log.debug("updating counts for jobId="+description.getJobId());
			jobTemplate.updateInsertCount(job);
			//
			try{
				String json99=convertToElasticSearchJSON(description);
				log.debug("sending to ES json="+json99);
				ESResponse esResponse=esService.postNew(description);
				log.debug("ElasticSearch response code="+esResponse.getResponse().getStatusCodeValue());
				log.debug("ElasticSearch posted json="+esResponse.getRequest().getBody());
				log.debug("ElasticSearch response json="+esResponse.getResponse().getBody());
			}catch(Exception e){
				log.debug("Exception posting to ElasticSearch: "+e+ "  sqsMessageId="+description.getSqsMessageId() + " jobId="+description.getJobId()+ "naId="+description.getDescNaId());
			}
// 2017-10-17 
			MessageStatus ms = new MessageStatus();
			ms.setNaid(createdNaid);
			ms.setSqsMessageId(""+description.getSqsMessageId());
			ms.setMessageIndex(description.getMessageIndex());
			ms.setGUID(description.getGUID());
			ms.setJobId(job.getJobId());
			ms.setInsertSuccess(true);
			messageStatusTemplate.upsert(ms);
			created = true;
		} catch (Exception e) {
			System.err.println(rawXML);
			ex = e;
			e.printStackTrace(System.err);
			json=stackTraceToString(e);
			
		}
//		//
		
		CreateDescriptionResponse response = new CreateDescriptionResponse();
		response.setError(flow);
		response.setNaid(createdNaid);
		ResponseEntity<String> r=null;
		if(created){
			r = new ResponseEntity<String>(json, HttpStatus.CREATED);
		}else{
			r = new ResponseEntity<String>(json, HttpStatus.CONFLICT);
		}
		log.debug("sending repsonse to client: "+System.currentTimeMillis() +" "+date2String(System.currentTimeMillis() ));
		return r;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getContext() {
		return applicationContext;
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

}
