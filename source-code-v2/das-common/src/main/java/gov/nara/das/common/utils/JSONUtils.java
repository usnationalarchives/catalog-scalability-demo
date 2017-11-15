package gov.nara.das.common.utils;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static gov.nara.das.common.utils.Utils.*;

/**
 * @brief Methods and structures for navigating and manipulating JSON
 * 
 * @author Matthew Mariano
 * 
 * Change Log
 * ticket        date       contributor                   comments
 * NARATO5-59    2017-11-15 Matthew Mariano              :initial release
 *
 */
public class JSONUtils {
	static Logger log = LoggerFactory.getLogger(JSONUtils.class);
	public static class PathValueOrderComparator implements Comparator<PathValue> {
		int order = 1;
	 
		public PathValueOrderComparator(boolean orderAscending) {
			order = orderAscending ? 1 : -1;
		}

		@Override
		public int compare(PathValue o1, PathValue o2) {

			return order * Integer.compare(o1.index, o2.index);
		}

	}
	public static final String ARRAY = "A";
	public static final String BOOLEAN = "B";
	public static final String INTEGER = "I";
	public static final String NUMBER = "N";
	public static final String OBJECT = "O";
	public static final String STRING = "S";

	public static enum WALK_ACTIONS {
		ACCEPT(1), ACCEPT_THIS_ONLY(2), REJECT(3), CHANGE_TO_ARRAY(4);

		private int value;

		public int getValue() {
			return value;
		}

		WALK_ACTIONS(final int i) {
			this.value = i;
		}
	}
	public static class Index {
		int index = 0;
	}
	public static class PathValue {
		private int index = -1;
		private final Stack<Object> path;
		private final Object value;
		private Object parent0;
		private Object parent;
		public PathValue(Stack<Object> apath, Object avalue) {
			path = new Stack<Object>();
			apath.forEach(x -> {
				path.push(x);
			});
			value = avalue;
		}
		public PathValue(Object aparent0, Object aparent,Stack<Object> apath, Object avalue) {
			this(apath,avalue);
			parent0=aparent0;
			parent=aparent;
		}
		public Stack<Object> getPath() {
			return path;
		}

		public Object getValue() {
			return value;
		}

		public int getIndex() {
			return index;
		}

		public String toString() {
			return stackToStringPath(path, false);
		}
		public String getStringPath() {
			return stackToStringPath(path, false);
		}
		/**
		 * @return the parent0
		 */
		public Object getParent0() {
			return parent0;
		}

		/**
		 * @return the parent
		 */
		public Object getParent() {
			return parent;
		}

		/**
		 * @param parent0 the parent0 to set
		 */
		public void setParent0(Object parent0) {
			this.parent0 = parent0;
		}

		/**
		 * @param parent the parent to set
		 */
		public void setParent(Object parent) {
			this.parent = parent;
		}
	}

	/**
	 * 
	 * @param json
	 *            the text to format
	 * @param indent
	 *            - the indent to use in formatting
	 * @return the formatted text
	 */
	public static String formatJson(String json, int indent) {

		String s = json;
		try {
			s = new JSONObject(json).toString(indent);
		} catch (Exception e) {

		}
		if (s == null) {
			try {
				s = new JSONArray(json).toString(indent);
			} catch (Exception e) {

			}
		}

		return s;
	}

	public static void addAll(JSONArray from, JSONArray to) {
		if (from == null || to == null) {
			return;
		}
		for (int i = 0; i < from.length(); i++) {
			to.put(from.get(i));
		}
	}

	public static boolean isJSONObject(Object o) {
		return JSONObject.class.equals(o.getClass());
	}

	public static boolean isJSONArray(Object o) {
		return JSONArray.class.equals(o.getClass());
	}


	public static JSONArray getJSONArrayNull(String json) {
		JSONArray j = null;
		try {
			j = new JSONArray(json);
		} catch (Exception e) {
			// do nothing
		}
		return j;
	}

