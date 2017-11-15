package gov.nara.das.common.db.search;

import java.sql.Timestamp;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class Search{
	
	private long id;
	private String searchCreatedUser;
	private Timestamp searchCreationTime;
	private Timestamp lastUpdateTime;
	private String searchJSON;
	
	
	public Search(){
		
	}
	public Search(Search search){
		id=search.id;
		searchCreatedUser=search.getSearchCreatedUser();
		searchJSON=search.getSearchJSON();
		lastUpdateTime=search.getLastUpdateTime();
		searchCreationTime=search.getSearchCreationTime();
	}
	/**
	 * @return the id
	 */
	
	public long getId() {
		return id;
	}
	/**
	 * @return the searchCreatedUser
	 */
	public String getSearchCreatedUser() {
		return searchCreatedUser;
	}
	/**
	 * @return the searchCreationTime
	 */
	public Timestamp getSearchCreationTime() {
		return searchCreationTime;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @param searchCreatedUser the searchCreatedUser to set
	 */
	public void setSearchCreatedUser(String searchCreatedUser) {
		this.searchCreatedUser = searchCreatedUser;
	}
	/**
	 * @param searchCreationTime the searchCreationTime to set
	 */
	public void setSearchCreationTime(Timestamp searchCreationTime) {
		this.searchCreationTime = searchCreationTime;
	}
	/**
	 * @return the searchJSON
	 */
	public String getSearchJSON() {
		return searchJSON;
	}
	/**
	 * @param searchJSON the searchJSON to set
	 */
	public void setSearchJSON(String searchJSON) {
		this.searchJSON = searchJSON;
	}
	/**
	 * @return the lastUpdateTime
	 */
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}
	/**
	 * @param lastUpdateTime the lastUpdateTime to set
	 */
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}