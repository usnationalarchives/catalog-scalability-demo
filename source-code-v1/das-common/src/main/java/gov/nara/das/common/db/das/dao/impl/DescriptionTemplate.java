package gov.nara.das.common.db.das.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

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
import gov.nara.das.common.db.das.dao.DescriptionDAOInterface;

@Repository
public class DescriptionTemplate implements DescriptionDAOInterface {

	@Value("${db.description.schema}")
	private String descriptionSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	/**
	 * insert the description 
	 * @return the db generated naid
	 */
	public Long create(Description desc) {

		String type = desc.getDescType();
		String title = desc.getTitle();
		String dateNote = desc.getDateNote();
		String dataControlGroup = desc.getDataControlGroup();
		PGobject jsonObject = new PGobject();
		jsonObject.setType("json");
		try {
			jsonObject.setValue(dataControlGroup);
		} catch (SQLException e) {
			// TBD logging
		}
		String sql = "insert into "+descriptionSchema+".description (desc_naid, desc_type, title,data_ctl_gp,date_note"
				+ ", job_id, sqs_message_id, chunking_id, message_index, created_date, guid) "
				+ "values (nextval(' "+descriptionSchema+".description_seq'),?,?,?,?,?,?,?,?,current_timestamp,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, type);
				ps.setString(2, title);
				ps.setObject(3, jsonObject);
				ps.setString(4, dateNote);
				//
				ps.setLong(5, desc.getJobId());
				ps.setString(6, desc.getSqsMessageId());
				ps.setString(7, desc.getChunkingId());
				ps.setLong(8, desc.getMessageIndex());
				ps.setString(9, desc.getGUID());
				return ps;
			}
		}, keyHolder);

		Long naid = -1L;
		if (keyHolder.getKeys().size() > 1) {
			naid = (Long) keyHolder.getKeys().get("desc_naid");
		} else {
			naid = keyHolder.getKey().longValue();
		}
		return naid;

	}

	@Override
	public Description getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
