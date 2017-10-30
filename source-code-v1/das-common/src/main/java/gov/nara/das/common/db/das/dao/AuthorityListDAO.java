package gov.nara.das.common.db.das.dao;
import gov.nara.das.common.db.das.AuthorityList;
import gov.nara.das.common.db.das.SpecialProjectForDescription;

public interface AuthorityListDAO {
	public Long create(AuthorityList spd);
	public AuthorityList getById(long id);
}
