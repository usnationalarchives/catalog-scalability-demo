package gov.nara.das.common.db.ingest;

import java.sql.Timestamp;

public class MessageStatus {
	
	private String sqsMessageId;
	
	private long messageIndex;
	
	private String guid;
	
	public long naid=-1;

	private String chunkingId;

	private String title;

	private long jobId;

	private int messageStatusTypeId;

	private boolean insertSuccess;

	private boolean insertFailed;

	private int validationFailureId;

	private Timestamp httpPostRequestTime;
	
	private Timestamp httpPostResponseTime;

	private int httpResponseCode;

	private Boolean httpTimeout;

	private Timestamp insertTimestamp;

	public String getSqsMessageId() {
		return sqsMessageId;
	}

	public String getTitle() {
		return title;
	}

	public long getJobId() {
		return jobId;
	}

	public int getMessageStatusTypeId() {
		return messageStatusTypeId;
	}

	public boolean getInsertSuccess() {
		return insertSuccess;
	}

	public boolean getInsertFailed() {
		return insertFailed;
	}

	public int getValidationFailureId() {
		return validationFailureId;
	}

	public Timestamp getHttpPostRequestTime() {
		return httpPostRequestTime;
	}

	public int getHttpResponseCode() {
		return httpResponseCode;
	}

	public boolean getHttpTimeout() {
		return httpTimeout;
	}

	public Timestamp getInsertTimestamp() {
		return insertTimestamp;
	}

	public void setSqsMessageId(String aSqsMessageId) {
		sqsMessageId = aSqsMessageId;
	}

	public void setTitle(String aTitle) {
		title = aTitle;
	}

	public void setJobId(long aJobId) {
		jobId = aJobId;
	}

	public void setMessageStatusTypeId(int aMessageStatusTypeId) {
		messageStatusTypeId = aMessageStatusTypeId;
	}

	public void setInsertSuccess(boolean aInsertSuccess) {
		insertSuccess = aInsertSuccess;
	}

	public void setInsertFailed(boolean aInsertFailed) {
		insertFailed = aInsertFailed;
	}

	public void setValidationFailureId(int aValidationFailureId) {
		validationFailureId = aValidationFailureId;
	}

	public void setHttpPostRequestTime(Timestamp aHttpPostRequestTime) {
		httpPostRequestTime = aHttpPostRequestTime;
	}

	public void setHttpResponseCode(int aHttpResponseCode) {
		httpResponseCode = aHttpResponseCode;
	}

	public void setHttpTimeout(Boolean aHttpTimeout) {
		httpTimeout = aHttpTimeout;
	}

	public void setInsertTimestamp(Timestamp aInsertTimestamp) {
		insertTimestamp = aInsertTimestamp;
	}

	/**
	 * @return the httpPostResponseTime
	 */
	public Timestamp getHttpPostResponseTime() {
		return httpPostResponseTime;
	}

	/**
	 * @param httpPostResponseTime the httpPostResponseTime to set
	 */
	public void setHttpPostResponseTime(Timestamp httpPostResponseTime) {
		this.httpPostResponseTime = httpPostResponseTime;
	}

	/**
	 * @return the messageIndex
	 */
	public long getMessageIndex() {
		return messageIndex;
	}

	/**
	 * @param messageIndex the messageIndex to set
	 */
	public void setMessageIndex(long messageIndex) {
		this.messageIndex = messageIndex;
	}

	/**
	 * @return the chunkingId
	 */
	public String getChunkingId() {
		return chunkingId;
	}

	/**
	 * @param chunkingId the chunkingId to set
	 */
	public void setChunkingId(String chunkingId) {
		this.chunkingId = chunkingId;
	}

	/**
	 * @return the naid
	 */
	public long getNaid() {
		return naid;
	}

	/**
	 * @param naid the naid to set
	 */
	public void setNaid(long naid) {
		this.naid = naid;
	}

	/**
	 * @return the guid
	 */
	public String getGUID() {
		return guid;
	}

	/**
	 * @param guid the guid to set
	 */
	public void setGUID(String guid) {
		this.guid = guid;
	}
}
