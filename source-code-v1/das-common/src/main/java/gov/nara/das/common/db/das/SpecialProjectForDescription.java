package gov.nara.das.common.db.das;

public class SpecialProjectForDescription {
	private long specialPjtForDescId;
	private long descNaid;
	private Long authListNaid;
	/**
	 * @return the specialPjtForDescId
	 */
	public long getSpecialPjtForDescId() {
		return specialPjtForDescId;
	}
	/**
	 * @return the descNaid
	 */
	public long getDescNaid() {
		return descNaid;
	}
	/**
	 * @return the authListNaid
	 */
	public Long getAuthListNaid() {
		return authListNaid;
	}
	/**
	 * @param specialPjtForDescId the specialPjtForDescId to set
	 */
	public void setSpecialPjtForDescId(long specialPjtForDescId) {
		this.specialPjtForDescId = specialPjtForDescId;
	}
	/**
	 * @param descNaid the descNaid to set
	 */
	public void setDescNaid(long descNaid) {
		this.descNaid = descNaid;
	}
	/**
	 * @param authListNaid the authListNaid to set
	 */
	public void setAuthListNaid(Long authListNaid) {
		this.authListNaid = authListNaid;
	}
	
}