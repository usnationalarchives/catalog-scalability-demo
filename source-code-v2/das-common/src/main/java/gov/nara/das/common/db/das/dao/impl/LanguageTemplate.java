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

import gov.nara.das.common.db.das.Language;
import gov.nara.das.common.db.das.LanguageMapper;
import gov.nara.das.common.db.das.SpecialProjectForDescription;
import gov.nara.das.common.db.das.SpecialProjectForDescriptionMapper;
import gov.nara.das.common.db.das.dao.LanguageDAO;
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
public class LanguageTemplate implements LanguageDAO {

	@Value("${db.description.schema}")
	private String descriptionSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	/**
	 * insert the description 
	 * @return the db generated naid
	 */
	public long create(Language o) {

		String sql = "insert into "+descriptionSchema+"."+TABLE_NAME +" (" + FIELD_LIST_FOR_INSERT + ") "
				+ "values (nextval(' "+descriptionSchema+"."+SEQUENCE_NAME+"'),?,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, o.getDescNaId());
				ps.setLong(2,o.getLanguageNaId());
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
	public Language getById(long id) {
		Language spd = null;
		String sql = "SELECT * "
				+ " from  "+descriptionSchema + "." + TABLE_NAME + " WHERE " + PK_FIELD + "=?";
		List<Language> results = jdbcTemplate.query(sql, new LanguageMapper(), new Object[] { id });
		if (!results.isEmpty()) {
			spd = results.get(0);
		}
		return spd;
	}
	@Override
	public List<Language> getByDescriptionNaid(long descNaid) {
		Language fa = null;
		String sql = "SELECT * "
				+ " from  "+descriptionSchema+"."+TABLE_NAME+" WHERE desc_naid=?";
		List<Language> results = jdbcTemplate.query(sql, new LanguageMapper(), new Object[] { descNaid });
		return results;
	}
}
