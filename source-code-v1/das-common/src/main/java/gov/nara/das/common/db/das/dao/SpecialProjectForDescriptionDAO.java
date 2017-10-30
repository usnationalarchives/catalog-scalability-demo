package gov.nara.das.common.db.das.dao;
import gov.nara.das.common.db.das.SpecialProjectForDescription;

public interface SpecialProjectForDescriptionDAO {
	public Long create(SpecialProjectForDescription spd);
	public SpecialProjectForDescription getById(long id);
}
