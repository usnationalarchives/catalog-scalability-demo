package gov.nara.das.common.utils;

import java.io.Serializable;

import org.json.JSONArray;

public class JSONArrayWrapper extends JSONArray implements Serializable {
	public JSONArrayWrapper(){
		
	}
	public JSONArrayWrapper(String json){
		super(json);
	}
}
