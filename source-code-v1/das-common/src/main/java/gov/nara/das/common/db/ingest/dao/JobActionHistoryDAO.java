package gov.nara.das.common.db.ingest.dao;

import java.util.List;

import gov.nara.das.common.db.ingest.JobActionHistory;

public interface JobActionHistoryDAO {
	public List<JobActionHistory> getByJobId(long jobId);
}
