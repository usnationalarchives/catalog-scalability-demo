package gov.nara.das.common.db.ingest;

import java.sql.Timestamp;

public class JobActionHistory {
	
	private long id;
	private long jobId;
	private long actionTypeId;
	private Timestamp actionTime;
	private String action;
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	/**
	 * @return the jobId
	 */
	public long getJobId() {
		return jobId;
	}
	/**
	 * @return the actionTypeId
	 */
	public long getActionTypeId() {
		return actionTypeId;
	}
	/**
	 * @return the actionTime
	 */
	public Timestamp getActionTime() {
		return actionTime;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @param jobId the jobId to set
	 */
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}
	/**
	 * @param actionTypeId the actionTypeId to set
	 */
	public void setActionTypeId(long actionTypeId) {
		this.actionTypeId = actionTypeId;
	}
	/**
	 * @param actionTime the actionTime to set
	 */
	public void setActionTime(Timestamp actionTime) {
		this.actionTime = actionTime;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
}
