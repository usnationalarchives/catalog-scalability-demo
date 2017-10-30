package gov.nara.das.ingest.controllers;

import java.awt.geom.FlatteningPathIterator;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import static gov.nara.das.util.Utils.*;
import static org.matt.utils.Utils.println;

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
	// private DBFacade dbf;

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

	@RequestMapping(value = "/v1/ingest/descriptionJob/inprocess", method = RequestMethod.POST)
	public ResponseEntity<JobsQueryResponse> getInProcess() {
		List<Job> jobs=jobTemplate.getJobsInProcess(120, 50);
		JobsQueryResponse response=new JobsQueryResponse(jobs);
		ResponseEntity<JobsQueryResponse> r = new ResponseEntity<JobsQueryResponse>(response, HttpStatus.OK);
		return r;

	}
	@RequestMapping(value = "/v1/ingest/descriptionJob/{jobId}", method = RequestMethod.POST)
	public ResponseEntity<JobQueryResponse> getJobStatus(@PathVariable Long jobId) {
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
		JobQueryResponse response = new JobQueryResponse();
		List<MessageStatus> messageStatuses=messageStatusTemplate.getByJobId(jobId);
		if (job != null) {
			response = new JobQueryResponse(job,messageStatuses);
			log.debug("message statuses="+messageStatuses);
			response.setCurrentAction(jobActionTypeTemplate.getByActionId(job.getCurrentActionTypeId()));
			response.setJobActionHistory(jobActionHistoryTemplate.getByJobId(job.getJobId()));

		}else{
			response.setError("job record with id="+jobId+" not found.");
		}

		ResponseEntity<JobQueryResponse> r = new ResponseEntity<JobQueryResponse>(response, HttpStatus.OK);
		return r;
	}

	@RequestMapping(value = "/v1/ingest/descriptionJob", method = RequestMethod.POST)
	public ResponseEntity<IngestRequestResponse> update(@RequestBody IngestRequestResponse ingestRequestData) {
		IngestRequestResponse response = new IngestRequestResponse();
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
			response.setJobId(job.getJobId());
			response.setStatus("CREATED");
			if (!createdMessage) {
				error += ",Created job but failed to create messaage. exception=" + ex;
			}
		} else {
			error += ",failed to create job. exception=" + ex;
		}
		if (error != null) {
			response.setError(error);
		}
		ResponseEntity<IngestRequestResponse> r = new ResponseEntity<IngestRequestResponse>(response, HttpStatus.CREATED);

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
