package gov.nara.das.common.db.das.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import javax.print.attribute.standard.RequestingUserName;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import gov.nara.das.common.db.das.AuthorityList;
import gov.nara.das.common.db.das.AuthorityListMapper;
import gov.nara.das.common.db.das.Description;
import gov.nara.das.common.db.das.dao.AuthorityListDAO;
import gov.nara.das.common.db.das.dao.DescriptionDAOInterface;
import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.JobMapper;

@Repository
public class AuthorityListTemplate implements AuthorityListDAO {

	@Value("${db.description.schema}")
	private String descriptionSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	/**
	 * insert the description 
	 * @return the db generated naid
	 */
	public Long create(AuthorityList a) {
//		String sql = "insert into "+descriptionSchema+".description (desc_naid, desc_type, title,data_ctl_gp,date_note"
//				+ ", job_id, sqs_message_id, chunking_id, message_index, created_date, guid) "
//				+ "values (nextval(' "+descriptionSchema+".description_seq'),?,?,?,?,?,?,?,?,current_timestamp,?)";
//
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//		jdbcTemplate.update(new PreparedStatementCreator() {
//			@Override
//			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//				ps.setString(1, type);
//				return ps;
//			}
//		}, keyHolder);
//
//		Long naid = -1L;
//		if (keyHolder.getKeys().size() > 1) {
//			naid = (Long) keyHolder.getKeys().get("desc_naid");
//		} else {
//			naid = keyHolder.getKey().longValue();
//		}
//		return naid;
		return -1L;
	}

	@Override
	public AuthorityList getById(long id) {
		AuthorityList a=null;
		String sql = "SELECT * "
				+ " from  "+descriptionSchema+".authority_list where auth_list_naid=?";
		List<AuthorityList> results = jdbcTemplate.query(sql, new AuthorityListMapper(), new Object[] { id });
		if (!results.isEmpty()) {
			a = results.get(0);
		}
		return a;
	}

}
