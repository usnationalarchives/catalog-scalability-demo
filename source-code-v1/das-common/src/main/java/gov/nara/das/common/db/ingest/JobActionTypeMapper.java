package gov.nara.das.common.db.ingest;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class JobActionTypeMapper implements RowMapper<JobActionType >{

	@Override
	public JobActionType mapRow(ResultSet rs, int rowNum) throws SQLException {
		JobActionType j=new JobActionType();
		j.setId(rs.getInt("id"));
		j.setAction(rs.getString("action"));
		return j;
	}

}
