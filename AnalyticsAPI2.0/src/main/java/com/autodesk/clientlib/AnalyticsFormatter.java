package com.autodesk.clientlib;

import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.autodesk.clientlib.KeyPair.Key;
import com.google.gson.Gson;

/**
 * Class used for containing different attributes and generate with that information
 *  an output String in a Jason Format.
 * @version 2.0.3
 * @author leandro.mora
 *
 */
public class AnalyticsFormatter {
	
//----------------------------------  Fields -------------------------------------------------
	 
	 private static java.lang.ThreadLocal<AnalyticsFormatter>    threadContext = new java.lang.ThreadLocal<AnalyticsFormatter>(); 
	 private TreeMap<String, String> logAttrs = new TreeMap<String, String>();
	 private EnumMap<Key, String> logEnumAttrs=new EnumMap<Key,String>(Key.class);
	 public static final String ERROR_MESSAGE="NOT FOUND";
	 
//----------------------------------  Constructors -------------------------------------------------
	 
	 /**
	 * Create an instance of Analytics Writer class.
	 * @author leandro.mora
	 * @return AnalyticsWriter instance
	 * @version 2.0
	 * @author t_moral
	 */
	public AnalyticsFormatter() {
	}
	
//----------------------------------  Statics Getters And Setters -------------------------------------------------
	
	private static AnalyticsFormatter  getCurrentThreadContext() { 
		return threadContext.get(); 		
	}

	//----------------------------------	
	private static void setCurrentThreadContext( AnalyticsFormatter   myAnalytics ) {
		threadContext.set(myAnalytics); 
	}

	//----------------------------------	
	/**
	 * In case there isn't a Analytics Formatter object associated with a thread local variable, it creates it and associates it with the thread local variable.
	 * In case it is already created, it returns the analytics formatter object.
	 * @return AnalyticsFormatter object
	 * @author t_moral
	 */
	public static synchronized AnalyticsFormatter getInstance(){
		AnalyticsFormatter returnInstance=getCurrentThreadContext();
		if(returnInstance==null){
			returnInstance=new AnalyticsFormatter();
			setCurrentThreadContext(returnInstance);
		}
		return returnInstance;
	}
	
//----------------------------------  Methods -------------------------------------------------
	
	/**
	 * Associates the specified value with the specified key. If the key does already exist, the old value is replaced
	 * @param a Key instance with which the specified value is to be associated. Only the keys defined in enumeration Key can be put.
	 * @param value value to be associated with the specified key
	 * @return the current AnalyticsWriter instance
	 * @version 2.0
	 * @author t_moral
	 */
	public synchronized AnalyticsFormatter put(Key key, String value) {
		if( (key!=null) && !(value.isEmpty()) ){
			logEnumAttrs.put(key, value);
		}
		return this;
	}

//----------------------------------
	/**
	 * Associates the specified value with the specified key. If the key does already exist, the old value is replaced
	 * @param a String key with which the specified value is to be associated. We recommend to use pass a Static String variable insted of an ordinary string.
	 * @param value value to be associated with the specified key
	 * @return the current AnalyticsWriter instance
	 * @version 2.0
	 * @author t_moral
	 */
	public synchronized AnalyticsFormatter put(String key, String value) {
		if(!(key.isEmpty()) & !(value.isEmpty())){
			logAttrs.put(key, value);
		}
		return this;
	}

	//----------------------------------
		/**
		 * Get information from a HTTP object passed as parameter.Put that information into the Analytic Formatter Object
		 * @param a http Request
		 * @return the current AnalyticsWriter instance
		 * @version 2.0
		 * @author t_moral
		 */
		public synchronized AnalyticsFormatter put(HttpServletRequest request) {
			if(request.getSession()!=null){
				logEnumAttrs.put(Key.CONTEXT_SESSION, request.getSession().toString());
			}
			if(request.getMethod()!=null){
				logEnumAttrs.put(Key.API_METHOD, request.getMethod());
			}
			return this;
		}
//----------------------------------
	/**
	 * Associates the specified value with the specified key.
	 * @param a String key with which the specified value is to be associated.
	 * @version 2.0
	 * @return  value value to be associated with the specified key
	 * @author t_moral
	 */
	public synchronized String get(Key key) {
		return logEnumAttrs.get(key);
	}

//----------------------------------
	/**
	* Returns true in case the key is stored into the analytics formatter object.In other case, it returns false.
	* @param an object key with which the specified value should be associated.
	* @version 2.0
	* @return  a boolean value
	* @author t_moral
	*/
	public synchronized boolean hasKey(Key key) {
		boolean returnedValue=false;
		if( (key!=null) && (logEnumAttrs.get(key)!=null) ){
			returnedValue=true;
		}
		return returnedValue;
	}

//----------------------------------
	/**
	* Get a value with a key passed as parameter.
	* @param a String key with which the specified value is to be associated.
	* @return value value to be associated with the specified key
	* @version 2.0
	* @author t_moral
	*/
	public synchronized String get(String key) {
		return logAttrs.get(key);
	}

//----------------------------------

	/**
	* Returns true in case the key is stored into the analytics formatter object.In other case, it returns false.
	* @param a String key with which the specified value should be associated.
	* @version 2.0
	* @return  a boolean value
	* @author t_moral
	*/
	public synchronized boolean hasKey(String key) {
		boolean returnedValue=false;
		if( (key!=null) && (logAttrs.get(key)!=null) ){
			returnedValue=true;
		}
		return returnedValue;
	}
//----------------------------------

	private String formatAttributesAsJason() {

		String json = "";
		logAttrs=this.mixMaps(logEnumAttrs, logAttrs);
		json = new Gson().toJson(logAttrs);
		return json;
	}
	
//----------------------------------
	
	private TreeMap<String,String> mixMaps(EnumMap<Key, String> enumMap,TreeMap<String,String> treeMap){
		
		for (Map.Entry<Key, String> entry : enumMap.entrySet())
		{
		    treeMap.put(entry.getKey().toString().toLowerCase(), entry.getValue());
		}
		return treeMap;
	}
//----------------------------------
	
	/**
	 * Generate a String which includes all the attributes of the instance in a JSON format
	 * @return String
	 */
	public String outputEvent() {
		
		return formatAttributesAsJason();
	}
}
