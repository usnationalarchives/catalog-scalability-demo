package gov.nara.das.common.db.das.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import gov.nara.das.common.db.das.FindingAid;
import gov.nara.das.common.db.das.FindingAidMapper;
import gov.nara.das.common.db.das.SpecialProjectForDescription;
import gov.nara.das.common.db.das.SpecialProjectForDescriptionMapper;
import gov.nara.das.common.db.das.dao.FindingAidDAO;
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
public class FindingAidTemplate implements FindingAidDAO {

	@Value("${db.description.schema}")
	private String descriptionSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	/**
	 * insert the description 
	 * @return the db generated naid
	 */
	public long create(FindingAid o) {

		String sql = "insert into "+descriptionSchema+"."+TABLE_NAME +" (" + FIELD_LIST_FOR_INSERT + ") "
				+ "values (nextval(' "+descriptionSchema+"."+SEQUENCE_NAME+"'),?,?,?,?,?,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, o.getDescNaId());
				ps.setString(2,o.getFindingAidNote());
				ps.setString(3, o.getFindingAidSource());
				ps.setLong(4, o.getFindingAidTypeNaId());
				ps.setLong(5, o.getFindingAidUrlNaId());
				ps.setLong(6, o.getObjectTypeNaId());
				return ps;
			}
		}, keyHolder);

		Long id = -1L;
		if (keyHolder.getKeys().size() > 1) {
			id = (Long) keyHolder.getKeys().get(PK_FIELD);
		} else {
			id = keyHolder.getKey().longValue();
		}
		return id;

	}

	@Override
	public FindingAid getById(long id) {
		FindingAid spd = null;
		String sql = "SELECT * "
				+ " from  "+descriptionSchema + "." + TABLE_NAME + " WHERE " + PK_FIELD + "=?";
		List<FindingAid> results = jdbcTemplate.query(sql, new FindingAidMapper(), new Object[] { id });
		if (!results.isEmpty()) {
			spd = results.get(0);
		}
		return spd;
	}
	@Override
	public List<FindingAid> getByDescriptionNaid(long descNaid) {
		FindingAid fa = null;
		String sql = "SELECT * "
				+ " from  "+descriptionSchema+".finding_aid where desc_naid=?";
		List<FindingAid> results = jdbcTemplate.query(sql, new FindingAidMapper(), new Object[] { descNaid });
		return results;
	}
}
