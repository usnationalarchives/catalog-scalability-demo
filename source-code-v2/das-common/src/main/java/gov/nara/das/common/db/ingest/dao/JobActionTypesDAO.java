package gov.nara.das.common.db.ingest.dao;

import java.util.List;

import gov.nara.das.common.db.ingest.JobActionType;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public interface JobActionTypesDAO {
	public static enum ActionTypes{
		CREATED(1, "created"),
		CHUNKING_STARTED(2, "chunking started"),
		CHUNKING_COMPLETED(3, "chunking completed"),
		COMPLETED(4, "completed"),
		PROCESSING(5, "processing");
		public final long id;
		public final String action;
		ActionTypes(long aId, String aAction){
			id=aId;
			action=aAction;
		}

	}
	public List<JobActionType> getAll();
	public JobActionType getByValue(String value);
	public JobActionType getById(long id);
	public String getByActionId(long id);
	public long getIdByValue(String value) ;
}
