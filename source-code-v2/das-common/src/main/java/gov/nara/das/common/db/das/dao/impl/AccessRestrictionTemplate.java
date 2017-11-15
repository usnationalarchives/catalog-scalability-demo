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

import gov.nara.das.common.db.das.AccessRestriction;
import gov.nara.das.common.db.das.AccessRestrictionMapper;
import gov.nara.das.common.db.das.Description;
import gov.nara.das.common.db.das.AccessRestriction;
import gov.nara.das.common.db.das.AccessRestrictionMapper;
import gov.nara.das.common.db.das.dao.AccessRestrictionDAO;
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
public class AccessRestrictionTemplate implements AccessRestrictionDAO {

	@Value("${db.description.schema}")
	private String descriptionSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	/**
	 * insert the description 
	 * @return the db generated naid
	 */
	public Long create(AccessRestriction a) {

		String sql = "insert into "+descriptionSchema+"."+TABLE +"("+INSERT_FIELD_CLAUSE+") "
				+ " values (nextval(' "+descriptionSchema+"."+SEQUENCE+"'),?,?,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, a.getDescNaId());
				ps.setLong(2, a.getAuthListNaId());
				ps.setString(3, a.getAccessRestrictionNote());
				return ps;
			}
		}, keyHolder);

		Long id = -1L;
		if (keyHolder.getKeys().size() > 1) {
			id = (Long) keyHolder.getKeys().get("special_pjt_for_descid");
		} else {
			id = keyHolder.getKey().longValue();
		}
		return id;

	}

	@Override
	public AccessRestriction getById(long id) {
		AccessRestriction a=null;
		String sql = "SELECT  "+SELECT_FIELD_CLAUSE
				+ " FROM  "+descriptionSchema+"."+TABLE+" WHERE "+PK+"=?";
		List<AccessRestriction> results = jdbcTemplate.query(sql, new AccessRestrictionMapper(), new Object[] { id });
		if (!results.isEmpty()) {
			a = results.get(0);
		}
		return a;
	}
	@Override
	public List<AccessRestriction> getByDescriptionNaid(long descNaid) {
		AccessRestriction fa = null;
		String sql = "SELECT * "
				+ " FROM  "+descriptionSchema+"."+TABLE+" WHERE desc_naid=? LIMIT 1";
		List<AccessRestriction> results = jdbcTemplate.query(sql, new AccessRestrictionMapper(), new Object[] { descNaid });
		return results;
	}
}
