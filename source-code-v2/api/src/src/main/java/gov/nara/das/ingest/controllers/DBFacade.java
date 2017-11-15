package gov.nara.das.ingest.controllers;

import org.slf4j.Logger;
import static gov.nara.das.common.elastic.ElasticSearchUtils.convertToElasticSearchJSON;
import static gov.nara.das.common.db.das.dao.FindingAidDAO.FindingAidType;
import static gov.nara.das.common.db.das.dao.FindingAidDAO.ObjectType;
import static gov.nara.das.common.db.das.dao.FindingAidDAO.FindingAidURL;
import static gov.nara.das.common.utils.Utils.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
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

@Component
public class DBFacade {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private String error = null;
	private boolean failed = false;
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

	public Description createDescription(String xml) {
		failed = false;
		Description description = new Description();
		try {
			log.debug("createDescription xml=" + xml);

			JAXBContext jc = JAXBContext.newInstance(Import.class.getPackage().getName());

			Unmarshaller u = jc.createUnmarshaller();
			Import imp = (Import) u.unmarshal(new StreamSource(new ByteArrayInputStream(xml.getBytes())));

			if (imp.getRecordGroupArray() != null && imp.getRecordGroupArray().getRecordGroup() != null
					&& imp.getRecordGroupArray().getRecordGroup().size() > 0) {

				createRecordGroup(imp, xml, description);
			} else if (imp.getSeriesArray() != null && imp.getSeriesArray().getSeries() != null
					&& imp.getSeriesArray().getSeries().size() > 0) {
				createSeries(imp, xml, description);
			}

		} catch (Exception e) {
			log.debug(e.getMessage());
			log.debug(stackTraceToString(e));
			e.printStackTrace(System.err);
			System.err.println(e);
			description = null;
			failed = true;
			error = "Caught Exception=" + e + " , " + e.getMessage();
		}
		return description;
	}

