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
public class OnlineResourceMapper  implements RowMapper<OnlineResource >{
	@Override
	public OnlineResource mapRow(ResultSet rs, int rowNum) throws SQLException {
		OnlineResource onlineResource =new OnlineResource();
		onlineResource.setOnlineResourceID(rs.getLong("online_resourceID"));
		onlineResource.setDescNaId(rs.getLong("desc_naid"));
		onlineResource.setAuthListNaId(rs.getLong("auth_list_naid"));
		return onlineResource;	
	}
}