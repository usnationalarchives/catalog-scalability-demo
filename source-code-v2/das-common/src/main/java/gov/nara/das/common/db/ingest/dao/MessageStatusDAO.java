package gov.nara.das.common.db.ingest.dao;

import java.util.List;

import gov.nara.das.common.db.ingest.MessageStatus;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public interface MessageStatusDAO {

	public void upsert (MessageStatus ms);
	
	public List<MessageStatus> getByJobId(long jobId);
}
