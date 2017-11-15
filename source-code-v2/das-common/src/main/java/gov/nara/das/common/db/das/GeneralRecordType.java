
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
public class GeneralRecordType{

	private long generalRecordTypeId;
	private long descNaId;
	private long authListNaId;

	public long getGeneralRecordTypeId(){
		return generalRecordTypeId;
	}
	public long getDescNaId(){
		return descNaId;
	}
	public long getAuthListNaId(){
		return authListNaId;
	}

	public void setGeneralRecordTypeId(long ageneralRecordTypeId){
		generalRecordTypeId = ageneralRecordTypeId;
	}
	public void setDescNaId(long adescNaId){
		descNaId = adescNaId;
	}
	public void setAuthListNaId(long aauthListNaId){
		authListNaId = aauthListNaId;
	}





}