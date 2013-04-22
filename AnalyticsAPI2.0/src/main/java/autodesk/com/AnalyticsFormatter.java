package autodesk.com;

import java.util.Date;
import java.util.TreeMap;

import com.google.gson.Gson;

//TODO Ask tony about the JSON format
public class AnalyticsFormatter {

//----------------------------------  Fields -------------------------------------------------
	
 private TreeMap<String, String> logAttrs = new TreeMap<String, String>();


//----------------------------------  Constructors -------------------------------------------------

	/**
	 * Create an instance of Analytics Writer class.
	 * @author leandro.mora
	 * @version 2.0
	 * @return AnalyticsWriter instance
	 */
	public AnalyticsFormatter() {
	}
	/**
	 * Create an instance of Analytics Writer class. It uses the String format as category
	 * @return AnalyticsWriter instance
	 * @author leandro.mora
	 * @version 2.0
	 */
	public AnalyticsFormatter(String category) {
		if(!category.isEmpty()){
			logAttrs.put("CATEGORY", category);
		}
	}

//----------------------------------  Methods -------------------------------------------------

	/**
	 * Associates the specified value with the specified key. If the key does already exist, the old value is replaced
	 * @param key key with which the specified value is to be associated. Only the keys defined in enumeration Key can be put.
	 * @param value value to be associated with the specified key
	 * @version 2.0
	 * @return the current AnalyticsWriter instance
	 */
	public synchronized AnalyticsFormatter put(String key, String value) {
		if(!(key.isEmpty()) & !(value.isEmpty())){
			logAttrs.put(key, value);
		}
		return this;
	}
	
//----------------------------------
	
	/**
	 * Removes a value associated with an specified key. 
	 * @param key with which the specified value is to be associated
	 * @return the current AnalyticsWriter instance
	 * @version 2.0
	 * @author leandro.mora
	 */
	public synchronized AnalyticsFormatter del(String key) {
		if(!key.isEmpty()){
			logAttrs.remove(key);
		}
		return this;
	}
	
//----------------------------------	
	
	private String getTimeStamp() {
		
		return String.format("%d", new Date().getTime());
	}
	
//----------------------------------

	private String formatAttributesAsJason() {

		logAttrs.put("ADSK_REQ_DATE", this.getTimeStamp());
		String json = "";
		json = new Gson().toJson(logAttrs);
		return json;
	}
	
//----------------------------------
	
	/**
	 * Generate a string which includes all the attributes of the instance in a JSON format
	 * @return String
	 */
	public String outputEvent() {
		
		return formatAttributesAsJason();
	}
}
