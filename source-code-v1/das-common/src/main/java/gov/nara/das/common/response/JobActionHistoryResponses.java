package gov.nara.das.common.response;
import java.util.ArrayList;
import java.util.List;

import gov.nara.das.common.db.ingest.JobActionHistory;
public class JobActionHistoryResponses extends ArrayList<JobActionHistoryResponse>{
	public JobActionHistoryResponses(List<JobActionHistory> list){
		for(JobActionHistory j:list){
			JobActionHistoryResponse j2=new JobActionHistoryResponse();
			j2.setActionTime(j.getActionTime());
			j2.setAction(j.getAction());
			add(j2);
		}
	}
}