	public Description createRecordGroup(Import imp, String xml, Description description) {
		//
		description.setDescType(DescriptionDAOInterface.RecordGroup);
		//
		RecordGroup rg = imp.getRecordGroupArray().getRecordGroup().get(0);
		JSONObject jo = XML.toJSONObject(xml);
		if (jo.getJSONObject("import").getJSONObject("recordGroupArray").getJSONObject("recordGroup")
				.getJSONObject("dataControlGroup") != null) {
			String dcg = jo.getJSONObject("import").getJSONObject("recordGroupArray").getJSONObject("recordGroup")
					.getJSONObject("dataControlGroup").toString(3);
			description.setDataCtlGp(dcg);
		}
		log.debug("xml recordGroupNumber="+rg.getRecordGroupNumber());
		description.setRecordGpNo(rg.getRecordGroupNumber());
		description.setGUID(rg.getGuid());
		description.setTitle(rg.getTitle());
		description.setDateNote(rg.getDateNote());
		if (rg.getSqsMessageId() != null) {
			description.setSqsMessageId(rg.getSqsMessageId());
		}
		if (rg.getChunkingId() != null) {
			description.setChunkingId(rg.getChunkingId());
		}
		if (rg.getMessageIndex() > 0) {
			description.setMessageIndex(rg.getMessageIndex());
		}
		if (rg.getJobId() > 0) {
			description.setJobId(rg.getJobId());
		}
		// check for begin congress
		log.debug("begin congress0");
		if (rg.getBeginCongress() != null && rg.getBeginCongress().getTermName() != null) {
			AuthorityList a = authListTemplate.getOneByTermNameAndType(rg.getBeginCongress().getTermName(),
					AuthorityListDAO.BeginCongress);
			log.debug("begin congress1," + a);

			if (a != null) {
				log.debug("begin congress2," + a.getAuthListNaId());
				description.setBeginCongressNaId(a.getAuthListNaId());
				description.setBeginCongressAuthorityList(a);
			}
		}
		if (rg.getEndCongress() != null && rg.getEndCongress().getTermName() != null) {
			AuthorityList a = authListTemplate.getOneByTermNameAndType(rg.getEndCongress().getTermName(),
					AuthorityListDAO.EndCongress);
			if (a != null) {
				description.setEndCongressNaId(a.getAuthListNaId());
				description.setEndCongressAuthorityList(a);
			}
		}
		//

		// insert record into database
		long createdNaid = descTemplate.create(description);
		//
		log.debug("naid=" + createdNaid);
		System.err.println("createNewRecordGroup: naid=" + createdNaid);
		description.setDescNaId(createdNaid);
		//
		if (rg.getSpecialProjectArray() != null && rg.getSpecialProjectArray().getSpecialProject() != null) {
			List<SpecialProject> list = rg.getSpecialProjectArray().getSpecialProject();
			for (SpecialProject sp : list) {
				String t = sp.getTermName();
				log.debug("special project termName=" + t);
				AuthorityList a = authListTemplate.getOneByTermNameAndType(t, AuthorityListDAO.SpecialProject);
				log.debug("special project authority list=" + a);
				if (a != null) {
					SpecialProjectForDescription spd = new SpecialProjectForDescription();
					spd.setAuthListNaid(a.getAuthListNaId());
					spd.setDescNaid(createdNaid);
					spdTemplate.create(spd);
					description.getSpecialProjectAuthorityList().add(a);
					log.debug("created SpecialProjectForDescription: " + spd.getSpecialPjtForDescId());
				}
			}
		}
		//
		if (rg.getFindingAidArray() != null && rg.getFindingAidArray().getFindingAid() != null) {
			List<gov.nara.das.common.db.das.jaxb.Import.RecordGroupArray.RecordGroup.FindingAidArray.FindingAid> list = rg
					.getFindingAidArray().getFindingAid();
			for (gov.nara.das.common.db.das.jaxb.Import.RecordGroupArray.RecordGroup.FindingAidArray.FindingAid fa : list) {
				Import.RecordGroupArray.RecordGroup.FindingAidArray.FindingAid.Type item = fa.getType();

				if (fa.getFileType() != null) {
					String termName = "" + fa.getFileType().getTermName();
					termName = termName.trim();
					log.debug("calling getOneByTermNameAndType for termName=" + termName + ", type="
							+ FindingAidDAO.ObjectType);
					AuthorityList a = authListTemplate.getOneByTermNameAndType(termName, FindingAidDAO.ObjectType);
					if (a != null) {
						FindingAid f = new FindingAid();
						f.setDescNaId(createdNaid);
						f.setObjectTypeNaId(a.getAuthListNaId());
						log.debug("create findingAid");
						findingAidTemplate.create(f);
						log.debug("created findingAid");
						description.getFindingAidAuthorityList().add(a);
						/**
						 * description.getFindingAidList().add(f);
						 */
					}
				}
				if (fa.getType() != null) {
					String termName = "" + fa.getType().getTermName();
					termName = termName.trim();
					log.debug("calling getOneByTermNameAndType for termName=" + termName + ", type="
							+ FindingAidDAO.FindingAidType);
					AuthorityList a = authListTemplate.getOneByTermNameAndType(termName, FindingAidDAO.FindingAidType);
					if (a != null) {
						FindingAid f = new FindingAid();
						f.setDescNaId(createdNaid);
						f.setObjectTypeNaId(a.getAuthListNaId());
						log.debug("create findingAid");
						findingAidTemplate.create(f);
						log.debug("created findingAid");
						description.getFindingAidAuthorityList().add(a);
						/**
						 * description.getFindingAidList().add(f);
						 */
					}
				}
				if (fa.getUrl() != null) {
					String termName = "" + fa.getUrl().getTermName();
					termName = termName.trim();
					log.debug("calling getOneByTermNameAndType for termName=" + termName + ", type="
							+ FindingAidDAO.FindingAidURL);
					AuthorityList a = authListTemplate.getOneByTermNameAndType(termName, FindingAidDAO.FindingAidURL);
					if (a != null) {
						FindingAid f = new FindingAid();
						f.setDescNaId(createdNaid);
						f.setObjectTypeNaId(a.getAuthListNaId());
						log.debug("create findingAid");
						findingAidTemplate.create(f);
						log.debug("created findingAid");
						description.getFindingAidAuthorityList().add(a);
						/**
						 * description.getFindingAidList().add(f);
						 */
					}
				}
			}
		}
		//
		return description;
	}

