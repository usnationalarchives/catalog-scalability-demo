package gov.nara.das.ingest.controllers;

import java.awt.geom.FlatteningPathIterator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import gov.nara.das.ingest.ElasticSearchHTTPService;
import gov.nara.das.ingest.configs.*;
import gov.nara.das.common.db.*;
import gov.nara.das.common.db.das.Description;
import gov.nara.das.common.db.das.dao.impl.AuthorityListTemplate;
import gov.nara.das.common.db.das.dao.impl.DescriptionTemplate;
import gov.nara.das.common.db.das.dao.impl.SpecialProjectForDescriptionTemplate;
import gov.nara.das.common.db.das.jaxb.Import;
import gov.nara.das.common.db.das.jaxb.Import.RecordGroupArray.RecordGroup;
import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.MessageStatus;
import gov.nara.das.common.db.ingest.dao.impl.JobActionHistoryTemplate;
import gov.nara.das.common.db.ingest.dao.impl.JobActionTypeTemplate;
import gov.nara.das.common.db.ingest.dao.impl.JobTemplate;
import gov.nara.das.common.db.ingest.dao.impl.MessageStatusTemplate;
import gov.nara.das.common.response.CreateDescriptionResponse;
import gov.nara.das.common.response.IngestRequestResponse;
import gov.nara.das.common.response.JobQueryResponse;
import gov.nara.das.common.response.SingleDescriptionResponse;
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

import static gov.nara.das.common.elastic.ElasticSearchUtils.convertToElasticSearchJSON;
import static gov.nara.das.util.Utils.*;
import static org.matt.utils.Utils.println;

@Controller
@ComponentScan({ "gov.nara.das.ingest.configs", "gov.nara.das.common.db" })
public class DescriptionController implements ApplicationContextAware {
	private final Logger log = LoggerFactory.getLogger("xxx");
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
	     
	@Autowired
	private ElasticSearchHTTPService esService;
	
	@Autowired
	private AuthorityListTemplate authListTemplate;
	
	@Autowired
	private SpecialProjectForDescriptionTemplate spdTemplate;

	@RequestMapping(value = "/v1/ingest/description/{naid}", method = RequestMethod.POST)
	public ResponseEntity<SingleDescriptionResponse> getDescriptionByNaid(@PathVariable Long naid) {
		Description description= null;
		ResponseEntity<SingleDescriptionResponse> response;
		SingleDescriptionResponse sdr=null;
		try{
			description=descTemplate.getById(naid);
			log.error("description returned by db=" + description);
			sdr=new SingleDescriptionResponse(description);
			response=new ResponseEntity<SingleDescriptionResponse>(sdr,HttpStatus.OK);
		}catch(Exception e){
			sdr=new SingleDescriptionResponse(description);
			sdr.setError("Exception="+e+ " message="+e.getMessage());
			response=new ResponseEntity<SingleDescriptionResponse>(sdr,HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
	@RequestMapping(value = "/v1/ingest/description", method = RequestMethod.POST)
	public ResponseEntity<CreateDescriptionResponse> postNewDescription(@RequestBody String rawXML) {
		String flow = "";
		boolean created = false;
		Job job = new Job();
		Exception ex = null;
		log.debug("IngestController.postNewDescription(...) time=" + System.currentTimeMillis());

		long createdNaid=-1;
		try {
			//
			DBFacade dbf=new DBFacade();
			Description description=dbf.createDescription(rawXML);
			if(description==null){
				throw new RuntimeException("description is null. DBFacade error message="+dbf.getError());
			}
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
			log.error("updating counts for jobId="+description.getJobId());
			jobTemplate.updateInsertCount(job);
			//
			try{
				esService.postNew(description);
				log.debug("posted json to ElasticSearch="+convertToElasticSearchJSON(description));
			}catch(Exception e){
				log.error("Exception posting to ElasticSearch: "+e+ "  sqsMessageId="+description.getSqsMessageId() + " jobId="+description.getJobId()+ "naId="+description.getDescNaId());
			}
// 2017-10-17 
			MessageStatus ms = new MessageStatus();
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
			flow += ", exception caught:" + e;
			e.printStackTrace(System.err);
			
		}
//		//
		CreateDescriptionResponse response = new CreateDescriptionResponse();
		response.setError(flow);

		response.setNaid(createdNaid);
		
		ResponseEntity<CreateDescriptionResponse> r = new ResponseEntity<CreateDescriptionResponse>(response, HttpStatus.CREATED);
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

public class DBFacade {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private String error=null;
	private boolean failed=false;

	
	public Description createDescription(String xml){
		failed=false;
		Description description=new Description();
		try{
			log.error("000000000");
			System.err.println("0");
			JAXBContext jc = JAXBContext.newInstance(Import.class.getPackage().getName());
			log.error("1");
			Unmarshaller u=jc.createUnmarshaller();
			Import imp=(Import)u.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes())));
			log.error("3");
			if(imp.getRecordGroupArray().getRecordGroup().size() > 0){
				log.error("5");
				createRecordGroup(imp,xml,description);
			}
			
		}catch(Exception e){
			log.error(e.getMessage());
			e.printStackTrace(System.err);
			System.err.println(e);
			description=null;
			failed=true;
			error="Caught Exception="+e+" , "+e.getMessage();
		}
		return description;
	}
		public Description createRecordGroup(Import imp, String xml ,Description description){
			RecordGroup rg=imp.getRecordGroupArray().getRecordGroup().get(0);
			JSONObject jo= XML.toJSONObject(xml);
			String dcg=jo.getJSONObject("import").getJSONObject("recordGroupArray")
					.getJSONObject("recordGroup").getJSONObject("dataControlGroup").toString(3);
			description.setGUID(rg.getGuid());
			description.setTitle(rg.getTitle());
			description.setDateNote(rg.getDateNote());
			description.setDataControlGroup(dcg);
			description.setSqsMessageId(rg.getSqsMessageId());
			description.setChunkingId(rg.getChunkingId());
			description.setMessageIndex(rg.getMessageIndex());
			description.setJobId(rg.getJobId());
			
			long createdNaid=descTemplate.create(description);
			description.setDescNaId(createdNaid);
			return description;
		}
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return the failed
	 */
	public boolean isFailed() {
		return failed;
	}

	/**
	 * @param failed the failed to set
	 */
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
}

}
