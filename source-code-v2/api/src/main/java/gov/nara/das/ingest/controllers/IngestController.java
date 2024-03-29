package gov.nara.das.ingest.controllers;

import static gov.nara.das.common.utils.Utils.*;

import java.awt.geom.FlatteningPathIterator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import gov.nara.das.ingest.configs.*;
import gov.nara.das.common.db.das.dao.impl.DescriptionTemplate;
import gov.nara.das.common.db.das.jaxb.Import;
import gov.nara.das.common.db.das.jaxb.Import.RecordGroupArray.RecordGroup;
import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.MessageStatus;
import gov.nara.das.common.db.ingest.dao.impl.JobActionHistoryTemplate;
import gov.nara.das.common.db.ingest.dao.impl.JobActionTypeTemplate;
import gov.nara.das.common.db.ingest.dao.impl.JobTemplate;
import gov.nara.das.common.db.ingest.dao.impl.MessageStatusTemplate;
import gov.nara.das.common.response.IngestRequestResponse;
import gov.nara.das.common.response.JobQueryResponse;
import gov.nara.das.common.response.JobsQueryResponse;
import gov.nara.das.common.response.XMLValidationResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ch.qos.logback.core.boolex.Matcher;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;

import static gov.nara.das.common.utils.JSONUtils.*;
@CrossOrigin(origins = "*")
@Controller
@ComponentScan({ "gov.nara.das.ingest.configs", "gov.nara.das.common.db" })
public class IngestController implements ApplicationContextAware {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private String port;
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	private ApplicationContext applicationContext;
	//
	@Autowired
	public Config config;

	// @Autowired
	// private DBFacade999 dbf;

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

	public String findTaggedString(String xml, String tag) {
		String value = null;
		Pattern p = Pattern.compile("<" + tag + ">(.*?)<\\/" + tag + ">");
		java.util.regex.Matcher m = p.matcher(xml);
		if (m.find()) {
			value = m.group(1);
		}
		return value;
	}

	public Long findTaggedLong(String xml, String tag) {
		Long value = null;
		Pattern p = Pattern.compile("<" + tag + ">(\\d+)<\\/" + tag + ">");
		java.util.regex.Matcher m = p.matcher(xml);
		if (m.find()) {
			value = Long.parseLong(m.group(1));
		}
		return value;
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

	@RequestMapping(value = "/v1/api/ingest/descriptionJob/inprocess", method = RequestMethod.GET)
	public ResponseEntity<String> getInProcess() {
		List<Job> jobs=jobTemplate.getJobsInProcess(120, 50);
		JobsQueryResponse jqr=new JobsQueryResponse(jobs);
		String json="";
		if(jqr!=null ){
			json=convertToJSON(jqr);
		}else{
			JSONObject j=new JSONObject();
			j.put("message", "no in process jobs found");
			json=j.toString(3);
		}
		ResponseEntity<String> r = new ResponseEntity<String>(json, HttpStatus.OK);
		return r;

	}
	@RequestMapping(value = "/v1/api/ingest/descriptionJob/{jobId}", method = RequestMethod.GET)
	public ResponseEntity<String>  getJobStatus(@PathVariable Long jobId) {
		boolean createdJob = false;
		boolean createdMessage = false;
		Exception ex = null;
		Job job = new Job();
		log.debug("descriptionJob");
		try {
			job = jobTemplate.getById(jobId);
		} catch (Exception e) {
			ex = e;
			e.printStackTrace(System.err);
		}
		//
		JobQueryResponse jqResponse = new JobQueryResponse();
		List<MessageStatus> messageStatuses=messageStatusTemplate.getByJobId(jobId);
		if (job != null) {
			jqResponse = new JobQueryResponse(job,messageStatuses);
			log.debug("message statuses="+messageStatuses);
			jqResponse.setCurrentAction(jobActionTypeTemplate.getByActionId(job.getCurrentActionTypeId()));
			jqResponse.setJobActionHistory(jobActionHistoryTemplate.getByJobId(job.getJobId()));

		}else{
			jqResponse.setError("job record with id="+jobId+" not found.");
		}
		String json=convertToJSON(jqResponse);
		json=removeNullValues(json);
		ResponseEntity<String> r = new ResponseEntity<String>(json, HttpStatus.OK);
		return r;
	}
	@RequestMapping(value = "/v1/ingest/angular", method = RequestMethod.GET)
	@CrossOrigin(origins="http://localhost:4200")
    public ResponseEntity<Collection<Map<String,String>>> angularTest1() {
		Collection<Map<String,String>> list=new ArrayList<Map<String,String>>();
		Map<String,String> map=new HashMap<String,String>();
		map.put("name","ajkhfdajskfhk");
		map.put("id",""+System.currentTimeMillis());
		list.add(map);
		System.err.println("returning map");
		ResponseEntity<Collection<Map<String,String>>> r=new ResponseEntity<Collection<Map<String,String>>>(list,HttpStatus.OK);
		return r;
    }

	@RequestMapping(value = "v1/api/ingest/descriptionJob", method = RequestMethod.POST)
	public ResponseEntity<String> update(@RequestBody IngestRequestResponse ingestRequestData) {
		IngestRequestResponse ingestRequestResponse = new IngestRequestResponse();
		boolean createdJob = false;
		boolean createdMessage = false;
		Exception ex = null;
		Job job = new Job();
		String error = "";
		long time=System.currentTimeMillis();
		log.debug("IngestController.update(...) "+time);

		// 
		try {
			job.setJobCreatedUser("ingestjob");
			jobTemplate.create(job);
			createdJob = true;
			
			JSONObject o = new JSONObject();
	        o.put("url",ingestRequestData.getUrl());
	        o.put("status",HttpStatus.CREATED.toString());
			o.put("jobId", job.getJobId());
			config.getSqsClient().sendMessage(config.getQueueUrl(), o.toString());
			createdMessage = true;
		} catch (Exception e) {
			ex = e;
			log.debug("Exception caught:" + e.getMessage());
			log.debug("Exception caught", e);
			error+="Exception caught:"+ex.getMessage();
			if(true==true){
				throw new RuntimeException("ouch"+e);
			}
		}
		//
		
		
		if (createdJob) {
			ingestRequestResponse.setJobId(job.getJobId());
			ingestRequestResponse.setStatus("CREATED");
			if (!createdMessage) {
				error += ",Created job but failed to create messaage. exception=" + ex;
			}
		} else {
			error += ",failed to create job. exception=" + ex;
		}
		if (error != null) {
			ingestRequestResponse.setError(error);
		}
		String json=convertToJSON(ingestRequestResponse);
		json=removeNullValues(json);
		ResponseEntity<String> r = new ResponseEntity<String>(json, HttpStatus.CREATED);
		return r;
	}
	@Value(value = "${server.port}")
	public void setServerPort(String aport) {

		port = aport;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getContext() {
		return applicationContext;
	}
}
