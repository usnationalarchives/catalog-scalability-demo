package gov.nara.das.common.db.das;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DescriptionMapper implements RowMapper<Description >{

	@Override
	public Description mapRow(ResultSet rs, int row) throws SQLException {
		Description d=new Description();
		d.setDescNaid(rs.getLong("desc_naid"));
		d.setGUID(rs.getString("guid"));
		d.setDescType("desc_type");
		d.setDescTitle("desc_title");
		d.setUnderEdit(rs.getBoolean("is_under_edit"));
		d.setDataControlGroup(rs.getString("data_ctl_gp"));
		d.setRecordGroupNumber(rs.getString("record_gp_no"));
		d.setDateNote(rs.getString("date_note"));
		//

//		d.setdescType(rs.getString("desc_type"));
//		d.setdataCtlGp(rs.getString("data_ctl_gp"));
//		d.setrecordGpNo(rs.getString("record_gp_no"));
		d.setCollectionIdentifier(rs.getString("collection_identifier"));
		d.setParentNaId(rs.getLong("parent_naid"));
		d.setOtherTitle(rs.getString("other_title"));
		d.setInclusiveStartDateQualifierNaId(rs.getLong("inclusive_start_date_qualifier_naid"));
		d.setInclusiveEndDateQualifierNaId(rs.getLong("inclusive_end_date_qualifier_naid"));
//		d.settitle(rs.getString("title"));
		d.setInclusiveStartDate(rs.getTimestamp("inclusive_start_date"));
		d.setBroadcastDateQualifierNaId(rs.getLong("coverage_start_date_qualifier_naid"));
		d.setBroadcastDateQualifierNaId(rs.getLong("coverage_end_date_qualifier_naid"));
		d.setInclusiveEndDate(rs.getTimestamp("inclusive_end_date"));
		d.setCopyrightDate(rs.getTimestamp("coverage_start_date"));
		d.setCoverageEndDate(rs.getTimestamp("coverage_end_date"));
//		d.setdateNote(rs.getString("date_note"));
		d.setScopeContentNote(rs.getString("scope_content_note"));
		d.setScaleNote(rs.getString("staff_note_only"));
		d.setRecordGpNo(rs.getString("record_status"));
		d.setPartyDesignationNaId(rs.getLong("party_designation_naid"));
		d.setDescriptionAuthor(rs.getString("description_author"));
		d.setBeginCongressNaId(rs.getLong("begin_congress_naid"));
		d.setEndCongressNaId(rs.getLong("end_congress_naid"));
		d.setArrangement(rs.getString("arrangement"));
		d.setFunctionUser(rs.getString("function_user"));
		d.setGeneralNote(rs.getString("general_note"));
		d.setLocalIdentifier(rs.getString("local_identifier"));
		d.setNumberingNote(rs.getString("numbering_note"));
		d.setAccessionNumberNaId(rs.getLong("accession_number_naid"));
		d.setRecordCtrXferNumberNaId(rs.getLong("record_ctr_xfer_number_naid"));
		d.setDispositionAuthNumberNaId(rs.getLong("disposition_auth_number_naid"));
		d.setInternalXferNumberNaId(rs.getLong("internal_xfer_number_naid"));
		d.setXferNote(rs.getString("xfer_note"));
		d.setCustodialHistoryNote(rs.getString("custodial_history_note"));
		d.setOnlineResourceNaId(rs.getLong("online_resource_naid"));
		d.setScaleNote(rs.getString("scale_note"));
		d.setEditStatusNaId(rs.getLong("edit_status_naid"));
		d.setSoundTypeNaId(rs.getLong("sound_type_naid"));
		d.setTotalFootage(rs.getString("total_footage"));
		d.setIsAv(rs.getString("is_av"));
		d.setPtrObjAvailabilityDate(rs.getTimestamp("ptr_obj_availability_date"));
		d.setProductionDate(rs.getTimestamp("production_date"));
		d.setCopyrightDate(rs.getTimestamp("copyright_date"));
		d.setSubtitle(rs.getString("subtitle"));
		d.setReleaseDate(rs.getTimestamp("release_date"));
		d.setBroadcastDate(rs.getTimestamp("broadcast_date"));
		d.setShotlist(rs.getString("shotlist"));
		d.setBroadcastDateQualifierNaId(rs.getLong("broadcast_date_qualifier_naid"));
		d.setReleaseDateQualifierNaId(rs.getLong("release_date_qualifier_naid"));
		d.setCopyrightDateQualifierNaId(rs.getLong("copyright_date_qualifier_naid"));
		d.setProductionDateQualifierNaId(rs.getLong("production_date_qualifier_naid"));
		d.setPtrObjAvailabilityDateQualifierNaId(rs.getInt("ptr_obj_availability_date_qualifier_naid"));
//		d.setisUnderEdit(rs.getString("is_under_edit"));
		d.setCreatedDate(rs.getTimestamp("created_date"));
		d.setImportedDate(rs.getTimestamp("imported_date"));
		d.setLastChangedDate(rs.getTimestamp("last_changed_date"));
		d.setLastApprovedDate(rs.getTimestamp("last_approved_date"));
		d.setLastBroughtUnderEditDate(rs.getTimestamp("last_brought_under_edit_date"));
		d.setApprovalHistory(rs.getString("approval_history"));
		d.setChangeHistory(rs.getString("change_history"));
		d.setBroughtUnderEditHistory(rs.getString("brought_under_edit_history"));
		d.setCreatedUser(rs.getString("created_user"));
		d.setImportedUser(rs.getString("imported_user"));
		d.setLastChangedUser(rs.getString("last_changed_user"));
		d.setLastApprovedUser(rs.getString("last_approved_user"));
		d.setLastBroughtUnderEditUser(rs.getString("last_brought_under_edit_user"));
		//
		d.setJobId(rs.getLong("jobId"));
		d.setSqsMessageId(rs.getString("sqs_message_id"));
		d.setChunkingId(rs.getString("chunking_id"));
		d.setMessageIndex(rs.getLong("message_index"));
		return d;
	}

}
