package gov.nara.das.common.utils;

import java.io.Serializable;

import org.json.JSONObject;

public class JSONObjectWrapper extends JSONObject implements Serializable {
	public JSONObjectWrapper() {

	}

	public JSONObjectWrapper(String json) {
		super(json);
	}
}
