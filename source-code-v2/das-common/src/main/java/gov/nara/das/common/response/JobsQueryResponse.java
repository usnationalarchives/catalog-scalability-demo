package gov.nara.das.common.response;

import java.util.ArrayList;
import java.util.List;

import gov.nara.das.common.db.ingest.Job;

/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class JobsQueryResponse extends ArrayList<JobQueryResponse>{
	public JobsQueryResponse(List<Job> list){
		for(Job j:list){
			add(new JobQueryResponse(j,null));
		}
	}
}
