package com.globant.akn;

import java.util.Date;
import java.util.EnumMap;

import com.globant.akn.KeyPair.Key;
import com.google.gson.Gson;

//TODO Ask tony about the JSON format
public class AnalyticsFormatter {

//----------------------------------  Fields -------------------------------------------------
	
	private EnumMap<Key, String> logAttrs = new EnumMap<Key, String>(Key.class);


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
		logAttrs.put(Key.CATEGORY, category);
	}

//----------------------------------  Methods -------------------------------------------------

	/**
	 * Associates the specified value with the specified key. If the key does already exist, the old value is replaced
	 * @param key key with which the specified value is to be associated. Only the keys defined in enumeration Key can be put.
	 * @param value value to be associated with the specified key
	 * @version 2.0
	 * @return the current AnalyticsWriter instance
	 */
	public AnalyticsFormatter put(Key key, String value) {

		logAttrs.put(key, value);
		return this;
	}
	
//----------------------------------
	
	/**
	 * Add all of elements in the specified KeyPair array to AnalyticsWriter. If the key does already exist, the old value is replaced.
	 * @param keyArray the array with KeyPair instances
	 * @return the current AnalyticsWriter instance
	 * @version 2.0
	 * @author leandro.mora
	 */
	public synchronized AnalyticsFormatter put(KeyPair[] keyArray) {

		for (int i = 0; i < keyArray.length; ++i) {

			KeyPair keyPair = keyArray[i];
			logAttrs.put(keyPair.getKey(), keyPair.getValue());
		}
		return this;
	}
	
//----------------------------------
	
	/**
	 * Removes the specified value with the specified key. 
	 * @param key with which the specified value is to be associated
	 * @return the current AnalyticsWriter instance
	 * @version 2.0
	 * @author leandro.mora
	 */
	public synchronized AnalyticsFormatter del(Key key) {

		logAttrs.remove(key);
		return this;
	}
	
//----------------------------------	
	
	private String getTimeStamp() {
		
		return String.format("%d", new Date().getTime());
	}
	
//----------------------------------

	private String formatAttributesAsJason() {

		logAttrs.put(Key.ADSK_REQ_DATE, this.getTimeStamp());
		String json = "";
		json = new Gson().toJson(logAttrs);
		return json;
	}
	
//----------------------------------
	
	/**
	 * Generate a string which includes all the attributes of the instance in a JSON format
	 * @return String
	 */
	public String output() {
		
		return formatAttributesAsJason();
	}
}
