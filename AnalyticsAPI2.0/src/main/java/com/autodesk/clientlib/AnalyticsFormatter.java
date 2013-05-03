package com.autodesk.clientlib;

import java.util.ArrayList;
import java.util.Enumeration;
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
		if( (key!=null) && (value!=null) && !(value.isEmpty()) ){
			logAttrs.put(key.getValue(), value);
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
		if(!(key.isEmpty()) && (value!=null) && !(value.isEmpty()) ){
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
			
			//Context Parameters
			put(Key.CONTEXT_TENANT, request.getHeader("x-ads-ctx-tenant"));
			put(Key.CONTEXT_USER, request.getHeader("x-ads-ctx-user"));
			put(Key.CONTEXT_SESSION, request.getHeader("x-ads-ctx-session"));
			put(Key.CONTEXT_JOB, request.getHeader("x-ads-ctx-job"));
			put(Key.CONTEXT_IDENTITY, request.getHeader("x-ads-ctx-identity"));
			put(Key.CONTEXT_CALL, request.getHeader("x-ads-ctx-call"));

			//HTTP Facets Parameters
			put(HttpFacetKeys.VERSION,"1.0.0");
			put(HttpFacetKeys.METHOD,request.getMethod());
			put(HttpFacetKeys.REMOTEIP,request.getRemoteAddr());//TODO ASK TONY
			put(HttpFacetKeys.URL,request.getRequestURL().toString());
			put(HttpFacetKeys.REQUEST_LEN,Integer.toString(request.getContentLength()));
			this.getHeaders(request);
			this.getFormParameters(request);

			return this;			
		}
		
		private void getFormParameters(HttpServletRequest request) {
			@SuppressWarnings("unchecked")
			Map<String,String> e = request.getParameterMap();
			for (Map.Entry<String,String> entry : e.entrySet())
			    	put(entry.getKey(),entry.getValue());			 
		}


		//TEST IT
		private void getHeaders(HttpServletRequest request) {
			ArrayList<String> repeatedHeaders=new ArrayList<String>();
			repeatedHeaders.add(Key.CONTEXT_CALL.getValue());
			repeatedHeaders.add(Key.CONTEXT_IDENTITY.getValue());
			repeatedHeaders.add(Key.CONTEXT_TENANT.getValue());
			repeatedHeaders.add(Key.CONTEXT_USER.getValue());
			repeatedHeaders.add(Key.CONTEXT_JOB.getValue());
			repeatedHeaders.add(Key.CONTEXT_SESSION.getValue());

			for (@SuppressWarnings("unchecked")
			Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();){
			       String inputHeader=e.nextElement();
			       if(inputHeader.startsWith("x-ads") && !(repeatedHeaders.contains(inputHeader))){
			    	   put(inputHeader,request.getHeader(inputHeader));
			       }
			}       
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
		return logAttrs.get(key.getValue());
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
		if( (key!=null) && (logAttrs.get(key.getValue())!=null) ){
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
		//logAttrs=this.mixMaps(logEnumAttrs, logAttrs);
		json = new Gson().toJson(logAttrs);
		return json;
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
