package gov.nara.das.common.db.das;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class DigitalObjectMapper  implements RowMapper<DigitalObject >{
	@Override
	public DigitalObject mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		DigitalObject digitalObject =new DigitalObject();
		digitalObject.setDigitalObjectId(rs.getLong("digital_objectid"));
		digitalObject.setDescNaId(rs.getLong("desc_naid"));
		digitalObject.setAuthListNaId(rs.getLong("auth_list_naid"));
		digitalObject.setObjectDescription(rs.getString("object_description"));
		digitalObject.setObjectDesignator(rs.getString("object_designator"));
		digitalObject.setLabelFlag(rs.getString("label_flag"));
		digitalObject.setLocateBy(rs.getString("locate_by"));
		digitalObject.setAccessFilename(rs.getString("access_filename"));
		digitalObject.setAccessFileSize(rs.getString("access_file_size"));
		digitalObject.setThumbnailFilename(rs.getString("thumbnail_filename"));
		digitalObject.setThumbnailFileSize(rs.getLong("thumbnail_file_size"));
		digitalObject.setImported(rs.getTimestamp("imported"));
		digitalObject.setStatus(rs.getString("status"));
		digitalObject.setDisplay(rs.getString("display"));
		digitalObject.setInDatabase(rs.getString("in_database"));
		digitalObject.setOriginalProcess(rs.getString("original_process"));
		digitalObject.setScanningColor(rs.getString("scanning_color"));
		digitalObject.setOriginalWidth(rs.getLong("original_width"));
		digitalObject.setScanningDimensions(rs.getLong("scanning_dimensions"));
		digitalObject.setServerName(rs.getString("server_name"));
		digitalObject.setVersion(rs.getLong("version"));
		digitalObject.setScanningProcess(rs.getString("scanning_process"));
		digitalObject.setScanningMedium(rs.getString("scanning_medium"));
		digitalObject.setScanningMediumCategory(rs.getString("scanning_medium_category"));
		digitalObject.setOriginalOrientation(rs.getString("original_orientation"));
		digitalObject.setMasterfileSize(rs.getLong("masterfile_size"));
		digitalObject.setMasterMediaBackup(rs.getString("master_media_backup"));
		digitalObject.setMasterMediaPrimary(rs.getString("master_media_primary"));
		digitalObject.setDigitalObjectTranslation(rs.getString("digital_object_translation"));
		digitalObject.setMasterDerivationFileMedia(rs.getString("master_derivation_file_media"));
		digitalObject.setMasterFilename(rs.getString("master_filename"));
		digitalObject.setDigitalObjectTranscript(rs.getString("digital_object_transcript"));
		digitalObject.setOriginalDimension(rs.getLong("original_dimension"));
		digitalObject.setOriginalHeight(rs.getLong("original_height"));
		digitalObject.setOriginalMedium(rs.getString("original_medium"));
		digitalObject.setBatchNumber(rs.getLong("batch_number"));
		digitalObject.setBatchDate(rs.getTimestamp("batch_date"));
		digitalObject.setOriginalColor(rs.getString("original_color"));
		return digitalObject;	
	}
}