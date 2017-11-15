
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
public class SpecialPjtForAuth{
	private long specialPjtForAuthId;
	private long primAuthNaId;
	private long authListNaId;
	public long getSpecialPjtForAuthId(){
		return specialPjtForAuthId;
	}
	public long getPrimAuthNaId(){
		return primAuthNaId;
	}
	public long getAuthListNaId(){
		return authListNaId;
	}
	public void setSpecialPjtForAuthId(long aspecialPjtForAuthId){
		specialPjtForAuthId = aspecialPjtForAuthId;
	}
	public void setPrimAuthNaId(long aprimAuthNaId){
		primAuthNaId = aprimAuthNaId;
	}
	public void setAuthListNaId(long aauthListNaId){
		authListNaId = aauthListNaId;
	}
}