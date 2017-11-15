
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
public class VariantControlNumber{
	private long variantControlNumberId;
	private long descNaId;
	private long authListNaId;
	private String variantCtlNo;
	private String variantCtlNoNote;
	public long getVariantControlNumberId(){
		return variantControlNumberId;
	}
	public long getDescNaId(){
		return descNaId;
	}
	public long getAuthListNaId(){
		return authListNaId;
	}
	public String getVariantCtlNo(){
		return variantCtlNo;
	}
	public String getVariantCtlNoNote(){
		return variantCtlNoNote;
	}
	public void setVariantControlNumberId(long avariantControlNumberId){
		variantControlNumberId = avariantControlNumberId;
	}
	public void setDescNaId(long adescNaId){
		descNaId = adescNaId;
	}
	public void setAuthListNaId(long aauthListNaId){
		authListNaId = aauthListNaId;
	}
	public void setVariantCtlNo(String avariantCtlNo){
		variantCtlNo = avariantCtlNo;
	}
	public void setVariantCtlNoNote(String avariantCtlNoNote){
		variantCtlNoNote = avariantCtlNoNote;
	}
}