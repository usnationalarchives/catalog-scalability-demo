package gov.nara.das.common.db.das.dao;

import gov.nara.das.common.db.das.Description;

public interface DescriptionDAOInterface {
	
	public Long create(Description description);
	
	public Description getById(long id);
}
