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
public class GeneralMediaTypeMapper  implements RowMapper<GeneralMediaType >{
	@Override
	public GeneralMediaType mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		GeneralMediaType generalMediaType =new GeneralMediaType();
		generalMediaType.setGeneralMediaTypeId(rs.getLong("general_media_typeid"));
		generalMediaType.setAuthListNaId(rs.getLong("auth_list_naid"));
		generalMediaType.setMediaOccurrenceId(rs.getLong("media_occurrenceid"));
		generalMediaType.setGeneralMediaTypeId(rs.getLong("general_media_typeid"));
		generalMediaType.setAuthListNaId(rs.getLong("auth_list_naid"));
		generalMediaType.setMediaOccurrenceId(rs.getLong("media_occurrenceid"));
		generalMediaType.setGeneralMediaTypeId(rs.getLong("general_media_typeid"));
		generalMediaType.setAuthListNaId(rs.getLong("auth_list_naid"));
		generalMediaType.setMediaOccurrenceId(rs.getLong("media_occurrenceid"));
		generalMediaType.setGeneralMediaTypeId(rs.getLong("general_media_typeid"));
		generalMediaType.setAuthListNaId(rs.getLong("auth_list_naid"));
		generalMediaType.setMediaOccurrenceId(rs.getLong("media_occurrenceid"));
		generalMediaType.setGeneralMediaTypeId(rs.getLong("general_media_typeid"));
		generalMediaType.setAuthListNaId(rs.getLong("auth_list_naid"));
		generalMediaType.setMediaOccurrenceId(rs.getLong("media_occurrenceid"));
		generalMediaType.setGeneralMediaTypeId(rs.getLong("general_media_typeid"));
		generalMediaType.setAuthListNaId(rs.getLong("auth_list_naid"));
		generalMediaType.setMediaOccurrenceId(rs.getLong("media_occurrenceid"));
		return generalMediaType;	
	}
}