	public Description createSeries(Import imp, String xml, Description description) {
		//
		description.setDescType(DescriptionDAOInterface.Series);
		//
		Import.SeriesArray sa = imp.getSeriesArray();
		if (sa == null || sa.getSeries() == null || sa.getSeries().size() == 0) {
			return null;
		}
		//
		//
		Series s = sa.getSeries().get(0);
		if(s.getInclusiveDates()!=null && s.getInclusiveDates().getInclusiveStartDate() !=null){
			short year=s.getInclusiveDates().getInclusiveStartDate().getYear();
			String logicalDate=year+"-01-01 00:00:00";
			JSONObject j=new JSONObject();
			j.put("year", ""+year);
			j.put("logicalDate", ""+logicalDate);
			description.setInclusiveStartDate(j.toString(3));
		}
		if(s.getInclusiveDates()!=null && s.getInclusiveDates().getInclusiveEndDate() !=null){
			short year=s.getInclusiveDates().getInclusiveEndDate().getYear();
			String logicalDate=year+"-12-31 23:59:59";
			JSONObject j=new JSONObject();
			j.put("year", ""+year);
			j.put("logicalDate", ""+logicalDate);
			description.setInclusiveEndDate(j.toString(3));
		}
		if(s.getCoverageDates()!=null && s.getCoverageDates().getCoverageStartDate()!=null){
			short year=s.getCoverageDates().getCoverageStartDate().getYear();
			String logicalDate=year+"-01-01 00:00:00";
			JSONObject j=new JSONObject();
			j.put("year", ""+year);
			j.put("logicalDate", ""+logicalDate);
			description.setCoverageStartDate(j.toString(3));
		}
		if(s.getCoverageDates()!=null && s.getCoverageDates().getCoverageEndDate()!=null){
			short year=s.getCoverageDates().getCoverageEndDate().getYear();
			String logicalDate=year+"-12-31 23:59:59";
			JSONObject j=new JSONObject();
			j.put("year", ""+year);
			j.put("logicalDate", ""+logicalDate);
			description.setCoverageEndDate(j.toString(3));
		}		
		if (s.getParentRecordGroup() != null) {
			long parentNaId = s.getParentRecordGroup().getNaId();
			description.setParentNaId(parentNaId);
			Description parent = descTemplate.getById(parentNaId);
			log.debug("parentNaId from xml=" + parentNaId);
			if (parent != null) {
				log.debug("parentNaId from db=" + parent.getDescNaId());
				description.setParentRecordGroup(parent);
			}
		}
		if (s.getInclusiveDates() != null && s.getInclusiveDates().getInclusiveStartDate() != null) {
			// description.setIn
		}
		description.setArrangement(s.getArrangement());
		description.setJobId(s.getJobId());
		description.setGUID(s.getGuid());
		description.setSqsMessageId(s.getSqsMessageId());
		description.setChunkingId(s.getChunkingId());
		description.setMessageIndex(s.getMessageIndex());
		//
		description.setTitle(s.getTitle());

		// set other 'primitives' at the root level of both series and
		// description
		description.setArrangement(s.getArrangement());
		description.setCustodialHistoryNote(s.getCustodialHistoryNote());
		description.setNumberingNote(s.getNumberingNote());
		description.setScaleNote(s.getScaleNote());
		description.setStaffOnlyNote(s.getStaffOnlyNote());
		//
		JSONObject jo = XML.toJSONObject(xml);
		if (jo.getJSONObject("import").getJSONObject("seriesArray").getJSONObject("series")
				.getJSONObject("dataControlGroup") != null) {
			String dcg = jo.getJSONObject("import").getJSONObject("seriesArray").getJSONObject("series")
					.getJSONObject("dataControlGroup").toString(3);
			description.setDataCtlGp(dcg);

		}

		//
		// check for begin congress
		log.debug("begin congress0");
		if (s.getBeginCongress() != null && s.getBeginCongress().getTermName() != null) {
			AuthorityList a = authListTemplate.getOneByTermNameAndType(s.getBeginCongress().getTermName(),
					AuthorityListDAO.BeginCongress);
			log.debug("begin congress1," + a);

			if (a != null) {
				log.debug("begin congress2," + a.getAuthListNaId());
				description.setBeginCongressNaId(a.getAuthListNaId());
				description.setBeginCongressAuthorityList(a);
			}
		}
		if (s.getEndCongress() != null && s.getEndCongress().getTermName() != null) {
			AuthorityList a = authListTemplate.getOneByTermNameAndType(s.getEndCongress().getTermName(),
					AuthorityListDAO.EndCongress);
			if (a != null) {
				description.setEndCongressNaId(a.getAuthListNaId());
				description.setEndCongressAuthorityList(a);
			}
		}
		//

		// insert record into database
		long createdNaid = descTemplate.create(description);
		//
		log.debug("naid=" + createdNaid);
		System.err.println("createNewRecordGroup: naid=" + createdNaid);
		description.setDescNaId(createdNaid);
		//
		if (s.getAccessRestriction() != null && s.getAccessRestriction().getStatus() != null
				&& s.getAccessRestriction().getStatus().getTermName() != null) {
			String termName = s.getAccessRestriction().getStatus().getTermName();
			AuthorityList a = authListTemplate.getOneByTermNameAndType(termName,
					AuthorityListDAO.AccessRestrictionStatus);
			if (a != null) {
				description.setAccessRestriction(a);
				AccessRestriction ar = new AccessRestriction();
				ar.setDescNaId(createdNaid);
				ar.setAuthListNaId(a.getAuthListNaId());
				accessRestrictionTemplate.create(ar);
			}
		}

		//
		if (s.getSpecialProjectArray() != null && s.getSpecialProjectArray().getSpecialProject() != null) {
			List<gov.nara.das.common.db.das.jaxb.Import.SeriesArray.Series.SpecialProjectArray.SpecialProject> list = s
					.getSpecialProjectArray().getSpecialProject();
			for (gov.nara.das.common.db.das.jaxb.Import.SeriesArray.Series.SpecialProjectArray.SpecialProject sp : list) {
				String t = sp.getTermName();
				log.debug("special project termName=" + t);
				AuthorityList a = authListTemplate.getOneByTermNameAndType(t, AuthorityListDAO.SpecialProject);
				log.debug("special project authority list=" + a);
				if (a != null) {
					SpecialProjectForDescription spd = new SpecialProjectForDescription();
					spd.setAuthListNaid(a.getAuthListNaId());
					spd.setDescNaid(createdNaid);
					spdTemplate.create(spd);
					description.getSpecialProjectAuthorityList().add(a);
					log.debug("created SpecialProjectForDescription: " + spd.getSpecialPjtForDescId());
				}
			}
		}
		//
		if (s.getDigitalObjectArray() != null && s.getDigitalObjectArray().getDigitalObject() != null) {
			List<gov.nara.das.common.db.das.jaxb.Import.SeriesArray.Series.DigitalObjectArray.DigitalObject> list = s
					.getDigitalObjectArray().getDigitalObject();
			for (gov.nara.das.common.db.das.jaxb.Import.SeriesArray.Series.DigitalObjectArray.DigitalObject digitalObject : list) {
				String termName = digitalObject.getObjectType().getTermName();
				log.debug("special project termName=" + termName);
				AuthorityList a = authListTemplate.getOneByTermNameAndType(termName, AuthorityListDAO.ObjectType);
				log.debug("digital object authority list=" + a);
				if (a != null) {
					DigitalObject dbDigitalObject = new DigitalObject();
					dbDigitalObject.setAuthListNaId(a.getAuthListNaId());
					dbDigitalObject.setDescNaId(createdNaid);
					dbDigitalObject.setAccessFilename(digitalObject.getAccessFilename());
					// dbDigitalObject.setAccessFileSize(""+digitalObject.getAccessFileSize());
					// dbDigitalObject.setBatchNumber(digitalObject.getBatchNumber());
					// dbDigitalObject.setDigitalObjectTranscript(digitalObject.get);
					// dbDigitalObject.setDigitalObjectTranslation(adigitalObjectTranslation);
					// dbDigitalObject.setDisplay(adisplay);
					// dbDigitalObject.setImported(aimported);
					// dbDigitalObject.setInDatabase(ainDatabase);
					dbDigitalObject.setLabelFlag(digitalObject.getLabelFlag());
					// dbDigitalObject.setLocateBy(alocateBy);
					// dbDigitalObject.setMasterDerivationFileMedia(amasterDerivationFileMedia);
					// dbDigitalObject.setMasterFilename(amasterFilename);
					// dbDigitalObject.setMasterFilename(amasterFilename);
					// dbDigitalObject.setMasterfileSize(amasterfileSize);
					// dbDigitalObject.setMasterMediaBackup(amasterMediaBackup);
					// dbDigitalObject.setMasterMediaPrimary(amasterMediaPrimary);
					dbDigitalObject.setObjectDescription(digitalObject.getObjectDescription());
					// dbDigitalObject.setObjectDesignator(aobjectDesignator);
					// dbDigitalObject.setOriginalColor(aoriginalColor);
					// dbDigitalObject.setOriginalDimension(aoriginalDimension);
					// dbDigitalObject.setOriginalHeight(aoriginalHeight);
					// dbDigitalObject.setOriginalMedium(aoriginalMedium);
					// dbDigitalObject.setOriginalOrientation(aoriginalOrientation);
					// dbDigitalObject.setOriginalProcess(aoriginalProcess);
					// dbDigitalObject.setOriginalWidth(aoriginalWidth);
					// dbDigitalObject.setProjectID(aprojectID);
					// dbDigitalObject.setScanningColor(ascanningColor);
					// dbDigitalObject.setScanningDimensions(ascanningDimensions);
					// dbDigitalObject.setScanningMedium(ascanningMedium);
					// dbDigitalObject.setScanningMediumCategory(ascanningMediumCategory);
					// dbDigitalObject.setScanningProcess(ascanningProcess);
					// dbDigitalObject.setServerName(aserverName);
					// dbDigitalObject.setStatus(astatus);
					dbDigitalObject.setThumbnailFilename(digitalObject.getThumbnailFilename());
					dbDigitalObject.setThumbnailFileSize(digitalObject.getThumbnailFileSize());
					// dbDigitalObject.setVersion(digitalObject.get);

					digitalObjectTemplate.create(dbDigitalObject);
					description.getDigitalObjectAuthorityList().add(a);
					/**
					 * description.getDigitalObjectList().add(dbDigitalObject);
					 */
					log.debug("created DigitalObject with id=" + dbDigitalObject.getDigitalObjectId());
				}
			}
		}
		//
		return description;
	}

