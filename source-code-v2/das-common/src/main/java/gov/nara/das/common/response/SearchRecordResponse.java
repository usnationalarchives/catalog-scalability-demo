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
public class SearchRecordResponse {

	private Search search;
	
	private String error;
	
	public SearchRecordResponse(){

	}
	public SearchRecordResponse(Search aSearch){
		search=aSearch;
	}
	public SearchRecordResponse(SearchRecordResponse srr){
		search=srr.getSearch();
		error=srr.getError();
	}
	/**
	 * @return the search
	 */
	public Search getSearch() {
		return search;
	}

	/**
	 * @param search the search to set
	 */
	public void setSearch(Search search) {
		this.search = search;
	}
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
}
