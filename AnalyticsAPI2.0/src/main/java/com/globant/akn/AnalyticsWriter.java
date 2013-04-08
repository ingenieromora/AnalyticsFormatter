package com.globant.akn;

import java.util.EnumMap;
import com.globant.akn.KeyPair.Key;
import com.globant.akn.KeyPair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//TODO Ask tony about the JSON format
public class AnalyticsWriter {

//----------------------------------  Fields -------------------------------------------------
	
	private KeyFormat keyFormat;
	private EnumMap<Key, String> logAttrs = new EnumMap<Key, String>(Key.class);

	public static final KeyFormat DEFAULT_KEY_FORMAT = KeyFormat.STRING;

	public enum KeyFormat {

		STRING,  // output key names as a string
		NUMERIC; // output key names as an numeric from Key enum mapping
	}

//----------------------------------  Constructors -------------------------------------------------

	/**
	 * Create an instance of Analytics Writer class. It uses the String format as Key Format.
	 * @author leandro.mora
	 * @version 2.0
	 * @return AnalyticsWriter instance
	 */
	public AnalyticsWriter() {
		this.setKeyFormat(DEFAULT_KEY_FORMAT);
	}
//----------------------------------
	/**
	 * Create an instance of Analytics Writer class specifying which keyFormat will be used.
	 * @author leandro.mora
	 * @version 2.0
	 * @return AnalyticsWriter instance
	 */
	public AnalyticsWriter(KeyFormat keyFormat) {

		this.setKeyFormat(keyFormat);
	}
	
//----------------------------------  Getter and Setters -------------------------------------------------

	/**
	 * Sets the key format to be string or numeric format
	 * @param keyFormat the key format in which the log will be recorded, STRING or NUMERIC.
	 * @author leandro.mora
	 * @version 2.0
	 */
	public void setKeyFormat(KeyFormat keyFormat) {

		this.keyFormat = keyFormat;
	}
//----------------------------------
	/**
	 * Returns current key format, STRING or NUMERIC
	 * @return current key format to log data
	 */
	public KeyFormat getKeyFormat() {

		return this.keyFormat;
	}

//----------------------------------  Methods -------------------------------------------------

	/**
	 * Associates the specified value with the specified key. If the key does already exist, the old value is replaced
	 * @param key key with which the specified value is to be associated. Only the keys defined in enumeration Key can be put.
	 * @param value value to be associated with the specified key
	 * @return the current AnalyticsWriter instance
	 */
	public AnalyticsWriter put(Key key, String value) {

		logAttrs.put(key, value);
		return this;
	}
//----------------------------------
	/**
	 * Add all of elements in the specified KeyPair array to AnalyticsWriter. If the key does already exist, the old value is replaced.
	 * @param keyArray the array with KeyPair instances
	 * @return the current AnalyticsWriter instance
	 */
	public AnalyticsWriter put(KeyPair[] keyArray) {

		for (int i = 0; i < keyArray.length; ++i) {

			KeyPair keyPair = keyArray[i];
			logAttrs.put(keyPair.getKey(), keyPair.getValue());
		}
		return this;
	}
//----------------------------------
	/**
	 * Removes all of the elements from AnalyticsWriter
	 * @return the current AnalyticsWriter instance
	 */
	public AnalyticsWriter clear() {

		logAttrs.clear();
		return this;
	}
//----------------------------------
	/**
	 * Generate a string which includes all the attributes of the instance in a JSON format
	 * @return String
	 */
	@Override
	public String toString() {

		return formatAttributesAsJason();
	}
//----------------------------------	
	private String formatAttributesAsJason() {

//TODO Ask Tony about the ADSK timeStamp
//		logAttrs.put(Key.ADSK_REQ_DATE, this.getTimeStamp());

		String json = "";
		switch (this.getKeyFormat()) {

		case STRING:
			
			json = new Gson().toJson(logAttrs);
			break;

		case NUMERIC:
			
			GsonBuilder builder = new GsonBuilder();
			builder.registerTypeAdapter(EnumMap.class, new LogAttrMapSerializer());
			json = builder.create().toJson(logAttrs);
			break;

		default:
			//TODO ASK Tony, what to do in case the format is wrong
			//logger.error(ERROR_UNEXPECTED_KEYFORMAT);
		}
		return json;
	}

}
