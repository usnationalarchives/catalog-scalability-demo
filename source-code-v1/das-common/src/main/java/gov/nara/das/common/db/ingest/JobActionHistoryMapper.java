package gov.nara.das.common.db.ingest;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class JobActionHistoryMapper implements RowMapper<JobActionHistory >{

	@Override
	public JobActionHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
		JobActionHistory j=new JobActionHistory();
		j.setId(rs.getInt("id"));
		j.setJobId(rs.getInt("job_id"));
		j.setActionTypeId(rs.getInt("action_type_id"));
		j.setActionTime(rs.getTimestamp("action_time"));
		j.setAction(rs.getString("action"));
		return j;
	}

}
