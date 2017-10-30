package gov.nara.das.common.db.ingest.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import gov.nara.das.common.db.ingest.Job;

public interface JobDAOInterface {
	
	    public void create(Job job) ;

	    public Job getById(long id);
	    
	    public List<Job> getJobsInProcess(long scanThreshold, long limit);
	    
		public void update(Job job);
		
		public boolean getJobCompletedStatus(long jobId)  throws Exception;
		
		/**
		 * update the passed failed flags but only if both are false
		 * this avoids any possible conflict of multiple threads or processes processing the
		 * same job in Job Monitor
		 * @param job - must have jobId, pass and fail flags
		 */
		public void updatePassFailWithCheck(Job job);
}
