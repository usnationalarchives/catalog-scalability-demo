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
public class AuthorityListMapper implements RowMapper<AuthorityList >{

	@Override
	public AuthorityList mapRow(ResultSet rs, int row) throws SQLException {
		AuthorityList a=new AuthorityList();
		a.setAuthListNaId(rs.getLong("auth_list_naid"));
		a.setTermName(rs.getString("term_name"));
		a.setAuthDetails(rs.getString("auth_details"));
		a.setAuthType(rs.getString("auth_type"));
		a.setCreatedDate(rs.getTimestamp("created_date"));
		a.setImportedDate(rs.getTimestamp("imported_date"));
		a.setLastChangedDate(rs.getTimestamp("last_changed_date"));
		a.setLastApprovedDate(rs.getTimestamp("last_approved_date"));
		a.setLastBroughtUnderEdit(rs.getTimestamp("last_brought_under_edit"));
		a.setApprovalHistory(rs.getString("approval_history"));
		a.setChangedHistory(rs.getString("changed_history"));
		a.setBroughtUnderEditHistory(rs.getString("brought_under_edit_history"));
		a.setUnderEdit(rs.getBoolean("is_under_edit"));
		return a;
	}

}
