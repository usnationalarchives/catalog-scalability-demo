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

import gov.nara.das.common.db.das.Description;
import gov.nara.das.common.db.das.SpecialProjectForDescription;
import gov.nara.das.common.db.das.SpecialProjectForDescriptionMapper;
import gov.nara.das.common.db.das.dao.DescriptionDAOInterface;
import gov.nara.das.common.db.das.dao.SpecialProjectForDescriptionDAO;
import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.JobMapper;

@Repository
public class SpecialProjectForDescriptionTemplate implements SpecialProjectForDescriptionDAO {

	@Value("${db.description.schema}")
	private String descriptionSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	/**
	 * insert the description 
	 * @return the db generated naid
	 */
	public Long create(SpecialProjectForDescription spd) {

		String sql = "insert into "+descriptionSchema+".special_pjt_for_desc (special_pjt_for_descid, desc_naid, auth_list_naid) "
				+ "values (nextval(' "+descriptionSchema+".special_pjt_for_desc_special_pjt_for_descid_seq'),?,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, spd.getDescNaid());
				ps.setLong(2, spd.getAuthListNaid());
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
	public SpecialProjectForDescription getById(long id) {
		SpecialProjectForDescription spd = null;
		String sql = "SELECT special_pjt_for_descid, desc_naid, auth_list_naid "
				+ " from  "+descriptionSchema+".ingest_jobs where job_id=?";
		List<SpecialProjectForDescription> results = jdbcTemplate.query(sql, new SpecialProjectForDescriptionMapper(), new Object[] { id });
		if (!results.isEmpty()) {
			spd = results.get(0);
		}
		return spd;
	}

}
