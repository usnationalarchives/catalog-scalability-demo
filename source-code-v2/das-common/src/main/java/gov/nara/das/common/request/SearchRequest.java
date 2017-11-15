package gov.nara.das.common.request;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class SearchRequest {

	private String url;
	
	private String user;
	
	private String json;

	private String searchType="";
	private String searchKeyWord="";
	private List<String> filterFields=new ArrayList<String>();
	private String saveSearch="N";


	
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @return the jSON
	 */
	public String getJSON() {
		return json;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @param jSON the jSON to set
	 */
	public void setJSON(String aJSON) {
		json = aJSON;
	}

	/**
	 * @return the searchType
	 */
	public String getSearchType() {
		return searchType;
	}

	/**
	 * @return the searchKeyWord
	 */
	public String getSearchKeyWord() {
		return searchKeyWord;
	}

	/**
	 * @return the filterFields
	 */
	public List<String> getFilterFields() {
		return filterFields;
	}

	/**
	 * @return the saveSearch
	 */
	public String getSaveSearch() {
		return saveSearch;
	}

	/**
	 * @param searchType the searchType to set
	 */
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	/**
	 * @param searchKeyWord the searchKeyWord to set
	 */
	public void setSearchKeyWord(String searchKeyWord) {
		this.searchKeyWord = searchKeyWord;
	}

	/**
	 * @param filterFields the filterFields to set
	 */
	public void setFilterFields(List<String> filterFields) {
		this.filterFields = filterFields;
	}

	/**
	 * @param saveSearch the saveSearch to set
	 */
	public void setSaveSearch(String saveSearch) {
		this.saveSearch = saveSearch;
	}
	
}
