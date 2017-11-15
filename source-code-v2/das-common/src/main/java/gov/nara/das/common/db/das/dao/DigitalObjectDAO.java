package gov.nara.das.common.db.das.dao;

import java.util.List;

import gov.nara.das.common.db.das.DigitalObject;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public interface DigitalObjectDAO {

	public final String TABLE_NAME="digital_object";
	public final String PK_FIELD="digital_objectid";
	public final String SEQUENCE_NAME="digital_object_digital_objectid_seq";
	public final String FIELD_LIST_FOR_INSERT="digital_objectid,desc_naid,auth_list_naid,object_description"
			+ ",object_designator,label_flag,locate_by,access_filename,access_file_size"
			+ ",thumbnail_filename,thumbnail_file_size,imported,status,display,in_database"
			+ ",original_process,scanning_color,original_width,scanning_dimensions,server_name"
			+ ",version,scanning_process,scanning_medium,scanning_medium_category,original_orientation"
			+ ",masterfile_size,master_media_backup,master_media_primary,digital_object_translation"
			+ ",master_derivation_file_media,master_filename,digital_object_transcript"
			+ ",original_dimension,original_height,original_medium,batch_number,batch_date,original_color";

	public DigitalObject getById(long id);
	
	public long create(DigitalObject vcn);
	
	public List<?> getByDescriptionNaid(long descNaid);
	
}
