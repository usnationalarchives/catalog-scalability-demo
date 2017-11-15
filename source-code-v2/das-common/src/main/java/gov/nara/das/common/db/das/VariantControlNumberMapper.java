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
public class VariantControlNumberMapper  implements RowMapper<VariantControlNumber >{
	@Override
	public VariantControlNumber mapRow(ResultSet rs, int rowNum) throws SQLException {
		VariantControlNumber variantControlNumber =new VariantControlNumber();
		variantControlNumber.setVariantControlNumberId(rs.getLong("variant_control_numberid"));
		variantControlNumber.setDescNaId(rs.getLong("desc_naid"));
		variantControlNumber.setAuthListNaId(rs.getLong("auth_list_naid"));
		variantControlNumber.setVariantCtlNo(rs.getString("variant_ctl_no"));
		variantControlNumber.setVariantCtlNoNote(rs.getString("variant_ctl_no_note"));
		return variantControlNumber;	
	}
}