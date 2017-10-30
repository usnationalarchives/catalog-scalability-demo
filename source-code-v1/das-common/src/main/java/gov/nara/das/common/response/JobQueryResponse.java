package gov.nara.das.common.response;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.nara.das.common.db.ingest.Job;
import gov.nara.das.common.db.ingest.JobActionHistory;
import gov.nara.das.common.db.ingest.MessageStatus;

public class JobQueryResponse {

	private String reportName="JobQueryResponse v1";
	private long jobId;

	private long totalDescriptionCount;

	private long successCount;

	private long failureCount;
	
	private boolean passed;

	private boolean failed;

	private long failureTypeId;

	private long chunkingErrorId;

	private String lastChunkedGuId;

	private String jobCreationTime;

	private String jobCreationTime_formatted;

	private String jobCreatedUser;

	private String currentAction;

	private String lastUpdateDate;

	private String error;

	private JobActionHistoryResponses jobActionHistory;

	private MessageStatusesLongFormatResponse messageStatuses;
	
	private List<Integer> naids=new ArrayList<Integer>();
	
	public JobQueryResponse(){
		
	}

	public JobQueryResponse(Job job,List<MessageStatus> aMessageStatuses){
		jobId=job.getJobId();
		totalDescriptionCount=job.getTotalDescriptionCount();
		passed=job.getPassed();
		successCount=job.getSuccessCount();
		failed=job.getFailed();
		failureCount=job.getFailureCount();
		failureTypeId=job.getFailureTypeId();
		chunkingErrorId=job.getChunkingErrorId();
		lastChunkedGuId=job.getLastChunkedGuId();
		setJobCreationTime(job.getJobCreationTime());
		jobCreatedUser=job.getJobCreatedUser();
		setLastUpdateDate(job.getLastUpdateDate());
		messageStatuses=new MessageStatusesLongFormatResponse(aMessageStatuses);
	}
	public long getJobId() {
		return jobId;
	}

	public long getTotalDescriptionCount() {
		return totalDescriptionCount;
	}

	public boolean getPassed() {
		return passed;
	}

	public long getSuccessCount() {
		return successCount;
	}

	public long getFailureCount() {
		return failureCount;
	}

	public boolean getFailed() {
		return failed;
	}

	public long getFailureTypeId() {
		return failureTypeId;
	}

	public long getChunkingErrorId() {
		return chunkingErrorId;
	}

	public String getLastChunkedGuId() {
		return lastChunkedGuId;
	}

	public String getJobCreatedUser() {
		return jobCreatedUser;
	}

	public void setJobId(long aJobId) {
		jobId = aJobId;
	}

	public void setTotalDescriptionCount(long aTotalDescriptionCount) {
		totalDescriptionCount = aTotalDescriptionCount;
	}

	public void setPassed(boolean b) {
		passed = b;
	}

	public void setSuccessCount(long aSuccessCount) {
		successCount = aSuccessCount;
	}

	public void setFailureCount(long aFailureCount) {
		failureCount = aFailureCount;
	}

	public void setFailed(boolean aFailed) {
		failed = aFailed;
	}

	public void setFailureTypeId(long aFailureTypeId) {
		failureTypeId = aFailureTypeId;
	}

	public void setChunkingErrorId(long aChunkingErrorId) {
		chunkingErrorId = aChunkingErrorId;
	}

	public void setLastChunkedGuId(String aLastChunkedGuId) {
		lastChunkedGuId = aLastChunkedGuId;
	}

	public void setJobCreationTime(Timestamp aJobCreationTime) {
		if (aJobCreationTime != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd~HH:mm:ss");
			Date date = new Date(aJobCreationTime.getTime());
			jobCreationTime = formatter.format(date);
		} else {
			jobCreationTime="";
		}
	}

	public void setJobCreatedUser(String aJobCreatedUser) {
		jobCreatedUser = aJobCreatedUser;
	}

	public void setCurrentActionTypeId(String aCurrentAction) {
		currentAction = aCurrentAction;
	}

	public void setLastUpdateDate(Timestamp aLastUpdateDate) {
		if (aLastUpdateDate != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd~HH:mm:ss");
			Date date = new Date(aLastUpdateDate.getTime());
			lastUpdateDate = formatter.format(date);
		} else {
			lastUpdateDate = "";
		}
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return the currentAction
	 */
	public String getCurrentAction() {
		return currentAction;
	}

	/**
	 * @param currentAction
	 *            the currentAction to set
	 */
	public void setCurrentAction(String currentAction) {
		this.currentAction = currentAction;
	}

	/**
	 * @return the jobActionHistory
	 */
	public JobActionHistoryResponses getJobActionHistory() {
		return jobActionHistory;
	}

	/**
	 * @param jobActionHistory
	 *            the jobActionHistory to set
	 */
	public void setJobActionHistory(List<JobActionHistory> list) {
		jobActionHistory = new JobActionHistoryResponses(list);
	}

	/**
	 * @return the jobCreationTime_formatted
	 */
	public String getJobCreationTime() {
		return jobCreationTime_formatted;
	}

	/**
	 * @return the lastUpdateDate_formatted
	 */
	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	/**
	 * @return the jobCreationTime_formatted
	 */
	public String getJobCreationTime_formatted() {
		return jobCreationTime_formatted;
	}

	/**
	 * @return the messageStatuses
	 */
	public MessageStatusesLongFormatResponse getMessageStatuses() {
		return messageStatuses;
	}

	/**
	 * @param messageStatuses the messageStatuses to set
	 */
	public void setMessageStatuses(MessageStatusesLongFormatResponse messageStatuses) {
		this.messageStatuses = messageStatuses;
	}

	/**
	 * @return the naids
	 */
	public List<Integer> getNaids() {
		return naids;
	}

	/**
	 * @param naids the naids to set
	 */
	public void setNaids(List<Integer> naids) {
		this.naids = naids;
	}


}
