package gov.nara.das.common.db.ingest.dao.impl;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.JobActionType;
import gov.nara.das.common.db.ingest.JobActionTypeMapper;
import gov.nara.das.common.db.ingest.dao.JobActionTypesDAO;

@Repository
@ComponentScan("gov.nara.das.common.db")
public class JobActionTypeTemplate implements JobActionTypesDAO {
	@Value("${db.job.schema}")
	private String jobSchema;
	private List<JobActionType> all;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<JobActionType> getAll() {
		if (all == null) {
			String sql = "SELECT * from " + jobSchema + ".job_action_types";
			all = jdbcTemplate.query(sql, new JobActionTypeMapper());
		}
		return all;
	}

	@Override
	public JobActionType getByValue(String action) {
		List<JobActionType> all = getAll();
		JobActionType type=null;
		for(JobActionType j:all){
			if(action.equals(j.getAction())){
				type=j;
				break;
			}
		}
		return type;
	}

	@Override
	public long getIdByValue(String action) {
		long id = -1;
		List<JobActionType> all = getAll();

		for (JobActionType j : all) {
			if (action.equals(j.getAction())) {
				id = j.getId();
				break;
			}
		}
		return id;
	}
	
	@Override
	public JobActionType getById(long id) {
		List<JobActionType> all = getAll();
		JobActionType type=null;
		for(JobActionType j:all){
			if(id==j.getId()){
				type=j;
				break;
			}
		}
		return type;
	}
	
	@Override
	public String getByActionId(long id) {
		String action=null;
		JobActionType t=getById(id);
		if(t!=null){
			action=t.getAction();
		}
		return action;
	}
}
