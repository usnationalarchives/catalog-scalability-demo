package gov.nara.das.common.db.das.dao;

import java.util.List;

import gov.nara.das.common.db.das.FindingAid;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public interface FindingAidDAO {

	public final String TABLE_NAME="finding_aid";
	public final String PK_FIELD="finding_aidid";
	public final String SEQUENCE_NAME="finding_aid_finding_aidid_seq";
	public final String FIELD_LIST_FOR_INSERT="finding_aidid, desc_naid"
			+ ",finding_aid_note, finding_aid_source, finding_aid_type_naid, finding_aid_url_naid, object_type_naid";
	// ObjectType corresponds to <fileType> struct in xml
	public static String ObjectType="ObjectType";
	
	// FindingAidType corresponds to <FindingAidType> struct in xml
	public static final String FindingAidType="FindingAidType";
	
	// FindingAidURL corresponds to <url> struct in xml
	public static final String FindingAidURL="FindingAidURL";
	
	public FindingAid getById(long id);
	
	public long create(FindingAid findingAid);
	
	public List<?> getByDescriptionNaid(long descNaid);
	
}
