package gov.nara.das.common.db.das.dao;
import gov.nara.das.common.db.das.AuthorityList;
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
public interface AuthorityListDAO {
	
    public static final String SELECT_FIELD_CLAUSE="auth_list_naid ,term_name ,to_json(auth_details) auth_details ,auth_type ,created_date ,imported_date ,last_changed_date ,last_approved_date ,last_brought_under_edit ,to_json(approval_history) approval_history,to_json(changed_history) changed_history,to_json(brought_under_edit_history) brought_under_edit_history,is_under_edit";
	public static final String BeginCongress="BeginCongress";
	public static final String EndCongress="EndCongress";
	//
	public static final String FindingAidType="FindingAidType";
	public static final String FindingAidURL="FindingAidURL";
	public static final String ObjectType="ObjectType";
	//
	public static final String SpecialProject="SpecialProject";
	//
	public static final String VariantControlNumberType="VariantControlNumberType";
	//
	public static final String AccessRestrictionStatus="AccessRestrictionStatus";
	//
	public Long create(AuthorityList spd);
	public AuthorityList getById(long id);
	public AuthorityList getOneByTermNameAndType(String termName, String type);
}
