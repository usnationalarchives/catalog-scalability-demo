package gov.nara.das.common.db.das.dao;

import java.util.List;

import gov.nara.das.common.db.das.Language;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public interface LanguageDAO {

	public final String TABLE_NAME="language";
	public final String PK_FIELD="languageid";
	public final String SEQUENCE_NAME="language_languageid_seq";
	public final String FIELD_LIST_FOR_INSERT="languageid, desc_naid, language_naid";

	public Language getById(long id);
	
	public long create(Language vcn);
	
	public List<?> getByDescriptionNaid(long descNaid);
	
}
