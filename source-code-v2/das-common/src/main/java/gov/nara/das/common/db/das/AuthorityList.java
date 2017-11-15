package gov.nara.das.common.db.das;

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
public class AuthorityList {	
	
	private long authListNaId;

	private String termName;

	private String authDetails;

	private String authType;

	private Timestamp createdDate;

	private Timestamp importedDate;

	private Timestamp lastChangedDate;

	private Timestamp lastApprovedDate;

	private Timestamp lastBroughtUnderEdit;

	private String approvalHistory;

	private String changedHistory;

	private String broughtUnderEditHistory;

	private boolean isUnderEdit;

	public AuthorityList(){
		
	}
	
	public AuthorityList(String type, long authNaid){
		authType=type;
		authListNaId=authNaid;
	}
	/**
	 * @return the authListNaId
	 */
	public long getAuthListNaId() {
		return authListNaId;
	}

	/**
	 * @return the termName
	 */
	public String getTermName() {
		return termName;
	}

	/**
	 * @return the authDetails
	 */
	public String getAuthDetails() {
		return authDetails;
	}

	/**
	 * @return the authType
	 */
	public String getAuthType() {
		return authType;
	}

	/**
	 * @return the createdDate
	 */
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	/**
	 * @return the importedDate
	 */
	public Timestamp getImportedDate() {
		return importedDate;
	}

	/**
	 * @return the lastChangedDate
	 */
	public Timestamp getLastChangedDate() {
		return lastChangedDate;
	}

	/**
	 * @return the lastApprovedDate
	 */
	public Timestamp getLastApprovedDate() {
		return lastApprovedDate;
	}

	/**
	 * @return the lastBroughtUnderEdit
	 */
	public Timestamp getLastBroughtUnderEdit() {
		return lastBroughtUnderEdit;
	}

	/**
	 * @return the approvalHistory
	 */
	public String getApprovalHistory() {
		return approvalHistory;
	}

	/**
	 * @return the changedHistory
	 */
	public String getChangedHistory() {
		return changedHistory;
	}

	/**
	 * @return the broughtUnderEditHistory
	 */
	public String getBroughtUnderEditHistory() {
		return broughtUnderEditHistory;
	}

	/**
	 * @return the isUnderEdit
	 */
	public boolean isUnderEdit() {
		return isUnderEdit;
	}

	/**
	 * @param authListNaId the authListNaId to set
	 */
	public void setAuthListNaId(long authListNaId) {
		this.authListNaId = authListNaId;
	}

	/**
	 * @param termName the termName to set
	 */
	public void setTermName(String termName) {
		this.termName = termName;
	}

	/**
	 * @param authDetails the authDetails to set
	 */
	public void setAuthDetails(String authDetails) {
		this.authDetails = authDetails;
	}

	/**
	 * @param authType the authType to set
	 */
	public void setAuthType(String authType) {
		this.authType = authType;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @param importedDate the importedDate to set
	 */
	public void setImportedDate(Timestamp importedDate) {
		this.importedDate = importedDate;
	}

	/**
	 * @param lastChangedDate the lastChangedDate to set
	 */
	public void setLastChangedDate(Timestamp lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}

	/**
	 * @param lastApprovedDate the lastApprovedDate to set
	 */
	public void setLastApprovedDate(Timestamp lastApprovedDate) {
		this.lastApprovedDate = lastApprovedDate;
	}

	/**
	 * @param lastBroughtUnderEdit the lastBroughtUnderEdit to set
	 */
	public void setLastBroughtUnderEdit(Timestamp lastBroughtUnderEdit) {
		this.lastBroughtUnderEdit = lastBroughtUnderEdit;
	}

	/**
	 * @param approvalHistory the approvalHistory to set
	 */
	public void setApprovalHistory(String approvalHistory) {
		this.approvalHistory = approvalHistory;
	}

	/**
	 * @param changedHistory the changedHistory to set
	 */
	public void setChangedHistory(String changedHistory) {
		this.changedHistory = changedHistory;
	}

	/**
	 * @param broughtUnderEditHistory the broughtUnderEditHistory to set
	 */
	public void setBroughtUnderEditHistory(String broughtUnderEditHistory) {
		this.broughtUnderEditHistory = broughtUnderEditHistory;
	}

	/**
	 * @param isUnderEdit the isUnderEdit to set
	 */
	public void setUnderEdit(boolean isUnderEdit) {
		this.isUnderEdit = isUnderEdit;
	}



}
