package gov.nara.das.common.db.das;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
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