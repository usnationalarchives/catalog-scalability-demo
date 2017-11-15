package gov.nara.das.common.db.das;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class FindingAidMapper  implements RowMapper<FindingAid >{
	@Override
	public FindingAid mapRow(ResultSet rs, int rowNum) throws SQLException {
		FindingAid findingAid =new FindingAid();
		findingAid.setFindingAidId(rs.getLong("finding_aidid"));
		findingAid.setDescNaId(rs.getLong("desc_naid"));
		findingAid.setFindingAidTypeNaId(rs.getLong("finding_aid_type_naid"));
		findingAid.setFindingAidNote(rs.getString("finding_aid_note"));
		findingAid.setFindingAidSource(rs.getString("finding_aid_source"));
		findingAid.setFindingAidUrlNaId(rs.getLong("finding_aid_url_naid"));
		findingAid.setObjectTypeNaId(rs.getLong("object_type_naid"));
		return findingAid;	
	}
}
