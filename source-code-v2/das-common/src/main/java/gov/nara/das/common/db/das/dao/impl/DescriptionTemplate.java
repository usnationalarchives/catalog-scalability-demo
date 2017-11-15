package gov.nara.das.common.db.das.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import javax.print.attribute.standard.RequestingUserName;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.postgresql.PGConnection;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import gov.nara.das.common.db.das.Description;
import gov.nara.das.common.db.das.DescriptionMapper;
import gov.nara.das.common.db.das.dao.DescriptionDAOInterface;
import static gov.nara.das.common.utils.JSONUtils.*;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
@Repository
public class DescriptionTemplate implements DescriptionDAOInterface {

	@Value("${db.description.schema}")
	private String descriptionSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	/**
	 * insert the description 
	 * @return the db generated naid
	 */
	public Long create(Description desc) {

		String type = desc.getDescType();
		String title = desc.getTitle();
		String dateNote = desc.getDateNote();
		String dataControlGroup = desc.getDataCtlGp();
		//
		String dataCtlGp=desc.getDataCtlGp();
		long recordGroupNumber=desc.getRecordGpNo();
		String inclusiveStartDate=desc.getInclusiveStartDate();
		String inclusiveEndDate=desc.getInclusiveEndDate();
		String coverageStartDate=desc.getCoverageStartDate();
		String coverageEndDate=desc.getCoverageEndDate();
		String productionDate=desc.getProductionDate();
		String copyrightDate=desc.getCopyrightDate();
		String releaseDate=desc.getReleaseDate();
		String broadcastDate=desc.getBroadcastDate();
		String ptrObjAvailabilityDate=desc.getPtrObjAvailabilityDate();
		String descriptionAuthor=desc.getDescriptionAuthor();
		String approvalHistory=desc.getApprovalHistory();
		String changeHistory=desc.getChangeHistory();
		String broughtUnderEditHistory=desc.getBroughtUnderEditHistory();
		boolean recordStatus=desc.getRecordStatus();
		long inclusiveStartDateQualifierNaId=desc.getInclusiveStartDateQualifierNaId();
		long inclusiveEndDateQualifierNaId=desc.getInclusiveEndDateQualifierNaId();
		long coverageStartDateQualifierNaId=desc.getCoverageStartDateQualifierNaId();
		long coverageEndDateQualifierNaId=desc.getCoverageEndDateQualifierNaId();
		long broadcastDateQualifierNaId=desc.getBroadcastDateQualifierNaId();
		long releaseDateQualifierNaId=desc.getReleaseDateQualifierNaId();
		long copyrightDateQualifierNaId=desc.getCopyrightDateQualifierNaId();
		long productionDateQualifierNaId=desc.getProductionDateQualifierNaId();
		long ptrObjAvailabilityDateQualifierNaId=desc.getPtrObjAvailabilityDateQualifierNaId();

		PGobject jsonObject = new PGobject();
		jsonObject.setType("json");
		try {
			jsonObject.setValue(dataControlGroup);
		} catch (SQLException e) {
			// TBD logging
		}

		String sql = "insert into "+descriptionSchema+".description ("
				+ "desc_naid"
				+ ", created_date"
				+ ", desc_type"
				+ ", title"
				+ ",data_ctl_gp"
				+ ",date_note"
				+ ", job_id"
				+ ", sqs_message_id"
				+ ", chunking_id"
				+ ", message_index"
				+ ", guid"
				+ ", parent_naid"
				+ ", begin_congress_naid"
				+ ", end_congress_naid"
				+ ", inclusive_start_date"
				+ ", inclusive_end_date"
				+ ", coverage_start_date"
				+ ", coverage_end_date"
				+ ", production_date"
				+ ", copyright_date"
				+ ", release_date"
				+ ", broadcast_date"
				+ ", ptr_obj_availability_date"
				+ ", record_gp_no"
				+ ", record_status"
				+ ", inclusive_start_date_qualifier_naid "
				+ ", inclusive_end_date_qualifier_naid "
				+ ", coverage_start_date_qualifier_naid "
				+ ", coverage_end_date_qualifier_naid "
				+ ", broadcast_date_qualifier_naid "
				+ ", release_date_qualifier_naid "
				+ ", copyright_date_qualifier_naid "
				+ ",  production_date_qualifier_naid "
				+ ",  ptr_obj_availability_date_qualifier_naid"
				+ ") "
				+ "values (nextval(' "+descriptionSchema+".description_seq'),current_timestamp"
						+ ",?,?,?,?,?,?,?,?,?,?"
						+ ",?,?,?,?,?,?,?,?,?,?"
						+ ",?,?,?,?,?,?,?,?,?,?,?,?"
						+ ")";		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, type);
				ps.setString(2, title);
				ps.setObject(3, jsonObject);
				ps.setString(4, dateNote);
				//
				ps.setLong(5, desc.getJobId());
				ps.setString(6, desc.getSqsMessageId());
				ps.setString(7, desc.getChunkingId());
				ps.setLong(8, desc.getMessageIndex());
				ps.setString(9, desc.getGUID());
				ps.setLong(10, desc.getParentNaId());
				if(desc.getBeginCongressNaId() >0){
					ps.setLong(11,desc.getBeginCongressNaId());
				}else{
					ps.setLong(11,Types.NULL);
				}
				if(desc.getEndCongressNaId() >0){
					ps.setLong(12,desc.getEndCongressNaId());
				}else{
					ps.setLong(12,Types.NULL);
				}
				