	public static JSONArray getJSONArrayNull(JSONObject parent, String path) {
		JSONArray j = null;
		try {
			Object o = getObject(parent, path);
			if (o != null && o.getClass().equals(JSONArray.class)) {
				return (JSONArray) o;
			}

		} catch (Exception e) {
			// do nothing
		}
		return j;
	}

	public static JSONObject getOrCreateJSONObject(JSONObject parent, String key) {
		JSONObject j = null;
		try {
			j = parent.getJSONObject(key);
		} catch (Exception e) {
			j = new JSONObject();
			parent.put(key, j);
		}
		return j;
	}

	/**
	 * 
	 * @param parent
	 * @param key
	 *            - the key to use
	 * @param defaultValue
	 *            - a correct JSON Object
	 * @return
	 */
	public static JSONObject getOrCreateJSONObject(JSONObject parent, String key, String defaultValue) {
		JSONObject j = null;
		try {
			j = parent.getJSONObject(key);
		} catch (Exception e) {
			j = new JSONObject(defaultValue);
			parent.put(key, j);
		}
		return j;
	}

	public static Dimension getOrCreatDimension(JSONObject parent, String key, Dimension defaultValue) {
		JSONArray a = getOrCreateJSONArray(parent, key);
		try {
			if (a.length() == 2) {
				int w = a.getInt(0);
				int h = a.getInt(1);
				return new Dimension(w, h);
			}
		} catch (Exception e) {

		}
		while (a.length() > 0) {
			a.remove(0);
		}
		a.put(0, defaultValue.getWidth());
		a.put(1, defaultValue.getHeight());
		return defaultValue;
	}

	public static List<String> getStringListFromArray(JSONObject parent, String key) {
		String[] a = getStringValuesFromArray(parent, key);
		List<String> list = new ArrayList<String>();
		Arrays.asList(a).forEach(x -> list.add(x));
		return list;
	}

	public static String[] getStringValuesFromArray(JSONObject parent, String key) {
		String[] data = new String[0];
		JSONArray a = (JSONArray) getObject(parent, key);
		return arrayToStringArray(a);

	}

	/**
	 * This doesn't support a path like widgets/0
	 * 
	 * @param parent
	 *            - the parent object
	 * @param key
	 *            - a String key to the array
	 * @return
	 */
	public static JSONArray getOrCreateJSONArray(JSONObject parent, String key) {
		return getOrCreateJSONArray(parent, key, "[]");
	}

	public static JSONArray getOrCreateJSONArray(JSONObject parent, String key, String defaultValue) {
		JSONArray j = null;
		try {
			j = parent.getJSONArray(key);
		} catch (Exception e) {
			j = new JSONArray(defaultValue);
			parent.put(key, j);
		}
		return j;
	}

	public static JSONObject getJSONObject(JSONArray parent, int i) {
		return parent.getJSONObject(i);
	}

	public static JSONObject getOrCreateJSONObject(JSONArray parent, Integer index) {

		return getOrCreateJSONObject(parent, index, "{}");
	}

	public static JSONObject getOrCreateJSONObject(JSONArray parent, Integer index, String defaultValue) {
		JSONObject j = null;
		for (int i = 0; i <= index; i++) {
			try {
				j = parent.getJSONObject(i);
			} catch (Exception e) {
				String s = i == index ? defaultValue : "{}";
				j = new JSONObject(s);
				parent.put(i, j);
			}
		}
		return j;
	}

	public static JSONArray getOrCreateJSONArray(JSONArray parent, Integer index) {
		JSONArray j = null;
		for (int i = 0; i <= index; i++) {
			try {
				j = parent.getJSONArray(i);
			} catch (Exception e) {
				j = new JSONArray();
				parent.put(i, j);
			}
		}
		return j;
	}

	public static JSONArray getOrCreateJSONArray(JSONArray parent, Integer index, String defaultValue) {
		JSONArray j = null;
		for (int i = 0; i <= index; i++) {
			try {
				j = parent.getJSONArray(index);
			} catch (Exception e) {
				String s = i == index ? defaultValue : "[]";
				j = new JSONArray(s);
				parent.put(i, j);
			}
		}
		return j;
	}

