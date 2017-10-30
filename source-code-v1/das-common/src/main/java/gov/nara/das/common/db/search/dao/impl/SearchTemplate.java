package gov.nara.das.common.db.search.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import gov.nara.das.common.db.search.Search;
import gov.nara.das.common.db.search.SearchMapper;
import gov.nara.das.common.db.search.dao.SearchDAO;

@Repository
@ComponentScan("gov.nara.das.common.db")
public class SearchTemplate implements SearchDAO {

	@Value("${db.search.schema}")
	private String searchSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void create(Search search) {
		search.setId(-1);
		String sql = "insert into "+searchSchema+".search (id, search_created_user,search_creation_time) "
				+ " values ( nextval('"+searchSchema+".search_seq'), ? , current_timestamp )";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, search.getSearchCreatedUser());
				return ps;
			}
		}, keyHolder);

		Integer searchId = -1;
		if (keyHolder.getKeys().size() > 1) {
			searchId = (Integer) keyHolder.getKeys().get("id");
		} else {
			searchId = keyHolder.getKey().intValue();
		}
		search.setId(searchId);
	}

	@Override
	public void update(Search search) {
		String user = search.getSearchCreatedUser();
		String sql = "UPDATE  "+searchSchema+".search SET " + " search_created_user=?,  last_update_time=current_timestamp, search_json=?"
				+ "  WHERE id =?";
		jdbcTemplate.update(sql, search.getSearchCreatedUser(),search.getSearchJSON(),search.getId());
		return;

	}

	@Override
	public Search getById(long id) {
		Search search = null;
		String sql = "SELECT from "+searchSchema+".search where id=?";
		List<Search> results = jdbcTemplate.query(sql, new SearchMapper(), new Object[] { id });
		if (!results.isEmpty()) {
			search = results.get(0);
		}
		return search;
	}
}
