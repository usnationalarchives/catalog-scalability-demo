package gov.nara.das.common.db.search.dao;

import gov.nara.das.common.db.search.Search;

public interface SearchDAO{
	
	public void create(Search search);
	
	public void update(Search search);
	
	public Search getById(long id);
	
}