package gov.nara.das.ingest.controllers;

import java.awt.geom.FlatteningPathIterator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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

import gov.nara.das.ingest.ElasticSearchHTTPService;
import gov.nara.das.ingest.configs.*;
import gov.nara.das.util.ESResponse;
import gov.nara.das.common.db.*;
import gov.nara.das.common.db.das.AuthorityList;
import gov.nara.das.common.db.das.Description;
import gov.nara.das.common.db.das.DigitalObject;
import gov.nara.das.common.db.das.FindingAid;
import gov.nara.das.common.db.das.SpecialProjectForDescription;
import gov.nara.das.common.db.das.dao.AuthorityListDAO;
import gov.nara.das.common.db.das.dao.DescriptionDAOInterface;
import gov.nara.das.common.db.das.dao.FindingAidDAO;
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
import gov.nara.das.common.db.search.Search;
import gov.nara.das.common.db.search.dao.impl.SearchTemplate;
import gov.nara.das.common.request.SearchRequest;
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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;

import static gov.nara.das.common.elastic.ElasticSearchUtils.convertToElasticSearchJSON;
import static gov.nara.das.common.utils.JSONUtils.removeNullValues;
import static gov.nara.das.common.utils.Utils.*;
import static gov.nara.das.common.utils.JSONUtils.*;

@CrossOrigin(origins = "*")
@Controller
@ComponentScan({ "gov.nara.das.ingest.configs", "gov.nara.das.common.db" })
public class SearchController implements ApplicationContextAware {
	public static final String META_DATA_KEY = "metaData";
	public static final String ERROR_KEY = "error";
	public static final String INSERTED_KEY = "inserted";
	public static final String UPDATED_KEY = "updated";
	public static final String ES_RESPONSE_KEY="esResponse";

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	private ApplicationContext applicationContext;
	//

	@Autowired
	private DBFacade dbf;
	@Autowired
	public Config config;

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
	private SearchTemplate searchTemplate;

	@RequestMapping(value = "/v1/api/search/descriptionSearch/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> getSearchById(@PathVariable long id) {

		SearchRecordResponse scr = null;
		Search search = searchTemplate.getById(id);
		log.debug("search id from request="+id);
		if(search !=null){
			log.debug("search id from db="+search.getId());
		}else{
			log.debug("no search found in db for id="+id);
		}
		SearchRecordResponse srr = new SearchRecordResponse();
		srr.setSearch(search);
		if (search == null) {
			srr.setError("no search found for id=" + id);
		}
		String json = convertSearchRecordResponseToJSON(srr);
		ResponseEntity<String> response = new ResponseEntity<String>(json, HttpStatus.CREATED);
		return response;
	}

	// @RequestMapping(value = "/v1/api/search/descriptionSearch-test", method =
	// RequestMethod.POST)
	// public ResponseEntity<String> authoritySearch(@RequestBody String json) {
	// return null;
	// }