	public static Boolean getOrCreateBoolean(JSONObject parent, String key) {

		return getOrCreateBoolean(parent, key, false);
	}

	public static String getOrCreateString(JSONObject parent, String key) {

		return getOrCreateString(parent, key, "");
	}

	public static String getOrCreateString(JSONObject parent, String key, String defaultValue) {
		String s = new String(defaultValue);
		try {
			s = parent.getString(key);
		} catch (Exception e) {
			s = defaultValue;
			parent.put(key, s);
		}
		return s;
	}

	public static String getString(JSONArray parent, int index) {
		return parent.getString(index);
	}

	public static String getOrCreateString(JSONArray parent, Integer index, String defaultValue) {
		String j = null;
		for (int i = 0; i <= index; i++) {
			try {
				j = parent.getString(i);
			} catch (Exception e) {
				j = i == index ? defaultValue : "";
				parent.put(i, j);
			}
		}
		return j;
	}

	public static Number getOrCreateNumber(JSONObject parent, String key) {

		return getOrCreateNumber(parent, key, new Integer(0));
	}

	public static Number getOrCreateNumber(JSONObject parent, String key, Number defaultValue) {
		Number n = 0;
		try {
			if (defaultValue.getClass().equals(Integer.class)) {
				n = parent.getInt(key);
			} else if (defaultValue.getClass().equals(Double.class)) {
				n = parent.getDouble(key);
			}
		} catch (Exception e) {
			n = defaultValue;
			parent.put(key, n);
		}
		return n;
	}

	public static Integer getOrCreateInteger(JSONObject parent, String key, Integer defaultValue) {
		Integer j = null;
		try {
			j = parent.getInt(key);
		} catch (Exception e) {
			j = defaultValue;
			parent.put(key, defaultValue.intValue());
		}

		return j;
	}

	public static Integer getInteger(JSONArray parent, Integer index) {
		return parent.getInt(index);
	}

	public static Integer getOrCreateInteger(JSONArray parent, Integer index, Integer defaultValue) {
		Integer j = null;
		for (int i = 0; i <= index; i++) {
			try {
				j = parent.getInt(i);
			} catch (Exception e) {
				j = defaultValue;
				parent.put(i, defaultValue.intValue());
			}
		}
		return j;
	}

	public static Long getLong(Object parent, String path) {
		Object o= getObject(parent, path);
		
		if(o instanceof Integer){
			return (long)((Integer)o).intValue();
		}
		return (Long)o;
	}

	public static Long getLong(JSONArray parent, Integer index) {
		return parent.getLong(index);
	}

	public static Long getOrCreateLong(JSONObject parent, String key, Long defaultValue) {
		Long j = null;
		try {
			j = parent.getLong(key);
		} catch (Exception e) {
			j = defaultValue;
			parent.put(key, defaultValue.intValue());
		}

		return j;
	}

	public static Long getOrCreateLong(JSONArray parent, Integer index, Long defaultValue) {
		Long j = null;
		for (int i = 0; i <= index; i++) {
			try {
				j = parent.getLong(i);
			} catch (Exception e) {
				j = defaultValue;
				parent.put(i, defaultValue.intValue());
			}
		}
		return j;
	}

	/**
	 * 
	 * @param parent
	 *            - the parent to ssearch
	 * @param key
	 *            -key to a String value
	 * @param keyValue
	 *            -the String value pointed to by key
	 * @return the index of the found Object
	 */
	public static int indexOf(JSONArray parent, String key, String keyValue) {
		for (int i = 0; i < parent.length(); i++) {
			try {
				JSONObject j = parent.getJSONObject(i);
				if (keyValue.equals(j.getString(key))) {
					return i;
				}
			} catch (Exception e) {
				// ignore
			}
		}
		return -1;
	}

