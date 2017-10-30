package gov.nara.das.common.db.ingest;

public class JobActionType {
	private int id;
	private String action;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	
}
