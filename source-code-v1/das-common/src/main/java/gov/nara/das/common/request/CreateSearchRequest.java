package gov.nara.das.common.request;

public class CreateSearchRequest {

	private String url;
	
	private String user;
	
	private String json;

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
	
}
