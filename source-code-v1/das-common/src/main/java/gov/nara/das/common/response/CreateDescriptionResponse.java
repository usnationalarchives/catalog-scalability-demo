package gov.nara.das.common.response;



public class CreateDescriptionResponse {
    private String status;
    private long   naid;
    private String error;
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @return the naid
	 */
	public long getNaid() {
		return naid;
	}
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @param naid the naid to set
	 */
	public void setNaid(long naid) {
		this.naid = naid;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
}
