package gov.nara.das.common.elastic;

import org.json.JSONObject;

import gov.nara.das.common.db.das.Description;

public class ElasticSearchUtils {

	/**
	 * 
	 * @param description
	 *            - the description to convert
	 * @return the JSON to POST to ElasticSearch
	 */
	public static String convertToElasticSearchJSON(Description description) {
		// {
		// "naid" : "999991",
		// "title" : "Matt 1",
		// "dataControlGroup" : {
		// "groupCd" : "RDEP",
		// "groupId" : "ou=NWME,ou=groups"
		// }
		// }
		JSONObject j = new JSONObject();
		j.put("naId", description.getDescNaid());
		j.put("title", description.getTitle());
		try {
			JSONObject dcg = new JSONObject(description.getDataControlGroup());
			j.put("dataControlGroup", dcg);
		} catch (Exception e) {
			// do nothing
		}
		return j.toString(3);
	}
	// public static String doubleQuote(Object s){
	// return "\""+s+"\"";
	// }
}
