package gov.nara.das.common.db.das;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import static gov.nara.das.common.utils.JSONUtils.*;

/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class Description {
	
	  
	private long descNaId;
	private String guid;
	private String descType;
	private String descTitle;
	//

	private String title;
	private boolean isUnderEdit;

	private String dateNote;
	private Timestamp createdDate;
	
	//
	private String sqsMessageId;
	private String chunkingId;
	private long jobId;
	private long messageIndex;

	private String dataCtlGp=new String();
	
	private String inclusiveStartDate=new String();
	
	private String inclusiveEndDate=new String();

	private String coverageStartDate=new String();

	private String coverageEndDate=new String();
	
	private String productionDate=new String();

	private String copyrightDate=new String();
	
	private String releaseDate=new String();

	private String broadcastDate=new String();
	
	private String ptrObjAvailabilityDate=new String();
	
	private String descriptionAuthor=new String();
	
	private String approvalHistory=new String();
	
	private String changeHistory=new String();

	private String broughtUnderEditHistory=new String();
	//
	
	private Timestamp lastBroughtUnderEditDate;
	
	private long recordGpNo;

	private String collectionIdentifier;

	private long parentNaId;

	private String otherTitle;

	private long inclusiveStartDateQualifierNaId;

	private long inclusiveEndDateQualifierNaId;

	//private String title;


	private long coverageStartDateQualifierNaId;

	private long coverageEndDateQualifierNaId;

	//private String dateNote;

	private String scopeContentNote;

	private String staffOnlyNote;

	private boolean recordStatus;

	private long partyDesignationNaId;

	

	private long beginCongressNaId;

	private long endCongressNaId;

	private String arrangement;

	private String functionUser;

	private String generalNote;

	private String localIdentifier;

	private String numberingNote;

	private long accessionNumberNaId;

	private long recordCtrXferNumberNaId;

	private long dispositionAuthNumberNaId;

	private long internalXferNumberNaId;

	private String xferNote;

	private String custodialHistoryNote;

	private long onlineResourceNaId;

	private String scaleNote;

	private long editStatusNaId;

	private long soundTypeNaId;

	private String totalFootage;

	private String isAv;

	
	private String subtitle;

	private String shotlist;

	private long broadcastDateQualifierNaId;

	private long releaseDateQualifierNaId;

	private long copyrightDateQualifierNaId;

	private long productionDateQualifierNaId;

	private long ptrObjAvailabilityDateQualifierNaId;

	//private boolean isUnderEdit;

	//private Timestamp createdDate;

	private Timestamp importedDate;

	private Timestamp lastChangedDate;

	private Timestamp lastApprovedDate;


	private String createdUser;

	private String importedUser;

	private String lastChangedUser;

	private String lastApprovedUser;

	private String lastBroughtUnderEditUser;
	
	private AuthorityList beginCongressAuthorityList;
	
	private AuthorityList endCongressAuthorityList;
	
	private List<AuthorityList> specialProjectAuthorityList=new ArrayList<AuthorityList>();

	private List<AuthorityList> findingAidAuthorityList=new ArrayList<AuthorityList>();
	
	private List<AuthorityList> digitalObjectAuthorityList=new ArrayList<AuthorityList>();
	
	//private List<DigitalObject> digitalObjectList=new ArrayList<DigitalObject>();
	
	//private List<FindingAid> findingAidList=new ArrayList<FindingAid>();
	
	private Description parentRecordGroup;
	
	private AuthorityList accessRestriction;

	
	
	/**
	 * @return the descNaId
	 */
	public long getDescNaId() {
		return descNaId;
	}
	/**
	 * @return the descType
	 */
	public String getDescType() {
		return descType;
	}
	/**
	 * @return the descTitle
	 */
	public String getDescTitle() {
		return descTitle;
	}
	/**
	 * @param descNaId the descNaId to set
	 */
	public void setDescNaid(long descNaId) {
		this.descNaId = descNaId;
	}
	/**
	 * @param descType the descType to set
	 */
	public void setDescType(String descType) {
		this.descType = descType;
	}
	/**
	 * @param descTitle the descTitle to set
	 */
	public void setDescTitle(String descTitle) {
		this.descTitle = descTitle;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the isUnderEdit
	 */
	public boolean isUnderEdit() {
		return isUnderEdit;
	}

	/**
	 * @return the dateNote
	 */
	public String getDateNote() {
		return dateNote;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @param isUnderEdit the isUnderEdit to set
	 */
	public void setUnderEdit(boolean isUnderEdit) {
		this.isUnderEdit = isUnderEdit;
	}

	/**
	 * @param dateNote the dateNote to set
	 */
	public void setDateNote(String dateNote) {
		this.dateNote = dateNote;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the dataCtlGp
	 */
	public String getDataCtlGp() {
		return dataCtlGp;
	}
	/**
	 * @return the recordGpNo
	 */
	public long getRecordGpNo() {
		return recordGpNo;
	}
	/**
	 * @return the collectionIdentifier
	 */
	public String getCollectionIdentifier() {
		return collectionIdentifier;
	}
	/**
	 * @return the parentNaId
	 */
	public long getParentNaId() {
		return parentNaId;
	}
	/**
	 * @return the otherTitle
	 */
	public String getOtherTitle() {
		return otherTitle;
	}
	/**
	 * @return the inclusiveStartDateQualifierNaId
	 */
	public long getInclusiveStartDateQualifierNaId() {
		return inclusiveStartDateQualifierNaId;
	}
	/**
	 * @return the inclusiveEndDateQualifierNaId
	 */
	public long getInclusiveEndDateQualifierNaId() {
		return inclusiveEndDateQualifierNaId;
	}
	/**
	 * @return the inclusiveStartDate
	 */
	public String getInclusiveStartDate() {
		return inclusiveStartDate;
	}
	/**
	 * @return the coverageStartDateQualifierNaId
	 */
	public long getCoverageStartDateQualifierNaId() {
		return coverageStartDateQualifierNaId;
	}
	/**
	 * @return the coverageEndDateQualifierNaId
	 */
	public long getCoverageEndDateQualifierNaId() {
		return coverageEndDateQualifierNaId;
	}
	/**
	 * @return the inclusiveEndDate
	 */
	public String getInclusiveEndDate() {
		return inclusiveEndDate;
	}
	/**
	 * @return the coverageStartDate
	 */
	public String getCoverageStartDate() {
		return coverageStartDate;
	}
	/**
	 * @return the coverageEndDate
	 */
	public String getCoverageEndDate() {
		return coverageEndDate;
	}
	/**
	 * @return the scopeContentNote
	 */
	public String getScopeContentNote() {
		return scopeContentNote;
	}
	/**
	 * @return the staffOnlyNote
	 */
	public String getStaffOnlyNote() {
		return staffOnlyNote;
	}
	/**
	 * @return the recordStatus
	 */
	public boolean getRecordStatus() {
		return recordStatus;
	}
	/**
	 * @return the partyDesignationNaId
	 */
	public long getPartyDesignationNaId() {
		return partyDesignationNaId;
	}
	/**
	 * @return the descriptionAuthor
	 */
	public String getDescriptionAuthor() {
		return descriptionAuthor;
	}
	/**
	 * @return the beginCongressNaId
	 */
	public long getBeginCongressNaId() {
		return beginCongressNaId;
	}
	/**
	 * @return the endCongressNaId
	 */
	public long getEndCongressNaId() {
		return endCongressNaId;
	}
	/**
	 * @return the arrangement
	 */
	public String getArrangement() {
		return arrangement;
	}
	/**
	 * @return the functionUser
	 */
	public String getFunctionUser() {
		return functionUser;
	}
	/**
	 * @return the generalNote
	 */
	public String getGeneralNote() {
		return generalNote;
	}
	/**
	 * @return the localIdentifier
	 */
	public String getLocalIdentifier() {
		return localIdentifier;
	}
	/**
	 * @return the numberingNote
	 */
	public String getNumberingNote() {
		return numberingNote;
	}
	/**
	 * @return the accessionNumberNaId
	 */
	public long getAccessionNumberNaId() {
		return accessionNumberNaId;
	}
	/**
	 * @return the recordCtrXferNumberNaId
	 */
	public long getRecordCtrXferNumberNaId() {
		return recordCtrXferNumberNaId;
	}
	/**
	 * @return the dispositionAuthNumberNaId
	 */
	public long getDispositionAuthNumberNaId() {
		return dispositionAuthNumberNaId;
	}
	/**
	 * @return the internalXferNumberNaId
	 */
	public long getInternalXferNumberNaId() {
		return internalXferNumberNaId;
	}
	/**
	 * @return the xferNote
	 */
	public String getXferNote() {
		return xferNote;
	}
	/**
	 * @return the custodialHistoryNote
	 */
	public String getCustodialHistoryNote() {
		return custodialHistoryNote;
	}
	/**
	 * @return the onlineResourceNaId
	 */
	public long getOnlineResourceNaId() {
		return onlineResourceNaId;
	}
	/**
	 * @return the scaleNote
	 */
	public String getScaleNote() {
		return scaleNote;
	}
	/**
	 * @return the editStatusNaId
	 */
	public long getEditStatusNaId() {
		return editStatusNaId;
	}
	/**
	 * @return the soundTypeNaId
	 */
	public long getSoundTypeNaId() {
		return soundTypeNaId;
	}
	/**
	 * @return the totalFootage
	 */
	public String getTotalFootage() {
		return totalFootage;
	}
	/**
	 * @return the isAv
	 */
	public String getIsAv() {
		return isAv;
	}
	/**
	 * @return the ptrObjAvailabilityDate
	 */
	public String getPtrObjAvailabilityDate() {
		return ptrObjAvailabilityDate;
	}
	/**
	 * @return the productionDate
	 */
	public String getProductionDate() {
		return productionDate;
	}
	/**
	 * @return the copyrightDate
	 */
	public String getCopyrightDate() {
		return copyrightDate;
	}
	/**
	 * @return the subtitle
	 */
	public String getSubtitle() {
		return subtitle;
	}
	/**
	 * @return the releaseDate
	 */
	public String getReleaseDate() {
		return releaseDate;
	}
	/**
	 * @return the broadcastDate
	 */
	public String getBroadcastDate() {
		return broadcastDate;
	}
	/**
	 * @return the shotlist
	 */
	public String getShotlist() {
		return shotlist;
	}
	/**
	 * @return the broadcastDateQualifierNaId
	 */
	public long getBroadcastDateQualifierNaId() {
		return broadcastDateQualifierNaId;
	}
	/**
	 * @return the releaseDateQualifierNaId
	 */
	public long getReleaseDateQualifierNaId() {
		return releaseDateQualifierNaId;
	}
	/**
	 * @return the copyrightDateQualifierNaId
	 */
	public long getCopyrightDateQualifierNaId() {
		return copyrightDateQualifierNaId;
	}
	/**
	 * @return the productionDateQualifierNaId
	 */
	public long getProductionDateQualifierNaId() {
		return productionDateQualifierNaId;
	}
	/**
	 * @return the ptrObjAvailabilityDateQualifierNaId
	 */
	public long getPtrObjAvailabilityDateQualifierNaId() {
		return ptrObjAvailabilityDateQualifierNaId;
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
	 * @return the lastBroughtUnderEditDate
	 */
	public Timestamp getLastBroughtUnderEditDate() {
		return lastBroughtUnderEditDate;
	}
	/**
	 * @return the approvalHistory
	 */
	public String getApprovalHistory() {
		return approvalHistory;
	}
	/**
	 * @return the changeHistory
	 */
	public String getChangeHistory() {
		return changeHistory;
	}
	/**
	 * @return the broughtUnderEditHistory
	 */
	public String getBroughtUnderEditHistory() {
		return broughtUnderEditHistory;
	}
	/**
	 * @return the createdUser
	 */
	public String getCreatedUser() {
		return createdUser;
	}
	/**
	 * @return the importedUser
	 */
	public String getImportedUser() {
		return importedUser;
	}
	/**
	 * @return the lastChangedUser
	 */
	public String getLastChangedUser() {
		return lastChangedUser;
	}
	/**
	 * @return the lastApprovedUser
	 */
	public String getLastApprovedUser() {
		return lastApprovedUser;
	}
	/**
	 * @return the lastBroughtUnderEditUser
	 */
	public String getLastBroughtUnderEditUser() {
		return lastBroughtUnderEditUser;
	}
	/**
	 * @param descNaId the descNaId to set
	 */
	public void setDescNaId(long descNaId) {
		this.descNaId = descNaId;
	}
	/**
	 * @param dataCtlGp the dataCtlGp to set
	 */
	public void setDataCtlGp(String adataCtlGp) {
		dataCtlGp=createValidJSONObjectStringSafely(adataCtlGp);
	}

	/**
	 * @param recordGpNo the recordGpNo to set
	 */
	public void setRecordGpNo(long recordGpNo) {
		this.recordGpNo = recordGpNo;
	}
	/**
	 * @param collectionIdentifier the collectionIdentifier to set
	 */
	public void setCollectionIdentifier(String collectionIdentifier) {
		this.collectionIdentifier = collectionIdentifier;
	}
	/**
	 * @param parentNaId the parentNaId to set
	 */
	public void setParentNaId(long parentNaId) {
		this.parentNaId = parentNaId;
	}
	/**
	 * @param otherTitle the otherTitle to set
	 */
	public void setOtherTitle(String otherTitle) {
		this.otherTitle = otherTitle;
	}
	/**
	 * @param inclusiveStartDateQualifierNaId the inclusiveStartDateQualifierNaId to set
	 */
	public void setInclusiveStartDateQualifierNaId(long inclusiveStartDateQualifierNaId) {
		this.inclusiveStartDateQualifierNaId = inclusiveStartDateQualifierNaId;
	}
	/**
	 * @param inclusiveEndDateQualifierNaId the inclusiveEndDateQualifierNaId to set
	 */
	public void setInclusiveEndDateQualifierNaId(long inclusiveEndDateQualifierNaId) {
		this.inclusiveEndDateQualifierNaId = inclusiveEndDateQualifierNaId;
	}
	/**
	 * @param inclusiveStartDate the inclusiveStartDate to set
	 */
	public void setInclusiveStartDate(String  json) {
		this.inclusiveStartDate = createValidJSONObjectStringSafely(json);
	}
	/**
	 * @param coverageStartDateQualifierNaId the coverageStartDateQualifierNaId to set
	 */
	public void setCoverageStartDateQualifierNaId(long coverageStartDateQualifierNaId) {
		this.coverageStartDateQualifierNaId = coverageStartDateQualifierNaId;
	}
	/**
	 * @param coverageEndDateQualifierNaId the coverageEndDateQualifierNaId to set
	 */
	public void setCoverageEndDateQualifierNaId(long coverageEndDateQualifierNaId) {
		this.coverageEndDateQualifierNaId = coverageEndDateQualifierNaId;
	}
	/**
	 * @param inclusiveEndDate the inclusiveEndDate to set
	 */
	public void setInclusiveEndDate(String aInclusiveEndDate) {
		inclusiveEndDate=createValidJSONObjectStringSafely(aInclusiveEndDate);
	}
	/**
	 * @param coverageStartDate the coverageStartDate to set
	 */
	public void setCoverageStartDate(String aCoverageStartDate) {
		coverageStartDate=createValidJSONObjectStringSafely(aCoverageStartDate);
	}
	/**
	 * @param coverageEndDate the coverageEndDate to set
	 */
	public void setCoverageEndDate(String aCoverageEndDate) {
		coverageEndDate=createValidJSONObjectStringSafely(aCoverageEndDate);
	}
	/**
	 * @param scopeContentNote the scopeContentNote to set
	 */
	public void setScopeContentNote(String scopeContentNote) {
		this.scopeContentNote = scopeContentNote;
	}
	/**
	 * @param staffOnlyNote the staffOnlyNote to set
	 */
	public void setStaffOnlyNote(String staffOnlyNote) {
		this.staffOnlyNote = staffOnlyNote;
	}
	/**
	 * @param recordStatus the recordStatus to set
	 */
	public void setRecordStatus(boolean recordStatus) {
		this.recordStatus = recordStatus;
	}
	/**
	 * @param naid the partyDesignationNaId to set
	 */
	public void setPartyDesignationNaId(long naid) {
		this.partyDesignationNaId = naid;
	}
	/**
	 * @param descriptionAuthor the descriptionAuthor to set
	 */
	public void setDescriptionAuthor(String adescriptionAuthor) {
		descriptionAuthor=createValidJSONArrayAsString(adescriptionAuthor);
	}
	/**
	 * @param beginCongressNaId the beginCongressNaId to set
	 */
	public void setBeginCongressNaId(long beginCongressNaId) {
		this.beginCongressNaId = beginCongressNaId;
	}
	/**
	 * @param endCongressNaId the endCongressNaId to set
	 */
	public void setEndCongressNaId(long endCongressNaId) {
		this.endCongressNaId = endCongressNaId;
	}
	/**
	 * @param arrangement the arrangement to set
	 */
	public void setArrangement(String arrangement) {
		this.arrangement = arrangement;
	}
	/**
	 * @param functionUser the functionUser to set
	 */
	public void setFunctionUser(String functionUser) {
		this.functionUser = functionUser;
	}
	/**
	 * @param generalNote the generalNote to set
	 */
	public void setGeneralNote(String generalNote) {
		this.generalNote = generalNote;
	}
	/**
	 * @param localIdentifier the localIdentifier to set
	 */
	public void setLocalIdentifier(String localIdentifier) {
		this.localIdentifier = localIdentifier;
	}
	/**
	 * @param numberingNote the numberingNote to set
	 */
	public void setNumberingNote(String numberingNote) {
		this.numberingNote = numberingNote;
	}
	/**
	 * @param accessionNumberNaId the accessionNumberNaId to set
	 */
	public void setAccessionNumberNaId(long accessionNumberNaId) {
		this.accessionNumberNaId = accessionNumberNaId;
	}
	/**
	 * @param recordCtrXferNumberNaId the recordCtrXferNumberNaId to set
	 */
	public void setRecordCtrXferNumberNaId(long recordCtrXferNumberNaId) {
		this.recordCtrXferNumberNaId = recordCtrXferNumberNaId;
	}
	/**
	 * @param dispositionAuthNumberNaId the dispositionAuthNumberNaId to set
	 */
	public void setDispositionAuthNumberNaId(long dispositionAuthNumberNaId) {
		this.dispositionAuthNumberNaId = dispositionAuthNumberNaId;
	}
	/**
	 * @param internalXferNumberNaId the internalXferNumberNaId to set
	 */
	public void setInternalXferNumberNaId(long internalXferNumberNaId) {
		this.internalXferNumberNaId = internalXferNumberNaId;
	}
	/**
	 * @param xferNote the xferNote to set
	 */
	public void setXferNote(String xferNote) {
		this.xferNote = xferNote;
	}
	/**
	 * @param custodialHistoryNote the custodialHistoryNote to set
	 */
	public void setCustodialHistoryNote(String custodialHistoryNote) {
		this.custodialHistoryNote = custodialHistoryNote;
	}
	/**
	 * @param onlineResourceNaId the onlineResourceNaId to set
	 */
	public void setOnlineResourceNaId(long onlineResourceNaId) {
		this.onlineResourceNaId = onlineResourceNaId;
	}
	/**
	 * @param scaleNote the scaleNote to set
	 */
	public void setScaleNote(String scaleNote) {
		this.scaleNote = scaleNote;
	}
	/**
	 * @param editStatusNaId the editStatusNaId to set
	 */
	public void setEditStatusNaId(long editStatusNaId) {
		this.editStatusNaId = editStatusNaId;
	}
	/**
	 * @param soundTypeNaId the soundTypeNaId to set
	 */
	public void setSoundTypeNaId(long soundTypeNaId) {
		this.soundTypeNaId = soundTypeNaId;
	}
	/**
	 * @param totalFootage the totalFootage to set
	 */
	public void setTotalFootage(String totalFootage) {
		this.totalFootage = totalFootage;
	}
	/**
	 * @param isAv the isAv to set
	 */
	public void setIsAv(String isAv) {
		this.isAv = isAv;
	}
	/**
	 * @param ptrObjAvailabilityDate the ptrObjAvailabilityDate to set
	 */
	public void setPtrObjAvailabilityDate(String aptrObjAvailabilityDate) {
		ptrObjAvailabilityDate=createValidJSONObjectStringSafely(aptrObjAvailabilityDate);
	}
	/**
	 * @param productionDate the productionDate to set
	 */
	public void setProductionDate(String aproductionDate) {
		productionDate=createValidJSONObjectStringSafely(aproductionDate);
	}
	/**
	 * @param copyrightDate the copyrightDate to set
	 */
	public void setCopyrightDate(String acopyrightDate) {
		copyrightDate=createValidJSONObjectStringSafely(acopyrightDate);
	}
	/**
	 * @param subtitle the subtitle to set
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	/**
	 * @param releaseDate the releaseDate to set
	 */
	public void setReleaseDate(String areleaseDate) {
		releaseDate=createValidJSONObjectStringSafely(areleaseDate);
	}
	/**
	 * @param broadcastDate the broadcastDate to set
	 */
	public void setBroadcastDate(String abroadcastDate) {
		broadcastDate=createValidJSONObjectStringSafely(abroadcastDate);
	}
	/**
	 * @param shotlist the shotlist to set
	 */
	public void setShotlist(String shotlist) {
		this.shotlist = shotlist;
	}
	/**
	 * @param broadcastDateQualifierNaId the broadcastDateQualifierNaId to set
	 */
	public void setBroadcastDateQualifierNaId(long broadcastDateQualifierNaId) {
		this.broadcastDateQualifierNaId = broadcastDateQualifierNaId;
	}
	/**
	 * @param releaseDateQualifierNaId the releaseDateQualifierNaId to set
	 */
	public void setReleaseDateQualifierNaId(long releaseDateQualifierNaId) {
		this.releaseDateQualifierNaId = releaseDateQualifierNaId;
	}
	/**
	 * @param copyrightDateQualifierNaId the copyrightDateQualifierNaId to set
	 */
	public void setCopyrightDateQualifierNaId(long copyrightDateQualifierNaId) {
		this.copyrightDateQualifierNaId = copyrightDateQualifierNaId;
	}
	/**
	 * @param productionDateQualifierNaId the productionDateQualifierNaId to set
	 */
	public void setProductionDateQualifierNaId(long productionDateQualifierNaId) {
		this.productionDateQualifierNaId = productionDateQualifierNaId;
	}
	/**
	 * @param ptrObjAvailabilityDateQualifierNaId the ptrObjAvailabilityDateQualifierNaId to set
	 */
	public void setPtrObjAvailabilityDateQualifierNaId(long ptrObjAvailabilityDateQualifierNaId) {
		this.ptrObjAvailabilityDateQualifierNaId = ptrObjAvailabilityDateQualifierNaId;
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
	 * @param timestamp the lastBroughtUnderEditDate to set
	 */
	public void setLastBroughtUnderEditDate(Timestamp timestamp) {
		this.lastBroughtUnderEditDate = timestamp;
	}
	/**
	 * @param approvalHistory the approvalHistory to set
	 */
	public void setApprovalHistory(String aapprovalHistory) {
		approvalHistory=createValidJSONArrayAsString(aapprovalHistory);
	}
	/**
	 * @param changeHistory the changeHistory to set
	 */
	public void setChangeHistory(String achangeHistory) {
		changeHistory=createValidJSONArrayAsString(achangeHistory);
	}
	/**
	 * @param broughtUnderEditHistory the broughtUnderEditHistory to set
	 */
	public void setBroughtUnderEditHistory(String abroughtUnderEditHistory) {
		broughtUnderEditHistory=createValidJSONArrayAsString(abroughtUnderEditHistory);
	}
	/**
	 * @param createdUser the createdUser to set
	 */
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	/**
	 * @param importedUser the importedUser to set
	 */
	public void setImportedUser(String importedUser) {
		this.importedUser = importedUser;
	}
	/**
	 * @param lastChangedUser the lastChangedUser to set
	 */
	public void setLastChangedUser(String lastChangedUser) {
		this.lastChangedUser = lastChangedUser;
	}
	/**
	 * @param lastApprovedUser the lastApprovedUser to set
	 */
	public void setLastApprovedUser(String lastApprovedUser) {
		this.lastApprovedUser = lastApprovedUser;
	}
	/**
	 * @param lastBroughtUnderEditUser the lastBroughtUnderEditUser to set
	 */
	public void setLastBroughtUnderEditUser(String lastBroughtUnderEditUser) {
		this.lastBroughtUnderEditUser = lastBroughtUnderEditUser;
	}
	/**
	 * @return the sqsMessageId
	 */
	public String getSqsMessageId() {
		return sqsMessageId;
	}
	/**
	 * @return the chunkingId
	 */
	public String getChunkingId() {
		return chunkingId;
	}
	/**
	 * @return the job_id
	 */
	public long getJobId() {
		return jobId;
	}
	/**
	 * @param sqsMessageId the sqsMessageId to set
	 */
	public void setSqsMessageId(String sqsMessageId) {
		this.sqsMessageId = sqsMessageId;
	}
	/**
	 * @param chunkingId the chunkingId to set
	 */
	public void setChunkingId(String chunkingId) {
		this.chunkingId = chunkingId;
	}
	/**
	 * @param job_id the job_id to set
	 */
	public void setJobId(long id) {
		this.jobId = id;
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
	 * @return the guid
	 */
	public String getGUID() {
		return guid;
	}


	/**
	 * @return the beginCongressAuthorityList
	 */
	public AuthorityList getBeginCongressAuthorityList() {
		return beginCongressAuthorityList;
	}
	/**
	 * @param guid the guid to set
	 */
	public void setGUID(String guid) {
		this.guid = guid;
	}
	/**
	 * @param beginCongressAuthorityList the beginCongressAuthorityList to set
	 */
	public void setBeginCongressAuthorityList(AuthorityList beginCongressAuthorityList) {
		this.beginCongressAuthorityList = beginCongressAuthorityList;
	}
	/**
	 * @return the endCongressAuthorityList
	 */
	public AuthorityList getEndCongressAuthorityList() {
		return endCongressAuthorityList;
	}
	/**
	 * @param endCongressAuthorityList the endCongressAuthorityList to set
	 */
	public void setEndCongressAuthorityList(AuthorityList endCongressAuthorityList) {
		this.endCongressAuthorityList = endCongressAuthorityList;
	}
	/**
	 * @return the specialProjectAuthorityList
	 */
	public List<AuthorityList> getSpecialProjectAuthorityList() {
		return specialProjectAuthorityList;
	}
	/**
	 * @param specialProjectAuthorityList the specialProjectAuthorityList to set
	 */
	public void setSpecialProjectAuthorityList(List<AuthorityList> specialProjectAuthorityList) {
		this.specialProjectAuthorityList = specialProjectAuthorityList;
	}
	/**
	 * @return the findingAidList
	 */
	public List<AuthorityList> getFindingAidAuthorityList() {
		return findingAidAuthorityList;
	}
	

	/**
	 * @return the digitalObjectAuthorityList
	 */
	public List<AuthorityList> getDigitalObjectAuthorityList() {
		return digitalObjectAuthorityList;
	}
	/**
	 * @return the digitalObjectList
	 */

	/**
	 * @param findingAidAuthorityList the findingAidAuthorityList to set
	 */
	public void setFindingAidAuthorityList(List<AuthorityList> findingAidAuthorityList) {
		this.findingAidAuthorityList = findingAidAuthorityList;
	}
	/**
	 * @param digitalObjectAuthorityList the digitalObjectAuthorityList to set
	 */
	public void setDigitalObjectAuthorityList(List<AuthorityList> digitalObjectAuthorityList) {
		this.digitalObjectAuthorityList = digitalObjectAuthorityList;
	}

	/**
	 * @return the parentRecordGroup
	 */
	public Description getParentRecordGroup() {
		return parentRecordGroup;
	}
	/**
	 * @param parentRecordGroup the parentRecordGroup to set
	 */
	public void setParentRecordGroup(Description parentRecordGroup) {
		this.parentRecordGroup = parentRecordGroup;
	}
	/**
	 * @return the accessRestriction
	 */
	public AuthorityList getAccessRestriction() {
		return accessRestriction;
	}
	/**
	 * @param accessRestriction the accessRestriction to set
	 */
	public void setAccessRestriction(AuthorityList accessRestriction) {
		this.accessRestriction = accessRestriction;
	}

}
