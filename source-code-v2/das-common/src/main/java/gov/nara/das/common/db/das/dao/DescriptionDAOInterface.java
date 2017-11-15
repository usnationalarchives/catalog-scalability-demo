package gov.nara.das.common.db.das.dao;

import gov.nara.das.common.db.das.Description;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public interface DescriptionDAOInterface {

	public static final String FIELD_LIST_FOR_SELECT=
			"job_id, sqs_message_id,chunking_id,message_index"
			+ ",desc_naid,desc_type,to_json(data_ctl_gp) data_ctl_gp"
			+ ",record_gp_no,collection_identifier,parent_naid,other_title"
			+ ",inclusive_start_date_qualifier_naid,inclusive_end_date_qualifier_naid,title"
			+ ",inclusive_start_date,coverage_start_date_qualifier_naid,coverage_end_date_qualifier_naid"
			+ ",inclusive_end_date,coverage_start_date,coverage_end_date,date_note,scope_content_note"
			+ ",staff_note_only,record_status,party_designation_naid,description_author,begin_congress_naid"
			+ ",end_congress_naid,arrangement,function_user,general_note,local_identifier,numbering_note"
			+ ",accession_number_naid,record_ctr_xfer_number_naid,disposition_auth_number_naid"
			+ ",internal_xfer_number_naid,xfer_note,custodial_history_note,online_resource_naid,scale_note"
			+ ",edit_status_naid,sound_type_naid,total_footage,is_av,to_json(ptr_obj_availability_date) ptr_obj_availability_date"
			+ ",ptr_obj_availability_date_qualifier_naid"
			+ ",to_json(production_date) production_date,to_json(copyright_date) copyright_date,to_json(subtitle) subtitle,to_json(release_date) release_date"
			+ ",to_json(broadcast_date) broadcast_date,shotlist,broadcast_date_qualifier_naid,release_date_qualifier_naid"
			+ ",copyright_date_qualifier_naid,production_date_qualifier_naid,is_under_edit"
			+ ",created_date,imported_date,last_changed_date,last_approved_date,last_brought_under_edit_date"
			+ ",to_json(approval_history) approval_history,to_json(change_history) change_history,to_json(brought_under_edit_history) brought_under_edit_history"
			+ ",created_user,imported_user,last_changed_user,last_approved_user"
			+ ",last_brought_under_edit_user";
	
	public static final String RecordGroup="RecordGroup";
	
	public static final String Series="Series";
	
	public Long create(Description description);
	
	public Description getById(long id);
}
