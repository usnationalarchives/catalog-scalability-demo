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
public class GeneralRecordTypeMapper  implements RowMapper<GeneralRecordType >{
	@Override
	public GeneralRecordType mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		GeneralRecordType generalRecordType =new GeneralRecordType();
		generalRecordType.setGeneralRecordTypeId(rs.getLong("general_record_typeid"));
		generalRecordType.setDescNaId(rs.getLong("desc_naid"));
		generalRecordType.setAuthListNaId(rs.getLong("auth_list_naid"));
		generalRecordType.setGeneralRecordTypeId(rs.getLong("general_record_typeid"));
		generalRecordType.setDescNaId(rs.getLong("desc_naid"));
		generalRecordType.setAuthListNaId(rs.getLong("auth_list_naid"));
		generalRecordType.setGeneralRecordTypeId(rs.getLong("general_record_typeid"));
		generalRecordType.setDescNaId(rs.getLong("desc_naid"));
		generalRecordType.setAuthListNaId(rs.getLong("auth_list_naid"));
		generalRecordType.setGeneralRecordTypeId(rs.getLong("general_record_typeid"));
		generalRecordType.setDescNaId(rs.getLong("desc_naid"));
		generalRecordType.setAuthListNaId(rs.getLong("auth_list_naid"));
		generalRecordType.setGeneralRecordTypeId(rs.getLong("general_record_typeid"));
		generalRecordType.setDescNaId(rs.getLong("desc_naid"));
		generalRecordType.setAuthListNaId(rs.getLong("auth_list_naid"));
		generalRecordType.setGeneralRecordTypeId(rs.getLong("general_record_typeid"));
		generalRecordType.setDescNaId(rs.getLong("desc_naid"));
		generalRecordType.setAuthListNaId(rs.getLong("auth_list_naid"));
		return generalRecordType;	
	}
}