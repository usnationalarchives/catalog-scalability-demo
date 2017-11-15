
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
public class GeneralMediaType{
	private long generalMediaTypeId;
	private long authListNaId;
	private long mediaOccurrenceId;

	public long getGeneralMediaTypeId(){
		return generalMediaTypeId;
	}
	public long getAuthListNaId(){
		return authListNaId;
	}
	public long getMediaOccurrenceId(){
		return mediaOccurrenceId;
	}

	public void setGeneralMediaTypeId(long ageneralMediaTypeId){
		generalMediaTypeId = ageneralMediaTypeId;
	}
	public void setAuthListNaId(long aauthListNaId){
		authListNaId = aauthListNaId;
	}
	public void setMediaOccurrenceId(long amediaOccurrenceId){
		mediaOccurrenceId = amediaOccurrenceId;
	}


}