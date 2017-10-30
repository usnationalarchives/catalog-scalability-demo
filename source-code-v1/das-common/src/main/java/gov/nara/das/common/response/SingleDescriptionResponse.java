package gov.nara.das.common.response;

import gov.nara.das.common.db.das.Description;

public class SingleDescriptionResponse {
	private Description description;
	private String error;
	public SingleDescriptionResponse(Description aDescription){
		description=aDescription;
	}
	/**
	 * @return the description
	 */
	public Description getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(Description description) {
		this.description = description;
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
