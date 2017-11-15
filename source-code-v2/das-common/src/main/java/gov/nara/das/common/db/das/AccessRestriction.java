
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
public class AccessRestriction{
	private long accessRestrictionId;
	private long descNaId;
	private long authListNaId;
	private String accessRestrictionNote;
	public long getAccessRestrictionId(){
		return accessRestrictionId;
	}
	public long getDescNaId(){
		return descNaId;
	}
	public long getAuthListNaId(){
		return authListNaId;
	}
	public String getAccessRestrictionNote(){
		return accessRestrictionNote;
	}
	public void setAccessRestrictionId(long aaccessRestrictionId){
		accessRestrictionId = aaccessRestrictionId;
	}
	public void setDescNaId(long adescNaId){
		descNaId = adescNaId;
	}
	public void setAuthListNaId(long aauthListNaId){
		authListNaId = aauthListNaId;
	}
	public void setAccessRestrictionNote(String aaccessRestrictionNote){
		accessRestrictionNote = aaccessRestrictionNote;
	}
}