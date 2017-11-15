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

import gov.nara.das.common.db.das.DigitalObject;
import gov.nara.das.common.db.das.DigitalObjectMapper;
import gov.nara.das.common.db.das.SpecialProjectForDescription;
import gov.nara.das.common.db.das.SpecialProjectForDescriptionMapper;
import gov.nara.das.common.db.das.dao.DigitalObjectDAO;
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
public class DigitalObjectTemplate implements DigitalObjectDAO {

	@Value("${db.description.schema}")
	private String descriptionSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	/**
	 * insert the description 
	 * @return the db generated naid
	 */
	public long create(DigitalObject o) {

		String sql = "INSERT INTO "+descriptionSchema+"."+TABLE_NAME +" (" + FIELD_LIST_FOR_INSERT + ") "
				+ "VALUES (nextval(' "+descriptionSchema+"."+SEQUENCE_NAME+"'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
						+ ",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1,o.getDescNaId());
				ps.setLong(2,o.getAuthListNaId());
				ps.setString(3,o.getObjectDescription());
				ps.setString(4,o.getObjectDesignator());
				ps.setString(5,o.getLabelFlag());
				ps.setString(6,o.getLocateBy());
				ps.setString(7,o.getAccessFilename());
				ps.setString(8,o.getAccessFileSize());
				ps.setString(9,o.getThumbnailFilename());
				ps.setLong(10,o.getThumbnailFileSize());
				ps.setTimestamp(11,o.getImported());
				ps.setString(12,o.getStatus());
				ps.setString(13,o.getDisplay());
				ps.setString(14,o.getInDatabase());
				ps.setString(15,o.getOriginalProcess());
				ps.setString(16,o.getScanningColor());
				ps.setLong(17,o.getOriginalWidth());
				ps.setLong(18,o.getScanningDimensions());
				ps.setString(19,o.getServerName());
				ps.setLong(20,o.getVersion());
				ps.setString(21,o.getScanningProcess());
				ps.setString(22,o.getScanningMedium());
				ps.setString(23,o.getScanningMediumCategory());
				ps.setString(24,o.getOriginalOrientation());
				ps.setLong(25,o.getMasterfileSize());
				ps.setString(26,o.getMasterMediaBackup());
				ps.setString(27,o.getMasterMediaPrimary());
				ps.setString(28,o.getDigitalObjectTranslation());
				ps.setString(29,o.getMasterDerivationFileMedia());
				ps.setString(30,o.getMasterFilename());
				ps.setString(31,o.getDigitalObjectTranscript());
				ps.setLong(32,o.getOriginalDimension());
				ps.setLong(33,o.getOriginalHeight());
				ps.setString(34,o.getOriginalMedium());
				ps.setLong(35,o.getBatchNumber());
				ps.setTimestamp(36,o.getBatchDate());
				ps.setString(37,o.getOriginalColor());
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
	public DigitalObject getById(long id) {
		DigitalObject spd = null;
		String sql = "SELECT * "
				+ " from  "+descriptionSchema + "." + TABLE_NAME + " WHERE " + PK_FIELD + "=?";
		List<DigitalObject> results = jdbcTemplate.query(sql, new DigitalObjectMapper(), new Object[] { id });
		if (!results.isEmpty()) {
			spd = results.get(0);
		}
		return spd;
	}
	@Override
	public List<DigitalObject> getByDescriptionNaid(long descNaid) {
		DigitalObject fa = null;
		String sql = "SELECT * "
				+ " from  "+descriptionSchema+"."+TABLE_NAME+" WHERE desc_naid=?";
		List<DigitalObject> results = jdbcTemplate.query(sql, new DigitalObjectMapper(), new Object[] { descNaid });
		return results;
	}
}
