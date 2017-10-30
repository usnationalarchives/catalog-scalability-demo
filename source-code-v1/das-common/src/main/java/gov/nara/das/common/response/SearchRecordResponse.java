package gov.nara.das.common.response;

import gov.nara.das.common.db.search.Search;

public class SearchRecordResponse {

	private Search search;
	
	public SearchRecordResponse(Search aSearch){
		search=aSearch;
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
}
