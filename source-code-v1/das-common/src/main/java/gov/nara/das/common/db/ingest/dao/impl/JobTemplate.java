package gov.nara.das.common.db.ingest.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.JobMapper;
import gov.nara.das.common.db.ingest.dao.JobActionTypesDAO;
import gov.nara.das.common.db.ingest.dao.JobDAOInterface;

@Repository
@ComponentScan("gov.nara.das.common.db")
public class JobTemplate implements JobDAOInterface {

	@Value("${db.job.schema}")
	private String jobSchema;

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	JobActionTypeTemplate jobActionTemplate;

	@Override
	public void create(Job job) {
		job.setJobId(-1);
		Long actionId=jobActionTemplate.getIdByValue(JobActionTypesDAO.ActionTypes.CREATED.action);
		String sql = "insert into "+jobSchema+".ingest_jobs (job_created_user, job_id,job_creation_time,current_action_type_id) "
				+ " values (?, nextval(' "+jobSchema+".job_seq'), current_timestamp "
				+ ", ?"
				+ ")";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, job.getJobCreatedUser());
				ps.setLong(2, actionId);
				return ps;
			}
		}, keyHolder);

		Integer jobId = -1;
		if (keyHolder.getKeys().size() > 1) {
			jobId = (Integer) keyHolder.getKeys().get("job_id");
		} else {
			jobId = keyHolder.getKey().intValue();
		}
		job.setJobId(jobId);
	}

	@Override
	public void update(Job job) {
		String user = job.getJobCreatedUser();
		String sql = "UPDATE  "+jobSchema+".ingest_jobs SET " + " total_description_count=?" + ", success_count=? "
				+ ", failure_count=?" + ", passed=?" + ", failed=?" + ", failure_type_id=?" + ", chunking_error_id=?"
				+ ", last_chunked_guid=?" + ", last_update_date=current_timestamp" + " WHERE job_id =?";
		jdbcTemplate.update(sql, job.getTotalDescriptionCount(), job.getSuccessCount(), job.getFailureCount(),
				job.getPassed(), job.getFailed(), job.getFailureTypeId(), job.getChunkingErrorId(),
				job.getLastChunkedGuId(), job.getJobId());
		return;

	}

	@Override
	public void updatePassFailWithCheck(Job job) {
		String user = job.getJobCreatedUser();
		String sql = "UPDATE  "+jobSchema+".ingest_jobs SET passed=? , failed=?  "
				+ " ,last_update_date=current_timestamp  WHERE job_id =? and passed=false and failed=false";
		jdbcTemplate.update(sql, job.getPassed(), job.getFailed(),  job.getJobId());
		return;

	}
	
	public void updateCurrentAction(Job job, long actionId) {
		String user = job.getJobCreatedUser();
		String sql = "UPDATE  "+jobSchema+".ingest_jobs " + "SET current_action_type_id=? " + ", last_update_date=current_timestamp"
				+ " WHERE job_id =?";
		jdbcTemplate.update(sql, actionId, job.getJobId());
		return;
	}

	// public void updateCurrentAction(Job job, String action) {
	// String user = job.getJobCreatedUser();
	// String sql = "UPDATE ingest_jobs "
	// + "SET current_action_type_id=(select id from job_action_types where
	// action=?) "
	// + ", last_update_date=current_timestamp"
	// + " WHERE job_id =?";
	// jdbcTemplate.update(sql, action, job.getJobId());
	// return;
	// }

	public void updateTotalDescriptionCount(Job job) {
		String user = job.getJobCreatedUser();
		String sql = "UPDATE  "+jobSchema+".ingest_jobs " + " SET current_action_type_id=? " + ", last_update_date=current_timestamp"
				+ ", total_description_count=?" + " WHERE job_id =?";
		jdbcTemplate.update(sql, JobActionTypesDAO.ActionTypes.CHUNKING_COMPLETED.id, job.getTotalDescriptionCount(),
				job.getJobId());
		return;
	}

	/**
	 * 
	 * @param job
	 */
	public void updateInsertCount(Job job) {
		/**
		 * TBD: consider updating passed = total_description_count=(success_count + failure_count)
		 */
		String user = job.getJobCreatedUser();
		String sql = "UPDATE  "+jobSchema+".ingest_jobs " + " SET last_update_date=current_timestamp"
				+ ", success_count=success_count + 1" + " WHERE job_id=?";
		jdbcTemplate.update(sql, job.getJobId());
		return;
	}

	@Override
	public Job getById(long id) {
		Job job = null;
		String sql = "SELECT * , current_timestamp as retrieved_time "
				+ ", extract(epoch from current_timestamp) epoch_seconds_retrieved, extract(epoch from job_creation_time) epoch_seconds_created"
				+ " from  "+jobSchema+".ingest_jobs where job_id=?";
		List<Job> results = jdbcTemplate.query(sql, new JobMapper(), new Object[] { id });
		if (!results.isEmpty()) {
			job = results.get(0);
		}
		return job;
	}

	@Override
	public List<Job> getJobsInProcess(long scanThresholdInSeconds, long limit) {
		String sql = "select * , current_timestamp retrieved_time "
				+ ", extract(epoch from current_timestamp) epoch_seconds_retrieved, extract(epoch from job_creation_time) epoch_seconds_created"
				+ " from  "+jobSchema+".ingest_jobs where "
				+ " total_description_count > 0 and extract(epoch from current_timestamp)- extract(epoch from job_creation_time) > ? "
				+ " and failed=false and passed=false LIMIT ?";
		List<Job> results = jdbcTemplate.query(sql, new JobMapper(), new Object[] { scanThresholdInSeconds, limit });
		return results;
	}
	/**
	 * select ingest_jobs.job_id from sandbox.ingest_jobs left join sandbox.message_status on ingest_jobs.job_id=message_status.job_id  group by ingest_jobs.job_id,message_status.insert_success having count(insert_success)=total_description_count  and insert_success=true
	 */
	@Override
	public boolean getJobCompletedStatus(long jobId) throws Exception{
		String sql = "select (count(insert_success)=total_description_count)  "
				+ " from  "+jobSchema+".ingest_jobs "
				+ " left join  "+jobSchema+".message_status on ingest_jobs.job_id=message_status.job_id  "
				+ " where ingest_jobs.job_id=? and insert_success=true  "
				+ " group by ingest_jobs.job_id,message_status.insert_success ";
				boolean passed=jdbcTemplate.queryForObject(sql, new Object[] {jobId},Boolean.class);
		return passed ;
	}
}
