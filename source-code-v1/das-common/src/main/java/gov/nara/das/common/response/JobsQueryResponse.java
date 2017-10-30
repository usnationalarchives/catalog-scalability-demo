package gov.nara.das.common.response;

import java.util.ArrayList;
import java.util.List;

import gov.nara.das.common.db.ingest.Job;

public class JobsQueryResponse extends ArrayList<JobQueryResponse>{
	public JobsQueryResponse(List<Job> list){
		for(Job j:list){
			add(new JobQueryResponse(j,null));
		}
	}
}
