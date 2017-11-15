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

import gov.nara.das.common.db.das.VariantControlNumber;
import gov.nara.das.common.db.das.VariantControlNumberMapper;
import gov.nara.das.common.db.das.SpecialProjectForDescription;
import gov.nara.das.common.db.das.SpecialProjectForDescriptionMapper;
import gov.nara.das.common.db.das.dao.VariantControlNumberDAO;
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
public class VariantControlNumberTemplate implements VariantControlNumberDAO {

	@Value("${db.description.schema}")
	private String descriptionSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	/**
	 * insert the description 
	 * @return the db generated naid
	 */
	public long create(VariantControlNumber o) {

		String sql = "insert into "+descriptionSchema+"."+TABLE_NAME +" (" + FIELD_LIST_FOR_INSERT + ") "
				+ "values (nextval(' "+descriptionSchema+"."+SEQUENCE_NAME+"'),?,?,?,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, o.getDescNaId());
				ps.setLong(2,o.getAuthListNaId());
				ps.setString(3, o.getVariantCtlNo());
				ps.setString(4, o.getVariantCtlNoNote());
//setString, desc_naid, auth_list_naid, variant_ctl_no, variant_ctl_no_note";

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
	public VariantControlNumber getById(long id) {
		VariantControlNumber spd = null;
		String sql = "SELECT * "
				+ " from  "+descriptionSchema + "." + TABLE_NAME + " WHERE " + PK_FIELD + "=?";
		List<VariantControlNumber> results = jdbcTemplate.query(sql, new VariantControlNumberMapper(), new Object[] { id });
		if (!results.isEmpty()) {
			spd = results.get(0);
		}
		return spd;
	}
	@Override
	public List<VariantControlNumber> getByDescriptionNaid(long descNaid) {
		VariantControlNumber fa = null;
		String sql = "SELECT * "
				+ " from  "+descriptionSchema+"."+TABLE_NAME+" WHERE desc_naid=?";
		List<VariantControlNumber> results = jdbcTemplate.query(sql, new VariantControlNumberMapper(), new Object[] { descNaid });
		return results;
	}
}