	public Description getDescriptionByNaid(long naid) {
		Description d = descTemplate.getById(naid);
		if (d != null) {
			if (d.getBeginCongressNaId() > 0) {
				AuthorityList a = authListTemplate.getById(d.getBeginCongressNaId());
				d.setBeginCongressAuthorityList(a);
			}
			if (d.getEndCongressNaId() > 0) {
				AuthorityList a = authListTemplate.getById(d.getEndCongressNaId());
				d.setEndCongressAuthorityList(a);
			}
			//
			if (d != null) {
				if (d.getBeginCongressNaId() > 0) {
					AuthorityList a = new AuthorityList(AuthorityListDAO.BeginCongress, d.getBeginCongressNaId());
					d.setBeginCongressAuthorityList(a);
				}
				if (d.getEndCongressNaId() > 0) {
					AuthorityList a = new AuthorityList(AuthorityListDAO.EndCongress, d.getEndCongressNaId());
					d.setEndCongressAuthorityList(a);
				}
			}
			//
			List<SpecialProjectForDescription> spds = spdTemplate.getByDescriptionNaid(naid);
			log.debug("spd list for desc=" + spds);
			if (spds != null) {
				for (SpecialProjectForDescription spd : spds) {
					log.debug("spd for desc=" + spd);
					AuthorityList a = authListTemplate.getById(spd.getAuthListNaid());
					log.debug("authority list for desc=" + a);
					if (a != null) {
						d.getSpecialProjectAuthorityList().add(a);
					}
				}
			}
			//

			//
			List<FindingAid> fas = findingAidTemplate.getByDescriptionNaid(naid);
			log.debug("spd list for desc=" + spds);
			if (fas != null) {
				for (FindingAid fa : fas) {
					log.debug("fa for desc=" + fa);
					if (fa.getObjectTypeNaId() > 0) {
						AuthorityList a = authListTemplate.getById(fa.getObjectTypeNaId());
						d.getFindingAidAuthorityList().add(a);

					} else if (fa.getFindingAidTypeNaId() > 0) {
						AuthorityList a = authListTemplate.getById(fa.getFindingAidTypeNaId());
						d.getFindingAidAuthorityList().add(a);
					} else if (fa.getFindingAidUrlNaId() > 0) {
						AuthorityList a = authListTemplate.getById(fa.getFindingAidUrlNaId());
						d.getFindingAidAuthorityList().add(a);
					}
				}
			}
			if (DescriptionDAOInterface.Series.equals(d.getDescType())) {
				populateSeries(d);
			}
		}

		return d;
	}

	private void populateSeries(Description d) {
		//
		if (d.getParentNaId() > 0) {
			Description parentRecordGroup = descTemplate.getById(d.getParentNaId());
			d.setParentRecordGroup(parentRecordGroup);
		}
		List<DigitalObject> dos = digitalObjectTemplate.getByDescriptionNaid(d.getDescNaId());
		log.debug("digital object list for desc=" + dos);
		if (dos != null) {
			for (DigitalObject digitalObject : dos) {
				log.debug("spd for desc=" + digitalObject);
				AuthorityList a = authListTemplate.getById(digitalObject.getAuthListNaId());
				log.debug("authority list for desc=" + a);
				if (a != null) {
					d.getSpecialProjectAuthorityList().add(a);
				}
				/**
				 * d.getDigitalObjectList().add(digitalObject);
				 */
			}
		}
		List<AccessRestriction> list = accessRestrictionTemplate.getByDescriptionNaid(d.getDescNaId());
		if (list.size() > 0) {
			long authListNaid = list.get(0).getAuthListNaId();
			AuthorityList a = authListTemplate.getById(authListNaid);
			d.setAccessRestriction(a);
		}
		//
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
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
	 * @param failed
	 *            the failed to set
	 */
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
}