package gov.nara.das.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import gov.nara.das.common.db.das.Description;

/**
 * TBD: this class should be in das-common
 * 
 * @author matthew mariano
 *
 */
public class ESResponse {
	private ResponseEntity<String> response;
	private final Description description;
	private final HttpEntity<String> request;
	
	public ESResponse(HttpEntity<String> aRequest
			, ResponseEntity<String> aResponse){
		response=aResponse;
		description=null;
		request=aRequest;
	}
	public ESResponse(Description aDescription
			, HttpEntity<String> aRequest
			, ResponseEntity<String> aResponse
			){
		response=aResponse;
		description=aDescription;
		request=aRequest;
	}
	private long naId;
	/**
	 * @return the response
	 */
	public ResponseEntity<String> getResponse() {
		return response;
	}
	/**
	 * @return the naId
	 */
	public long getNaId() {
		return naId;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(ResponseEntity<String> response) {
		this.response = response;
	}
	/**
	 * @param naId the naId to set
	 */
	public void setNaId(long naId) {
		this.naId = naId;
	}
	/**
	 * @return the description
	 */
	public Description getDescription() {
		return description;
	}
	/**
	 * @return the request
	 */
	public HttpEntity<String> getRequest() {
		return request;
	}
	
}
