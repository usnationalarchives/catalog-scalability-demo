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
public class AccessRestrictionMapper  implements RowMapper<AccessRestriction >{
	@Override
	public AccessRestriction mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		AccessRestriction accessRestriction =new AccessRestriction();
		accessRestriction.setAccessRestrictionId(rs.getLong("access_restrictionid"));
		accessRestriction.setDescNaId(rs.getLong("desc_naid"));
		accessRestriction.setAuthListNaId(rs.getLong("auth_list_naid"));
		accessRestriction.setAccessRestrictionNote(rs.getString("access_restriction_note"));
		return accessRestriction;	
	}
}