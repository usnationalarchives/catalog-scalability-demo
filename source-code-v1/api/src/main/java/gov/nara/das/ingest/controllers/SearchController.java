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
import gov.nara.das.common.db.das.dao.impl.DescriptionTemplate;
import gov.nara.das.common.db.das.jaxb.Import;
import gov.nara.das.common.db.das.jaxb.Import.RecordGroupArray.RecordGroup;
import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.MessageStatus;
import gov.nara.das.common.db.ingest.dao.impl.JobActionHistoryTemplate;
import gov.nara.das.common.db.ingest.dao.impl.JobActionTypeTemplate;
import gov.nara.das.common.db.ingest.dao.impl.JobTemplate;
import gov.nara.das.common.db.ingest.dao.impl.MessageStatusTemplate;
import gov.nara.das.common.db.search.Search;
import gov.nara.das.common.db.search.dao.impl.SearchTemplate;
import gov.nara.das.common.request.CreateSearchRequest;
import gov.nara.das.common.response.CreateDescriptionResponse;
import gov.nara.das.common.response.CreateSearchResponse;
import gov.nara.das.common.response.IngestRequestResponse;
import gov.nara.das.common.response.JobQueryResponse;
import gov.nara.das.common.response.SearchRecordResponse;
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
public class SearchController implements ApplicationContextAware {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private String port;
	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	private ApplicationContext applicationContext;
	//
	@Autowired
	public Config config;
   
	@Autowired
	private ElasticSearchHTTPService esService;
	
	@Autowired
	private SearchTemplate searchTemplate;
	
	@RequestMapping(value = "/v1/search/descriptionSearch/{id}", method = RequestMethod.GET)
	public ResponseEntity<SearchRecordResponse> getSearchById(@PathVariable Long searchId) {

		SearchRecordResponse scr=null;
		ResponseEntity<SearchRecordResponse> response = new ResponseEntity<SearchRecordResponse>(scr, HttpStatus.CREATED);
		return response;
	}
	@RequestMapping(value = "/v1/search/descriptionSearch", method = RequestMethod.POST)
	public ResponseEntity<CreateSearchResponse> postNewSearch(@RequestBody CreateSearchRequest scr) {

		CreateSearchResponse csr=new CreateSearchResponse();
		Search s=new Search();
		try{
			s.setSearchCreatedUser("");
			s.setSearchJSON(scr.getJSON());
			searchTemplate.create(s);
			csr.setSearch(s);
		}catch(Exception e){
			csr.setError(e+" "+e.getMessage());
		}
		ResponseEntity<CreateSearchResponse> response = new ResponseEntity<CreateSearchResponse>(csr, HttpStatus.CREATED);
		return response;
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
