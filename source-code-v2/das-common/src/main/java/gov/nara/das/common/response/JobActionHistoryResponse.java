package gov.nara.das.common.response;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class JobActionHistoryResponse {
	private long id;
	private long jobId;
	private String action;
	private String actionTime;
	
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
	public String getAction() {
		return action;
	}
	/**
	 * @return the actionTime
	 */
	public String getActionTime() {
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
	public void setAction(String aAction) {
		action = aAction;
	}
	/**
	 * @param actionTime the actionTime to set
	 */
	public void setActionTime(Timestamp aActionTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd~HH:mm:ss");
        Date date=new Date(aActionTime.getTime());
        actionTime=formatter.format(date);
	}
}
