package gov.nara.das.common.utils;

import static gov.nara.das.common.utils.JSONUtils.removeNullValues;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import gov.nara.das.common.db.das.Description;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class ResponseUtils {
	public static String convertToResponseJSON(Description description) {
		ObjectMapper om=new ObjectMapper();
		String json=null;
		JSONObject dcg=null;
		try {
			json=om.writerWithDefaultPrettyPrinter().writeValueAsString(description);
			dcg = new JSONObject(description.getDataCtlGp());
		} catch (Exception e) {
			// do nothing
		}
		
		json=json.replaceAll("descNaId","naId");
		json=removeNullValues(json);

		JSONObject j=new JSONObject(json);

		
		// put dcg
		if(dcg != null){
			j.put("dataCtlGp", dcg);
		}
		//
		json=j.toString(3);
		return json;
	}
}
