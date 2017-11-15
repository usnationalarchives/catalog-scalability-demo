package gov.nara.das.common.db.ingest;

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
public class JobMapper  implements RowMapper<Job >{
	@Override
	public Job mapRow(ResultSet rs, int rowNum) throws SQLException {
		Job job=new Job();
		job.setJobId(rs.getLong("job_id"));
		job.setRetrievedTime(rs.getTimestamp("retrieved_time"));
		job.setJobCreatedUser(rs.getString("job_created_user"));
		job.setTotalDescriptionCount(rs.getInt("total_description_count"));
		job.setSuccessCount(rs.getLong("success_count"));
		job.setFailureCount(rs.getLong("failure_count"));
		job.setFailed(rs.getBoolean("failed"));
		job.setPassed(rs.getBoolean("passed"));
		job.setJobCreationTime(rs.getTimestamp("job_creation_time"));
		job.setLastUpdateDate(rs.getTimestamp("last_update_date"));
		job.setEpochSecondsRetrieved(rs.getLong("epoch_seconds_retrieved"));
		job.setEpochSecondsCreated(rs.getLong("epoch_seconds_created"));
		return job;
		
	}
}
