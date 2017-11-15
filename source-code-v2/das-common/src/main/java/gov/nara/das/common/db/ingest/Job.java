package gov.nara.das.common.db.ingest;

import java.sql.Timestamp;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class Job {

	private long jobId;

	private long totalDescriptionCount;

	private boolean passed;

	private long successCount;

	private long failureCount;

	private boolean failed;

	private long failureTypeId;

	private long chunkingErrorId;

	private String lastChunkedGuId;

	private Timestamp jobCreationTime;

	private String jobCreatedUser;

	private long currentActionTypeId;

	private Timestamp lastUpdateDate;
	
	private Timestamp retrievedTime;

	// for timezone conversion issue
	
	private long epochSecondsRetrieved;
	
	private long epochSecondsCreated;
	
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


public Timestamp getJobCreationTime() {
	return jobCreationTime;
}


public String getJobCreatedUser() {
	return jobCreatedUser;
}


public long getCurrentActionTypeId() {
	return currentActionTypeId;
}


public Timestamp getLastUpdateDate() {
	return lastUpdateDate;
}




public void setJobId(long aJobId) {
	jobId=aJobId;
}


public void setTotalDescriptionCount(long aTotalDescriptionCount) {
	totalDescriptionCount=aTotalDescriptionCount;
}


public void setPassed(boolean b) {
	passed=b;
}


public void setSuccessCount(long aSuccessCount) {
	successCount=aSuccessCount;
}


public void setFailureCount(long aFailureCount) {
	failureCount=aFailureCount;
}


public void setFailed(boolean aFailed) {
	failed=aFailed;
}


public void setFailureTypeId(long aFailureTypeId) {
	failureTypeId=aFailureTypeId;
}


public void setChunkingErrorId(long aChunkingErrorId) {
	chunkingErrorId=aChunkingErrorId;
}


public void setLastChunkedGuId(String aLastChunkedGuId) {
	lastChunkedGuId=aLastChunkedGuId;
}


public void setJobCreationTime(Timestamp aJobCreationTime) {
	jobCreationTime=aJobCreationTime;
}


public void setJobCreatedUser(String aJobCreatedUser) {
	jobCreatedUser=aJobCreatedUser;
}


public void setCurrentActionTypeId(long aCurrentActionTypeId) {
	currentActionTypeId=aCurrentActionTypeId;
}


public void setLastUpdateDate(Timestamp aLastUpdateDate) {
	lastUpdateDate=aLastUpdateDate;
}


/**
 * @return the retrievedTime
 */
public Timestamp getRetrievedTime() {
	return retrievedTime;
}


/**
 * @param retrievedTime the retrievedTime to set
 */
public void setRetrievedTime(Timestamp retrievedTime) {
	this.retrievedTime = retrievedTime;
}


/**
 * @return the epochSecondsRetrieved
 */
public long getEpochSecondsRetrieved() {
	return epochSecondsRetrieved;
}


/**
 * @return the epochSecondsCreated
 */
public long getEpochSecondsCreated() {
	return epochSecondsCreated;
}


/**
 * @param epochSecondsRetrieved the epochSecondsRetrieved to set
 */
public void setEpochSecondsRetrieved(long epochSecondsRetrieved) {
	this.epochSecondsRetrieved = epochSecondsRetrieved;
}


/**
 * @param epochSecondsCreated the epochSecondsCreated to set
 */
public void setEpochSecondsCreated(long epochSecondsCreated) {
	this.epochSecondsCreated = epochSecondsCreated;
}
}
