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
public class FindingAid{
	private long findingAidId;
	private long descNaId;
	private long findingAidTypeNaId;
	private String findingAidNote;
	private String findingAidSource;
	private long findingAidUrlNaId;
	private long objectTypeNaId;
	public long getFindingAidId(){
		return findingAidId;
	}
	public long getDescNaId(){
		return descNaId;
	}
	public long getFindingAidTypeNaId(){
		return findingAidTypeNaId;
	}
	public String getFindingAidNote(){
		return findingAidNote;
	}
	public String getFindingAidSource(){
		return findingAidSource;
	}
	public long getFindingAidUrlNaId(){
		return findingAidUrlNaId;
	}
	public long getObjectTypeNaId(){
		return objectTypeNaId;
	}
	public void setFindingAidId(long afindingAidId){
		findingAidId = afindingAidId;
	}
	public void setDescNaId(long adescNaId){
		descNaId = adescNaId;
	}
	public void setFindingAidTypeNaId(long afindingAidTypeNaId){
		findingAidTypeNaId = afindingAidTypeNaId;
	}
	public void setFindingAidNote(String afindingAidNote){
		findingAidNote = afindingAidNote;
	}
	public void setFindingAidSource(String afindingAidSource){
		findingAidSource = afindingAidSource;
	}
	public void setFindingAidUrlNaId(long afindingAidUrlNaId){
		findingAidUrlNaId = afindingAidUrlNaId;
	}
	public void setObjectTypeNaId(long aobjectTypeNaId){
		objectTypeNaId = aobjectTypeNaId;
	}
}