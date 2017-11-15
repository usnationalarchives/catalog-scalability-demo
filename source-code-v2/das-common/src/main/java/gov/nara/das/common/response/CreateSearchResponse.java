package gov.nara.das.common.response;

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
public class CreateSearchResponse {
	
	private Search search;
	
	private String error;
	
	public CreateSearchResponse(){

	}
	
	public CreateSearchResponse(Search aSearch){
		search=aSearch;
	}

	/**
	 * @return the search
	 */
	public Search getSearch() {
		return search;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param search the search to set
	 */
	public void setSearch(Search search) {
		this.search = search;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
}
