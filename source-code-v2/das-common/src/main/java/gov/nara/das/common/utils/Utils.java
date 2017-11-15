package gov.nara.das.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream.PutField;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static gov.nara.das.common.utils.JSONUtils.*;

import gov.nara.das.common.db.das.Description;
import gov.nara.das.common.db.search.Search;
import gov.nara.das.common.response.SearchRecordResponse;

import static gov.nara.das.common.db.das.DescriptionJSONB.DESCRIPTION_RESPONSE_JSONB_PATHS;

/**
 * @brief common util functions
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class Utils {
	static Logger log = LoggerFactory.getLogger(Utils.class);

	public static void println(Object... a) {
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
	}

	public static final String readFully(InputStream is) throws IOException {
		byte b = 0;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		while (true) {
			int i = is.read();
			if (i < 0) {
				break;
			}
			b = (byte) i;
			bout.write(b);
		}
		return bout.toString();
	}

	public static String date2String(long date) {
		return date2String(date,"yyyy-MM-dd HH:mm:ss");
	}
	public static String date2String(long date,String format) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}
	public static String stackTraceToString(Exception e) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(bo);
		e.printStackTrace(ps);
		ps.close();
		return bo.toString();
	}

	public static String convertSearchRecordResponseToJSON(SearchRecordResponse srr){
		SearchRecordResponse clone=new SearchRecordResponse(srr);
		if(srr.getSearch()==null){
			return convertToJSON(srr);
		}
		log.debug("SearchRecordResponse before="+convertToJSON(srr));
		Search searchClone=new Search(srr.getSearch());
		String searchJSON=srr.getSearch().getSearchJSON();
		searchClone.setSearchJSON("");
		SearchRecordResponse srrNew=new SearchRecordResponse();
		srrNew.setSearch(searchClone);
		String srrJSON=convertToJSON(srrNew);
		JSONObject srrObject=new JSONObject(srrJSON);
		log.debug("SearchRecordResponse JSONObject="+srrObject);
		log.debug("SearchRecordResponse json="+srrObject.toString(3));
		JSONObject j2=srrObject.getJSONObject("search");
		log.debug("searchJSON="+searchJSON);
		j2.put("searchJSON", new JSONObject(searchJSON));
		return srrObject.toString(3);
	}

	public static String convertSearchToJSON(Search s) {
		JSONObject j = new JSONObject();
		String json2 = s.getSearchJSON();
		if (json2 != null) {
			JSONObject j2 = new JSONObject(json2);
			j.put("searchJSON", j2);
		}
		j.put("searchCreatedUser", s.getSearchCreatedUser());
		String json = j.toString(3);
		return json;
	}

	/**
	 * 
	 * @param d - the description to be converted to JSON
	 * @return json
	 */
	public static String convertDescriptionToJSON(Description d) {
		String json = convertToJSON(d);
		try {
			JSONObject parent0 = new JSONObject(json);
			List<PathValue> list = getEndPointsList(parent0, false);
			list.sort(new PathValueOrderComparator(false));
			//
			Consumer<PathValue> consumer = pv -> {

				String sp = pv.getStringPath();
				boolean found=sp.endsWith("authDetails") || sp.endsWith("approvalHistory")
						||sp.endsWith("inclusiveStartDate") ||sp.endsWith("inclusiveEndDate") 
						||sp.endsWith("coverageStartDate") ||sp.endsWith("coverageEndDate") 
						||sp.endsWith("dataCtlGp") 
						||sp.endsWith("broadcastDate") 
						||sp.endsWith("productionDate") ;

				if (found) {
					try {
						int i = sp.lastIndexOf("/");
						String suffix = sp.substring(i + 1, sp.length());
						Object parent=null;
						if(i == -1){
							parent=parent0;
						}else{
							String pp = sp.substring(0, i);
							parent = getObject(parent0, pp);
						}
						log.debug("suffix="+suffix);
						Object j2=null;
						if (pv.getValue() != null && pv.getValue() instanceof String) {
							String value=("" + pv.getValue()).trim();
							if (value.startsWith("{")) {
								log.debug("2suffix="+suffix+", value="+value);
								j2 = new JSONObject("" + value);
							} else if(value.startsWith("[")){
								j2 = new JSONArray("" + pv.getValue());
							}
							if(j2!=null){
								setObject(parent, suffix, j2);
							}else{
								log.debug("skipped bad json expression="+value);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						log.debug(stackTraceToString(e));
					}
				}
			};
			//
			list.stream().forEach(consumer);
			//
//			if (parent0.getString("dataCtlGp") != null) {
//				parent0.put("dataCtlGp", new JSONObject(parent0.getString("dataCtlGp")));
//			}
			json = parent0.toString(3);

		} catch (Exception e) {
			log.debug("Exception caught parsing json. the original json will be returned.");
			log.debug(stackTraceToString(e));
		}
		return json;
	}

	public static <T> String convertToJSON(T t) {
		ObjectMapper om = new ObjectMapper();
		String json = null;
		try {
			json = om.writerWithDefaultPrettyPrinter().writeValueAsString(t);
		} catch (Exception e) {
			log.debug(stackTraceToString(e));
		}
		return json;
	}

	public static <T> String flatten(T[] list, String delim, int start, int end) {
		String f = "";
		if (list.length == 0) {
			return f;
		}

		for (int i = start; i < end; i++) {
			if (i > start) {
				f = f + delim;
			}
			f = f + list[i];
		}
		return f;
	}

	public static <T> String flatten(List<T> list, String delim, int start, int end) {
		String f = "";
		if (list.size() == 0) {
			return f;
		}

		for (int i = start; i < end; i++) {
			if (i > start) {
				f = f + delim;
			}
			f = f + list.get(i);
		}
		return f;
	}
}
