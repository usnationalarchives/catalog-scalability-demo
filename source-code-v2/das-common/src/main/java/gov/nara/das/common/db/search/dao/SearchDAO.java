package gov.nara.das.common.db.search.dao;

import gov.nara.das.common.db.search.Search;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public interface SearchDAO{
	
	public void create(Search search);
	
	public void update(Search search);
	
	public Search getById(long id);
	
}