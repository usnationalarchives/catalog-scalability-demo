package gov.nara.das.common.db.ingest.dao;

import java.util.List;

import gov.nara.das.common.db.ingest.MessageStatus;

public interface MessageStatusDAO {

	public void upsert (MessageStatus ms);
	
	public List<MessageStatus> getByJobId(long jobId);
}