	public static Boolean getOrCreateBoolean(JSONObject parent, String key, Boolean defaultValue) {
		Boolean j = null;
		try {
			j = parent.getBoolean(key);
		} catch (Exception e) {
			j = defaultValue;
			parent.put(key, defaultValue.booleanValue());
		}

		return j;
	}

	public static Boolean getBoolean(JSONArray parent, Integer index) {
		return parent.getBoolean(index);
	}

	public static Boolean getOrCreateBoolean(JSONArray parent, Integer index, Boolean defaultValue) {
		Boolean j = null;
		for (int i = 0; i <= index; i++) {
			try {
				j = parent.getBoolean(i);
			} catch (Exception e) {
				j = i == index ? defaultValue : false;
				parent.put(i, j);
			}
		}
		return j;
	}

	// public static Number getOrCreateInteger(JSONObject parent, Integer key) {
	// Integer n=0;
	// try {
	//
	// } catch (Exception e) {
	// parent.put(key, n);
	// }
	// return n;
	// }
	public static String[] pathToArray(String path) {
		String[] array = path.split("/");
		return array;
	}

	public static List<String> pathToList(String path) {
		return Arrays.asList(pathToArray(path));
	}

	public static Queue pathToQueue(String path) {
		return pathToQueue(path, 0, -1);
	}

	public static Queue pathToQueue(String path, int start) {
		return pathToQueue(path, start, -1);
	}

	public static Queue pathToQueue(String path, int start, int end) {
		String[] pathPieces = pathToArray(path);
		final int end2;
		if (end == -1) {
			end2 = Integer.MAX_VALUE;
		} else {
			end2 = end;
		}
		Queue<String> q = new LinkedList<String>();
		class Index {
			int index = 0;
		}
		Index index = new Index();
		Arrays.asList(pathPieces).stream().forEach(x -> {
			if (index.index >= start && index.index < end2) {
				q.add(x);
			}
			index.index++;
		});
		return q;
	}

	public static String[] arrayToStringArray(JSONObject jobject, String key) {
		JSONArray jarray = getOrCreateJSONArray(jobject, key);
		return arrayToStringArray(jarray);
	}

	public static String[] arrayToStringArray(JSONArray jarray) {
		List<String> list = new ArrayList<String>();
		if (jarray != null) {
			for (int i = 0; i < jarray.length(); i++) {
				list.add("" + jarray.get(i));
			}
		}
		String[] a = list.toArray(new String[0]);
		return a;
	}

	public static JSONArray stringArrayToArray(String[] array) {
		JSONArray a = new JSONArray();
		if (array == null) {
			return a;
		}
		for (int i = 0; i < array.length; i++) {
			a.put(array[i]);
		}
		return a;
	}

	public static JSONArray stringArrayToArrayOrNull(String[] array) {
		return stringArrayToArrayOrNull(array, false);
	}

	public static JSONArray stringArrayToArrayOrNull(String[] array, boolean sort) {
		JSONArray a = new JSONArray();
		if (array == null) {
			return null;
		}
		if (sort) {
			Arrays.sort(array);
		}
		for (int i = 0; i < array.length; i++) {
			a.put(array[i]);
		}
		return a;
	}

	public static List<String> arrayToStringList(JSONArray jarray) {
		List<String> list = new ArrayList<String>();
		if (jarray == null) {
			return list;
		}
		for (int i = 0; i < jarray.length(); i++) {
			list.add(jarray.getString(i));
		}
		return list;
	}

	public static <T> List<T> arrayToList(JSONArray jarray) {
		List<T> list = new ArrayList<T>();
		if (jarray == null) {
			return list;
		}
		for (int i = 0; i < jarray.length(); i++) {
			list.add((T) jarray.get(i));
		}
		return list;
	}

