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
public class SpecialPjtForAuthMapper  implements RowMapper<SpecialPjtForAuth >{
	@Override
	public SpecialPjtForAuth mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		SpecialPjtForAuth specialPjtForAuth =new SpecialPjtForAuth();
		specialPjtForAuth.setSpecialPjtForAuthId(rs.getLong("special_pjt_for_authid"));
		specialPjtForAuth.setPrimAuthNaId(rs.getLong("prim_auth_naid"));
		specialPjtForAuth.setAuthListNaId(rs.getLong("auth_list_naid"));
		return specialPjtForAuth;	
	}
}