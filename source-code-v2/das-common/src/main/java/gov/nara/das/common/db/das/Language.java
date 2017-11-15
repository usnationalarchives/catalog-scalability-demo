
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
public class Language{
	private long languageId;
	private long descNaId;
	private long languageNaId;
	public long getLanguageId(){
		return languageId;
	}
	public long getDescNaId(){
		return descNaId;
	}
	public long getLanguageNaId(){
		return languageNaId;
	}
	public void setLanguageId(long alanguageId){
		languageId = alanguageId;
	}
	public void setDescNaId(long adescNaId){
		descNaId = adescNaId;
	}
	public void setLanguageNaId(long alanguageNaId){
		languageNaId = alanguageNaId;
	}
}