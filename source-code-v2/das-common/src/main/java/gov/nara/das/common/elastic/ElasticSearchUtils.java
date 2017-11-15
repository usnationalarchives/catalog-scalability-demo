package gov.nara.das.common.elastic;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import gov.nara.das.common.db.das.Description;
import static gov.nara.das.common.utils.JSONUtils.*;
import static gov.nara.das.common.utils.Utils.*;
/**
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class ElasticSearchUtils {

	/**
	 * 
	 * @param description
	 *            - the description to convert
	 * @return the JSON to POST to ElasticSearch
	 */
	public static String convertToElasticSearchJSON(Description description) {
		
		String json=null;
		
		// the description as a JSONObject with zeros and nulls removed
		JSONObject cleanedDescriptionJSONObject=null;
		
		// the final JSONObject whose String representation is returned
		JSONObject esJSONObject=new JSONObject();
		
		//
		JSONObject dataControlGroup=null;
		long parentRecordGroupNumber=0;
		String accessRestrictionTermname=null;
		String dataControlGroupGroupCd=null;
		try {
			json=convertDescriptionToJSON(description);
		} catch (Exception e) {
			// do nothing
		}
		
		json=json.replaceAll("descNaId","naId");
		json=removeNullValues(json);
		
		
		cleanedDescriptionJSONObject=new JSONObject(json);
		//
		esJSONObject.put("naId", cleanedDescriptionJSONObject.getLong("naId"));
		esJSONObject.put("title", cleanedDescriptionJSONObject.getString("title"));
		esJSONObject.put("level", cleanedDescriptionJSONObject.getString("descType"));
		//
		// put the full description into the output with the key description
		esJSONObject.put("description", new JSONObject(cleanedDescriptionJSONObject.toString(3)));

		if(description.getParentRecordGroup()!=null ){
			parentRecordGroupNumber=description.getParentRecordGroup().getRecordGpNo();
			esJSONObject.put("parentRecordGroup", parentRecordGroupNumber);
		}
		if(description.getAccessRestriction() !=null && description.getAccessRestriction() != null){
			accessRestrictionTermname=description.getAccessRestriction().getTermName();
			esJSONObject.put("accessRestriction", accessRestrictionTermname);
		}
		if(description.getDataCtlGp() != null ){
			try{
				dataControlGroup=new JSONObject(description.getDataCtlGp());
				dataControlGroupGroupCd=dataControlGroup.getString("groupCd");
				esJSONObject.put("dataControlGroup", dataControlGroupGroupCd);
			}catch(Exception e){
				
			}
		}
		json=esJSONObject.toString(3);
		return json;
	}
	//
	public static void getAndPutString(String key,String newKey,JSONObject j1,JSONObject j2){
		try{
			String j=j1.getString(key);
			j2.put(newKey, j);
		}catch(Exception e){
			// do nothing
		}
	}
	
	public  static void  getAndPutString(String key,JSONObject j1,JSONObject j2){
		try{
			j2.put(key,j1.getString(key));
		}catch(Exception e){
			// do nothing
		}
	}
	//
	public  static void  getAndPutLong(String key,JSONObject j1,JSONObject j2){
		try{
			j2.put(key,j1.getLong(key));
		}catch(Exception e){
			// do nothing
		}
	}
	public  static void  getAndPutObject(String key,String newKey,JSONObject j1,JSONObject j2){
		try{
			JSONObject j=j1.getJSONObject(key);
			j2.put(newKey, j);
		}catch(Exception e){
			// do nothing
		}
	}
	
	public  static void  getAndPutObject(String key,JSONObject j1,JSONObject j2){
		try{
			j2.put(key,j1.getJSONObject(key));
		}catch(Exception e){
			// do nothing
		}
	}
}
