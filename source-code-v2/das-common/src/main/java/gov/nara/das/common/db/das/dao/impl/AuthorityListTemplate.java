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
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
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
		throw new RuntimeException("not implemented");
	}

	@Override
	public AuthorityList getById(long id) {
		AuthorityList a=null;
		String sql = "SELECT  "+SELECT_FIELD_CLAUSE
				+ " FROM  "+descriptionSchema+".authority_list where auth_list_naid=?";
		List<AuthorityList> results = jdbcTemplate.query(sql, new AuthorityListMapper(), new Object[] { id });
		if (!results.isEmpty()) {
			a = results.get(0);
		}
		return a;
	}
	@Override
	public AuthorityList getOneByTermNameAndType(String termName, String type) {
		AuthorityList a=null;
		String sql = "SELECT "+SELECT_FIELD_CLAUSE
				+ " FROM  "+descriptionSchema+".authority_list where term_name=? AND auth_type=? LIMIT 1";
		List<AuthorityList> results = jdbcTemplate.query(sql, new AuthorityListMapper(), new Object[] { termName,type });
		if (!results.isEmpty()) {
			a = results.get(0);
		}
		return a;
	}
}