	public static List<Object> arrayToObjectList(JSONArray jarray) {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < jarray.length(); i++) {
			list.add(jarray.get(i));
		}
		return list;
	}



	public static <T> List<T> arrayToList(JSONArray jarray, Function<JSONObject, T> creator) {
		List<T> list = new ArrayList<T>();
		if (jarray == null) {
			return list;
		}
		for (int i = 0; i < jarray.length(); i++) {
			list.add(creator.apply(jarray.getJSONObject(i)));
		}
		return list;
	}

	public static <T> JSONArray listToJSONArray(List<T> list) {
		JSONArray a = new JSONArray();
		if (list == null) {
			return a;
		}
		list.stream().forEach(x -> a.put(x.toString()));
		return a;
	}

	public static List<String> objectArrayToStringList(JSONArray jarray, String key, String defaultValue) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < jarray.length(); i++) {
			String s = getString(jarray.getJSONObject(i), key);
			s = s == null ? "" : s;
			list.add(s);
		}
		return list;
	}

	public static void setStringArrayData(JSONArray array, String[] data) {
		for (int i = 0; i < data.length; i++) {
			array.put(data[i]);
		}
	}

	public static Object getInt(Object parent, String path) {
		Integer n = null;
		Object o = getObject(parent, path);
		if (o == null) {
			return null;
		}
		if (o.getClass().equals(Number.class)) {
			n = Integer.parseInt("" + o);
			return n;
		}
		throw new RuntimeException("Expected a Number at path, " + path + ",but got a " + o.getClass().getName());
	}

	public static JSONArray getJSONArray(JSONArray parent, int index) {
		return parent.getJSONArray(index);
	}

	public static JSONArray getJSONArray(Object parent, String path) {
		return (JSONArray) getObject(parent, path);
	}

	public static JSONObject getJSONObject(Object parent, String path) {
		return (JSONObject) getObject(parent, path);
	}

	public static Boolean getBoolean(Object parent, String path) {
		return (Boolean) getObject(parent, path);
	}

	public static Integer getInteger(Object parent, String path) {
		return (Integer) getObject(parent, path);
	}

	public static String getString(Object parent, String path) {
		return (String) getObject(parent, path);
	}

	public static String getStringOrNullByPath(Object parent, String path) {
		String s = null;
		try {
			s = (String) getObject(parent, path);
		} catch (Exception e) {
			// ignore
		}
		return s;
	}

	public static JSONArray getArrayOrNull(Object parent, String path) {
		JSONArray s = null;
		try {
			s = (JSONArray) getObject(parent, path);
		} catch (Exception e) {
			// ignore
		}
		return s;
	}

	public static Boolean getBooleanOrNull(Object parent, String path) {
		Boolean s = null;
		try {
			s = (Boolean) getObject(parent, path);
		} catch (Exception e) {
			// ignore
		}
		return s;
	}

	/**
	 * 
	 * @param o
	 *            - JSONArray or JSONObject
	 * @throws ParseException
	 *             - if path is empty or doesn't exist in o
	 */
	public static void setObject(Object o, String path, Object value) throws ParseException {
		String[] a = path.trim().split("/");
		String parentPath = null;
		if (a.length > 0) {
			parentPath = flatten(a, "/", 0, a.length - 1);
		} else {
			throw new ParseException("path must not be empty",0);
		}
		Queue q = pathToQueue(parentPath);
		Object key = a[a.length - 1];
		Object parent = null;
		if (a.length == 1) {
			// a single value on the path is either a key to a JSONObject or an
			// index for a JSONArray
			parent = o;
		} else {
			parent = getObject(o, parentPath);
		}
		if (parent == null) {
			throw new ParseException("path does not exist in o",0);
		}
		try {
			if (JSONObject.class.equals(parent.getClass())) {
				((JSONObject) parent).put("" + key, value);
			} else {
				// must be JSONArray
				int index = Integer.parseInt("" + key);
				((JSONArray) parent).put(index, value);

			}
		} catch (Exception e) {
			throw new ParseException("path does not exist in o. " + e.getMessage(),0);
		}
	}

	public static Object getObject(Object parent, String path) {
		Queue q = pathToQueue(path);
		return getObject2(parent, q);
	}

	private static Object getObject2(Object parent, Queue q) {
		if (q.isEmpty()) {
			return parent;
		}
		Object key = q.remove();
		try {
			Object o = null;
			if (parent.getClass().equals(JSONObject.class)) {
				o = ((JSONObject) parent).get("" + key);
			} else if (parent.getClass().equals(JSONArray.class)) {
				o = ((JSONArray) parent).get(Integer.parseInt("" + key));
			}
			if (o != null) {
				return getObject2(o, q);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	// public static Object getOrCreateObject(Object parent, String path, String
	// pathSchema, Object defaultValue){
	// Queue<String> q1 = pathToQueue(path);
	// Queue<String> q2 = pathToQueue(pathSchema);
	// return getOrCreateObject2(parent, q1,q2,defaultValue);
	// }
	// private static Object getOrCreateObject2(Object parent, Queue<String> q1,
	// Queue<String> q2, Object defaultValue) {
	// if (q1.isEmpty()) {
	// return parent;
	// }
	// String key = q1.remove();
	// String type = q2.remove();
	// try {
	// Object o = null;
	// if(ARRAY.equals(type)){
	// o=getOrCreateJSONArray((JSONObject)parent, key);
	// }else if(BOOLEAN.equals(type)){
	// o=getOrCreateBoolean((JSONObject)parent, key,(Boolean)defaultValue);
	// }else if(INTEGER.equals(type)){
	// // only used for indices of array
	// o=getOrCreateInteger((JSONObject)parent, key);
	// }else if(NUMBER.equals(type)){
	// o=getOrCreateNumber((JSONObject)parent, key,(Number)defaultValue);
	// }else if(OBJECT.equals(type)){
	// o=getOrCreateJSONObject((JSONObject)parent, key);
	// }else if(STRING.equals(type)){
	// o=getOrCreateString((JSONObject)parent, key,(String)defaultValue);
	// }
	// if (parent.getClass().equals(JSONObject.class)) {
	// o = ((JSONObject) parent).get("" + key);
	// } else if (parent.getClass().equals(JSONArray.class)) {
	// o = ((JSONArray) parent).get(Integer.parseInt("" + key));
	// }
	// if (o != null) {
	// return getOrCreateObject2(o, q1,q2,defaultValue);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	public static void clearArray(JSONArray a) {
		while (a.length() > 0) {
			a.remove(0);
		}
	}

	public static JSONArray dimToArray(Dimension dim) {
		JSONArray a = new JSONArray();
		a.put(dim.getWidth());
		a.put(dim.getHeight());
		return a;
	}

	public static Dimension arrayToDim(JSONArray a) {
		Dimension d = new Dimension(a.getInt(0), a.getInt(1));
		return d;
	}

	public static <T> void forEachConsume(JSONArray array, Consumer<T> consumer) {
		if (array == null) {
			return;
		}
		int start = 0;
		int end = array.length();
		forEachConsume(array, consumer, start, end);
	}

	public static <T> void forEachConsume(JSONArray array, Consumer<T> consumer, int start, int end) {
		for (int i = start; i < end; i++) {
			consumer.accept((T) array.get(i));
		}
	}

	/**
	 * mm 2016-06-28 endpoints are primitive values(Boolean, String, Number,
	 * etc...) in other words everything outside of JSONObject or JSONArray.
	 * Every json path terminates in an endpoint or null.
	 * 
	 * @return
	 */
	public static Map<String, Object> getEndPointsMap(JSONObject parent) {
		Map<String, Object> map = new TreeMap<String, Object>();

		Consumer<PathValue> consumer = x -> {
			map.put(stackToStringPath(x.path), x.value);
		};
		Stack<Object> s = new Stack<Object>();
		s.push("ROOT");
		recurse(parent, s, consumer);
		return map;
	}

	/**
	 * mm 2016-06-28 endpoints are every path/value in the object. Sometimes the
	 * enddpoint is a JSONArray or JSONObject. In those cases the enpoint is in
	 * general nonterminnated. If the value is anything other than JSONObject or
	 * JSONArray , the endpoint is terminated. That is to say we reached the end
	 * of that path. We could refer to the latter as primitive endpoints.
	 * * @return a List of PathValue objects, each with an index for sorting
	 * with PathValuesOrderComparator. The index insures the parent is before
	 * the child
	 * 
	 * @see PathValuesOrderComparator
	 */
	public static List<PathValue> getEndPointsList(JSONObject parent) {
		return getEndPointsList(parent, false);
	}

	public static <T> List<T> jsonArrayToList(JSONArray array) {
		List<T> list = new ArrayList<T>();
		Consumer<T> consumer = x -> {
			list.add(x);
		};
		forEachConsume(array, consumer);
		return list;
	}

	/**
	 * 
	 * @param parent
	 * @param primitiveOnly
	 *            if true return only terminated endpoints aka those ending with
	 *            a primitive. So don't return all the parents.
	 * @return
	 */

	public static List<PathValue> getEndPointsList(Object parent, boolean primitiveOnly) {
		List<PathValue> list = new ArrayList<PathValue>();
		Index index = new Index();
		Consumer<PathValue> consumer = x -> {
			if (primitiveOnly) {
				boolean bad = isJSONObject(x.value) || isJSONArray(x.value);
				if (bad) {
					return;
				}
			}
			list.add(x);
			x.index = index.index++;
		};
		Stack<Object> s = new Stack<Object>();
		s.push("ROOT");
		recurse(parent, s, consumer);
		return list;
	}

	public static String pathStackToStringPath(Stack<Object> pathStack) {
		if (pathStack.size() == 0) {
			return "";
		}
		StringBuffer b = new StringBuffer();
		b.append(pathStack.elementAt(0));
		for (int i = 1; i < pathStack.size(); i++) {
			b.append("/");
			b.append(pathStack.elementAt(i));
		}
		return b.toString();
	}

	public static void recurse(Object parent, Stack<Object> pathStack, Consumer<PathValue> consumer) {
		consumer.accept(new PathValue(pathStack, parent));
		if (JSONObject.class.equals(parent.getClass())) {
			JSONObject jo = (JSONObject) parent;
			for (String key : jo.keySet()) {
				pathStack.push(key);
				recurse(jo.get(key), pathStack, consumer);
				pathStack.pop();
			}
		} else if (JSONArray.class.equals(parent.getClass())) {
			JSONArray ja = (JSONArray) parent;
			for (int i = 0; i < ja.length(); i++) {
				pathStack.push(i);
				recurse(ja.get(i), pathStack, consumer);
				pathStack.pop();
			}
		}
	}



	public static final String stackToStringPath(Stack<Object> stack) {
		return stackToStringPath(stack, true);
	}

	public static boolean deleteElementAtPath(Object arrayOrObject, String path) {
		List<String> list = pathToList(path);
		Object parent = null;
		if (list.size() == 0) {
			return false;
		} else if (list.size() == 1) {
			parent = arrayOrObject;
		} else {
			String parentPath = flatten(list, "/", 0, list.size() - 1);
			parent = getObject(arrayOrObject, parentPath);
		}
		if (parent == null) {
			return false;
		}
		String key = list.get(list.size() - 1);
		Object removed = null;
		if (parent.getClass().equals(JSONObject.class)) {
			removed = ((JSONObject) parent).remove(key);
		} else {
			removed = ((JSONArray) parent).remove(Integer.parseInt(key));
		}
		return removed != null;
	}

	/**
	 * 
	 * @param stack
	 * @param includeROOT
	 *            - if true and the zeroeth element is exactly ROOT then it is
	 *            included if true and the zeroeth element is not ROOT it is
	 *            still included if false and ROOT it is not included if false
	 *            and not ROOT it is included
	 * @return
	 */
	public static final String stackToStringPath(Stack<Object> stack, boolean includeROOT) {
		StringBuffer b = new StringBuffer();
		for (int i = 0; i < stack.size(); i++) {
			if (b.length() > 0) {
				b.append("/");
			}
			if (i == 0 && includeROOT == false && stack.elementAt(i).equals("ROOT")) {
				continue;
			}
			b.append("" + stack.elementAt(i));
		}
		return b.toString();
	}
	public static String removeNullValues(String json){
		return removeNullValues(json,true,true);
	}
	public static String removeNullValues(String json, boolean removeEmptyArrays, boolean removeEmptyStrings){
		JSONObject j=new JSONObject(json);
		List<PathValue> list=getEndPointsList(j, false);
		Collections.sort(list, new PathValueOrderComparator(false));
		list.stream().forEach(pv->{
			println(pv.toString());
			String s=pv.getStringPath();
			int i=s.lastIndexOf('/');
			Object parent=null;
			if(i > 0){
				String parentPath=s.substring(0, i);
				parent=getObject(j,parentPath);
			}
			if(parent==null){
				parent=j;
			}

			Object okey=pv.path.get(pv.path.size()-1);
			boolean remove=false;
			if(pv.value !=null && (pv.value instanceof Long || pv.value instanceof Integer)){
				if(Long.parseLong(""+pv.value)==0L){
					remove=true;
				}
			}
			if("digitalObjectAuthorityList".equals(okey)){
				boolean isarray=pv.value instanceof JSONArray;
				log.debug("found digitalObjectAuthorityList isarray="+isarray + " pv.value="+pv.value);
			}
			if(removeEmptyArrays && pv.value instanceof JSONArray){
				remove=((JSONArray)pv.value).length()==0;
				log.debug("checking array:"+okey+" remove=" + remove+ "length="+((JSONArray)pv.value).length());
			}
			if(removeEmptyStrings && pv.value instanceof String){
				remove=((String)pv.value).length()==0;
			}
			if( remove || pv.value==null || (""+pv.value).equals("null")){
				if(parent instanceof JSONObject){
					((JSONObject) parent).remove(""+okey);
				}else if(parent instanceof JSONArray){
					println("got here"+pv);
					((JSONArray) parent).remove(Integer.parseInt(""+okey));
				}else{
					//TBD log error
				}
			}
		});
		return j.toString(3);
	}
	public static String createValidJSONObjectStringSafely(String json){
		JSONObject j=null;
		try{
			if(json==null){
				json="{}";
			}
			j=new JSONObject(json);
		}catch(Exception e){
			j=new JSONObject();
		}
		return j.toString(3);
	}
	public static String createValidJSONArrayAsString(String json){
		JSONArray j=null;
		try{
			if(json==null){
				json="[]";
			}
			j=new JSONArray(json);
		}catch(Exception e){
			j=new JSONArray();
		}
		return j.toString(3);
	}
	public static String getStringSafely(JSONObject j, String key, String defaultValue){
		String t=defaultValue;
		try{
			t=j.getString(key);
		}catch(Exception e){
			
		}
		return t;
	}
	public static Boolean getBooleanSafely(JSONObject j, String key, Boolean defaultValue){
		Boolean t=defaultValue;
		try{
			t=j.getBoolean(key);
		}catch(Exception e){
			
		}
		return t;
	}
	public static Long getLongSafely(JSONObject j, String key, Long defaultValue){
		Long t=defaultValue;
		try{
			t=j.getLong(key);
		}catch(Exception e){
			
		}
		return t;
	}
	public static Integer getIntegerSafely(JSONObject j, String key, Integer defaultValue){
		Integer t=defaultValue;
		try{
			t=j.getInt(key);
		}catch(Exception e){
			
		}
		return t;
	}
	public static JSONObject getJSONObjectSafely(JSONObject j, String key, JSONObject defaultValue){
		JSONObject t=null;
		try{
			if(defaultValue!=null){
				t=new JSONObject(defaultValue.toString());
			}
			t=j.getJSONObject(key);
		}catch(Exception e){
			
		}
		return t;
	}
}