				jsonObject.setValue(createValidJSONObjectStringSafely(inclusiveStartDate));
				ps.setObject(13, jsonObject);
				jsonObject.setValue(createValidJSONObjectStringSafely(inclusiveEndDate));
				ps.setObject(14, jsonObject);
				jsonObject.setValue(createValidJSONObjectStringSafely(coverageStartDate));
				ps.setObject(15, jsonObject);
				jsonObject.setValue(createValidJSONObjectStringSafely(coverageEndDate));
				ps.setObject(16, jsonObject);
				jsonObject.setValue(createValidJSONObjectStringSafely(productionDate));
				ps.setObject(17, jsonObject);
				jsonObject.setValue(createValidJSONObjectStringSafely(copyrightDate));
				ps.setObject(18, jsonObject);
				jsonObject.setValue(createValidJSONObjectStringSafely(releaseDate));
				ps.setObject(19, jsonObject);
				jsonObject.setValue(createValidJSONObjectStringSafely(broadcastDate));
				ps.setObject(20, jsonObject);
				jsonObject.setValue(createValidJSONObjectStringSafely(ptrObjAvailabilityDate));
				ps.setObject(21, jsonObject);
				ps.setLong(22, recordGroupNumber);
				ps.setBoolean(23, recordStatus);
				ps.setLong(24, inclusiveStartDateQualifierNaId);
				ps.setLong(25, inclusiveEndDateQualifierNaId);
				ps.setLong(26, coverageStartDateQualifierNaId);
				ps.setLong(27, coverageEndDateQualifierNaId);
				ps.setLong(28, broadcastDateQualifierNaId);
				ps.setLong(29, releaseDateQualifierNaId);
				ps.setLong(30, copyrightDateQualifierNaId);
				ps.setLong(31, productionDateQualifierNaId);
				ps.setLong(32, ptrObjAvailabilityDateQualifierNaId);
				
				return ps;
			}
		}, keyHolder);

		Long naid = -1L;
		if (keyHolder.getKeys().size() > 1) {
			naid = (Long) keyHolder.getKeys().get("desc_naid");
		} else {
			naid = keyHolder.getKey().longValue();
		}
		return naid;

	}

	@Override
	public Description getById(long naid) {
		Description d = null;
		String sql = "SELECT "+FIELD_LIST_FOR_SELECT+" FROM "+ descriptionSchema+".description  WHERE  desc_naid=? ";
		List<Description> results = jdbcTemplate.query(sql, new DescriptionMapper(), new Object[] { naid });
		if (!results.isEmpty()) {
			d = results.get(0);
		}
		return d;
	}

}
