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
public class LogItem {
	
//----------------------------------  Fields -------------------------------------------------
	 
	 private static java.lang.ThreadLocal<LogItem>    threadContext = new java.lang.ThreadLocal<LogItem>(); 
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
	public LogItem() {
	}
	
//----------------------------------  Statics Getters And Setters -------------------------------------------------
	
	private static LogItem  getCurrentThreadContext() { 
		return threadContext.get(); 		
	}

	//----------------------------------	
	private static void setCurrentThreadContext( LogItem   myAnalytics ) {
		threadContext.set(myAnalytics); 
	}

	//----------------------------------	
	/**
	 * In case there isn't a Analytics Formatter object associated with a thread local variable, it creates it and associates it with the thread local variable.
	 * In case it is already created, it returns the analytics formatter object.
	 * @return AnalyticsFormatter object
	 * @author t_moral
	 */
	public static synchronized LogItem getInstance(){
		LogItem returnInstance=getCurrentThreadContext();
		if(returnInstance==null){
			returnInstance=new LogItem();
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
	public synchronized LogItem put(Key key, String value) {
		if((key!=null) ){
			if((value!=null) && !(value.isEmpty())){ //TEST IT
				logAttrs.put(key.getValue(), value);				
			}else{
				logAttrs.remove(key.getValue());
			}
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
	public synchronized LogItem put(String key, String value) {
		if( key!=null && !(key.isEmpty())  ){
			if((value!=null) && !(value.isEmpty())){ //TEST IT
				logAttrs.put(key, value);				
			}else{
				logAttrs.remove(key);
			}
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
		public synchronized LogItem put(HttpServletRequest request) {
			
			//Context Parameters
			checkAndPutContext(Key.CONTEXT_CALL, request);
			checkAndPutContext(Key.CONTEXT_TENANT, request);
			checkAndPutContext(Key.CONTEXT_USER, request);
			checkAndPutContext(Key.CONTEXT_SESSION, request);
			checkAndPutContext(Key.CONTEXT_JOB, request);
			checkAndPutContext(Key.CONTEXT_IDENTITY, request);
			checkAndPutContext(Key.CONTEXT_CALL, request);
			
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
		private LogItem checkAndPutContext(Key key,HttpServletRequest request){
			if(request.getHeader(key.getValue())!=null){
				put(key,request.getHeader(key.getValue()));
			}
			return this;
		}
		private void getFormParameters(HttpServletRequest request) {
			@SuppressWarnings("unchecked")
			Map<String,String> e = request.getParameterMap();
			for (Map.Entry<String,String> entry : e.entrySet())
			    	put(entry.getKey(),entry.getValue());			 
		}


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

		/**
		* Returns true in case the key is stored into the analytics formatter object.In other case, it returns false.
		* @param a String key with which the specified value should be associated.
		* @version 2.0
		* @return  a boolean value
		* @author t_moral
		*/
//		public synchronized boolean addPropsToHttp(HttpServletResponse response) {
//			response.addHeader(name, value)
//		}	
	
//----------------------------------

	private String formatAttributesAsJason() {

		String json = "";
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
