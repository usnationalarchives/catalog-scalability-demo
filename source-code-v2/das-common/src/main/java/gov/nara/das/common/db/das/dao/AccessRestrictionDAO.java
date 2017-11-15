package gov.nara.das.common.db.das.dao;
import java.util.List;

import gov.nara.das.common.db.das.AccessRestriction;
import gov.nara.das.common.db.das.SpecialProjectForDescription;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public interface AccessRestrictionDAO {
	public static final String TABLE="access_restriction";
	public static final String PK="access_restrictionid";
	public static final String SEQUENCE="access_restriction_access_restrictionid_seq";
    public static final String SELECT_FIELD_CLAUSE="access_restrictionid ,desc_naid ,auth_list_naid,access_restriction_note";
    public static final String INSERT_FIELD_CLAUSE="access_restrictionid ,desc_naid ,auth_list_naid,access_restriction_note";
    public static final String Restricted="Restricted";
	public static final String Unrestricted="Unrestricted";
	//
	public Long create(AccessRestriction spd);
	public AccessRestriction getById(long id);
	public List<AccessRestriction> getByDescriptionNaid(long descNaid);

}
