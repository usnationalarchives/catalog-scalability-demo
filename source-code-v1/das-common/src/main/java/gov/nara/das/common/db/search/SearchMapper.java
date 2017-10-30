package gov.nara.das.common.db.search;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SearchMapper  implements RowMapper<Search >{
	@Override
	public Search mapRow(ResultSet rs, int rowNum) throws SQLException {
		Search search=new Search();
		search.setId(rs.getLong("id"));
		search.setSearchCreatedUser(rs.getString("search_created_user"));
		search.setSearchCreationTime(rs.getTimestamp("search_creation_time"));
		search.setLastUpdateTime(rs.getTimestamp("last_update_time"));
		return search;
		
	}
}