	/**
	 * 
	 * @param json
	 *            a search json. currently this is a valid es query json with an
	 *            extra object keyed meta which contains the meta data like user
	 *            and searchId. the meta data is pulled out of the ES query json
	 *            is forwarded to ES.
	 * 
	 * @return a description json object.
	 */
	@RequestMapping(value = "/v1/api/search/descriptionSearch", method = RequestMethod.POST)
	public ResponseEntity<String> postSearch(@RequestBody String json) {

		Search search = new Search();
		List<Long> naids = new ArrayList<Long>();
		JSONArray descriptionsArray = new JSONArray();
		ResponseEntity<String> response = null;
		JSONObject meta = null;
		JSONObject responseMeta = new JSONObject();
		JSONObject responseJSONObject = new JSONObject();
		boolean saveSearch = false;
		String user = null;
		String esPostJSON = "";
		long searchId = -1;
		ESResponse esr = null;
		boolean updated = false;
		boolean inserted = false;
		String responseJSON=null;
		try {
			JSONObject j0 = new JSONObject(json);
			meta = getJSONObjectSafely(j0, META_DATA_KEY, null);
			if (meta != null) {
				// put the request meta data into the response
				responseMeta = new JSONObject(meta.toString(3));
				saveSearch = getBooleanSafely(meta, "saveSearch", false);
				searchId = getLongSafely(meta, "searchId", -1L);
				user = getStringSafely(meta, "user", "");
				log.debug("user from post="+user);
				j0.remove(META_DATA_KEY);
			}else{
				log.debug("no metaData found in request");
			}
			esPostJSON = j0.toString(3);
			esr = esService.postSearch(esPostJSON);
			log.debug("es response status code=" + esr.getResponse().getStatusCodeValue());

			naids = getAllNaIds(esr);

			log.debug("found " + naids.size() + " naIds");
			naids.stream().forEach(naid -> {
				Description description = dbf.getDescriptionByNaid(naid);
				if (description != null) {
					String json2 = convertDescriptionToJSON(description);
					log.error("adding json to array: " + json2);
					try {
						descriptionsArray.put(new JSONObject(json2));
					} catch (Exception e) {
						log.error("Exception caught parsing json: " + e + " json=" + json2);
					}
				}
			});

			
			responseJSONObject.put("results", descriptionsArray);
			responseJSONObject.put(ES_RESPONSE_KEY, new JSONObject(esr.getResponse().getBody()));
			
			log.debug("json before=" + json);


		} catch (Exception e) {
			saveSearch = false;
			log.error("Exception caught: " + e + " the search will not be saved");
			String m = stackTraceToString(e);
			if (json != null) {
				m = m + "\n" + json;

			}
			log.error(stackTraceToString(e));
			response = new ResponseEntity<String>(m, HttpStatus.EXPECTATION_FAILED);
		}
		log.debug("saveSearch="+saveSearch);
		if (saveSearch) {
			search.setId(searchId);
			search.setSearchJSON(esPostJSON);
			search.setSearchCreatedUser(user);
			Search search2=searchTemplate.getById(searchId);
			log.debug("search id="+searchId);
			long searchId2=-1;
			if(search2 !=null){
				searchId2=search2.getId();
				log.debug("db returned search id="+search2.getId());
			}else{
				log.debug("no search found in db for id="+searchId);
			}
			if (searchId2 > 0) {
				// update
				log.debug("updating search with id="+searchId);
				searchTemplate.update(search);
				updated = true;
			} else {
				// insert
				log.debug("creating new search");
				searchTemplate.create(search);
				inserted = true;
			}
		}
		if (responseMeta != null) {
			responseJSONObject.put(META_DATA_KEY, responseMeta);
			responseMeta.put(INSERTED_KEY, inserted);
			responseMeta.put(UPDATED_KEY, updated);
		}
		responseJSON = responseJSONObject.toString(3);
		responseJSON = removeNullValues(responseJSON);
		response = new ResponseEntity<String>(responseJSON, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/v1/api/search/descriptionSearch-test", method = RequestMethod.POST)
	public ResponseEntity<String> postNewSearch(@RequestBody SearchRequest searchRequest) {

		CreateSearchResponse csr = new CreateSearchResponse();
		Search s = new Search();
		try {
			s.setSearchCreatedUser("");
			s.setSearchJSON(searchRequest.getJSON());
			searchTemplate.create(s);
			csr.setSearch(s);
			// JSONObject jsonObj = JSONObject.fromObject(str);
			// try
			// {
			// // Contains the above string
			//
			// ObjectMapper mapper = new ObjectMapper();
			// SearchRequest p = mapper.readValue(jsonObj.toString(), new
			// TypeReference<SearchRequest>()
			// {
			// });
			//
			// }
			// catch (Throwable throwable)
			// {
			// System.out.println(throwable.getMessage());
			// }
		} catch (Exception e) {
			csr.setError(e + " " + e.getMessage());
		}

		ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.CREATED);
		return response;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getContext() {
		return applicationContext;
	}

	private List<Long> getAllNaIds(ESResponse esr) {
		List<Long> naids = new ArrayList<Long>();
		try {
			if (esr != null && esr.getResponse() != null) {
				JSONObject j = new JSONObject(esr.getResponse().getBody());
				JSONArray a = j.getJSONObject("hits").getJSONArray("hits");
				for (int i = 0; i < a.length(); i++) {
					JSONObject hit = a.getJSONObject(i);
					Long lng = null;
					Object o = hit.getJSONObject("fields").getJSONArray("naId").get(0);
					if (o instanceof Integer) {
						int i0 = ((Integer) o).intValue();
						lng = new Long(i0);
					} else {
						lng = (Long) o;
					}
					if (lng != null) {
						naids.add(lng);
					}
				}
			}
		} catch (Exception e) {
			log.error("Exception caught: " + e + " no naids will be returned");
		}
		return naids;
	}

	private List<Long> getFirstNaId(ESResponse esr) {
		List<Long> naids = new ArrayList<Long>();
		try {
			if (esr != null && esr.getResponse() != null) {
				JSONObject j = new JSONObject(esr.getResponse().getBody());

				Long lng = null;

				Object o = getObject(j, "hits/hits/0/fields/naId/0");
				if (o instanceof Integer) {
					int i0 = ((Integer) o).intValue();
					lng = new Long(i0);
				} else {
					lng = (Long) o;
				}

				if (lng != null) {
					naids.add(lng);
				}
			}
		} catch (Exception e) {
			log.error("Exception caught: " + e + " no naids will be returned");
		}
		return naids;
	}
}
