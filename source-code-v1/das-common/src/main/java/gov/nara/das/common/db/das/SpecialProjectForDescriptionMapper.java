package gov.nara.das.common.db.das;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SpecialProjectForDescriptionMapper implements RowMapper<SpecialProjectForDescription >{

	@Override
	public SpecialProjectForDescription mapRow(ResultSet rs, int row) throws SQLException {
		SpecialProjectForDescription s=new SpecialProjectForDescription();
		s.setSpecialPjtForDescId(rs.getLong("special_pjt_for_descid"));
		s.setDescNaid(rs.getLong("desc_naid"));
		s.setAuthListNaid(rs.getLong("auth_list_naid"));
		return s;
	}

}
