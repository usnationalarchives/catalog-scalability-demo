package gov.nara.das.common.db.ingest.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.JobActionHistory;
import gov.nara.das.common.db.ingest.JobActionHistoryMapper;
import gov.nara.das.common.db.ingest.dao.JobActionHistoryDAO;
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
@ComponentScan("gov.nara.das.common.db")
public class JobActionHistoryTemplate implements JobActionHistoryDAO {
	
	@Value("${db.job.schema}")
	private String jobSchema;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private JobActionTypeTemplate actionTypeTemplate;
	
	public List<JobActionHistory> getByJobId(long jobId){
		String sql="select job_action_history.id, job_id, action_type_id, action_time, action "
				+ "from "+jobSchema+".job_action_history, "+jobSchema+".job_action_types "
				+ "where job_id=?"
				+ " AND action_type_id = job_action_types.id";
		List<JobActionHistory> list= jdbcTemplate.query(sql, new JobActionHistoryMapper(),new Object[] {jobId});
		return list;
	}
	public void insert(Job job, long actionId){
		String sql="insert into job_action_history(id,job_id,action_type_id,action_time)"
				+ " values(nextval('job_action_history_seq')"
				+ ", ?,?,current_timestamp"
				+ ")";
		jdbcTemplate.update(sql,job.getJobId(),actionId);
	}
}
