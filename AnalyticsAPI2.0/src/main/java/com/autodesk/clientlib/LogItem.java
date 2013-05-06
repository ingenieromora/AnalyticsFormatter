package com.autodesk.clientlib;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.autodesk.clientlib.KeyPair.Key;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
	 * Create an instance of Log Item class.
	 * @author leandro.mora
	 * @returnLog Item instance
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
	 * In case there isn't a Log Item object associated with a thread local variable, it creates it and associates it with the thread local variable.
	 * In case it is already created, it returns the analytics formatter object.
	 * @return LogItem object
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
	 * If the value is null, the key is removed from the LogItem object
	 * @param a Key instance with which the specified value is to be associated. Only the keys defined in enumeration Key can be put.
	 * @param value value to be associated with the specified key
	 * @return the currentLog Item instance
	 * @version 2.0
	 * @author t_moral
	 */
	public synchronized LogItem put(Key key, String value) {
		put(key.getValue(),value);
		return this;
	}

//----------------------------------
	/**
	 * Associates the specified value with the specified key. If the key does already exist, the old value is replaced
	 * If the value is null, the key is removed from the LogItem object
	 * @param a String key with which the specified value is to be associated. We recommend to use pass a Static String variable insted of an ordinary string.
	 * @param value value to be associated with the specified key
	 * @return the currentLog Item instance
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
		 * Get information from a HTTP object passed as parameter.Put that information into the Log Item object
		 * @param a http Request
		 * @return the current Log Item instance
		 * @version 2.0
		 * @author t_moral
		 */
		public synchronized LogItem put(HttpServletRequest request) {
			
			//Context Parameters
			checkAndPutContext(Key.CONTEXT_CALL.getValue(), request);
			checkAndPutContext(Key.CONTEXT_TENANT.getValue(), request);
			checkAndPutContext(Key.CONTEXT_USER.getValue(), request);
			checkAndPutContext(Key.CONTEXT_SESSION.getValue(), request);
			checkAndPutContext(Key.CONTEXT_JOB.getValue(), request);
			checkAndPutContext(Key.CONTEXT_IDENTITY.getValue(), request);
			checkAndPutContext(Key.CONTEXT_CALL.getValue(), request);
			
			//HTTP Facets Parameters
			put(HttpFacetKeys.VERSION,"1.0.0");
			put(HttpFacetKeys.METHOD,request.getMethod());
			put(HttpFacetKeys.REMOTEIP,request.getRemoteAddr());//TODO ASK TONY
			put(HttpFacetKeys.URL,request.getRequestURL().toString());
			put(HttpFacetKeys.REQUEST_LEN,Integer.toString(request.getContentLength()));
			this.addContextHeaders(HttpFacetKeys.REQUEST_HEADER,request);
			this.putParamAttributes(HttpFacetKeys.FORM_PARAMS,request);
			return this;			
		}
		private LogItem checkAndPutContext(String key,HttpServletRequest request){
			if(request.getHeader(key)!=null){
				put(key,request.getHeader(key));
			}
			return this;
		}
		private void putParamAttributes(String key,HttpServletRequest request) {
			@SuppressWarnings("unchecked")
			Map<String,String> parameterMap = request.getParameterMap();
			String jsonValues = new Gson().toJson(parameterMap);    	
			put(key,jsonValues);	
		}


		private void addContextHeaders(String key,HttpServletRequest request) {
			ArrayList<String> repeatedHeaders=new ArrayList<String>();
			repeatedHeaders.add(Key.CONTEXT_CALL.getValue());
			repeatedHeaders.add(Key.CONTEXT_IDENTITY.getValue());
			repeatedHeaders.add(Key.CONTEXT_TENANT.getValue());
			repeatedHeaders.add(Key.CONTEXT_USER.getValue());
			repeatedHeaders.add(Key.CONTEXT_JOB.getValue());
			repeatedHeaders.add(Key.CONTEXT_SESSION.getValue());

			TreeMap<String,String> headerMaps=new TreeMap<String,String>();
			getHeadersMap(request, repeatedHeaders, headerMaps);
			String jsonValues = new Gson().toJson(headerMaps);    	
			put(key,jsonValues);
		}

		private void getHeadersMap(HttpServletRequest request,
				ArrayList<String> repeatedHeaders,
				TreeMap<String, String> headerMaps) {
			for (@SuppressWarnings("unchecked")
			Enumeration<String> e = request.getHeaderNames(); 
			e.hasMoreElements();){
				String inputHeader=e.nextElement();
				if(inputHeader.startsWith("x-ads") && !(repeatedHeaders.contains(inputHeader))){
					headerMaps.put(inputHeader, request.getHeader(inputHeader));		    	   
			    }
			}
		}

//----------------------------------
				/**
				 * Get information from a Log Item Object and set that information as an attribute for a given request object.
				 * @param a http Request
				 * @return the current Log Item instance
				 * @version 2.0
				 * @author t_moral
				 */
				public synchronized LogItem addPropsToHttp(HttpServletRequest request) {
					
					checkAndPutLogItemToRequest(Key.CONTEXT_CALL.getValue(), request);
					checkAndPutLogItemToRequest(Key.CONTEXT_TENANT.getValue(), request);
					checkAndPutLogItemToRequest(Key.CONTEXT_USER.getValue(), request);
					checkAndPutLogItemToRequest(Key.CONTEXT_SESSION.getValue(), request);
					checkAndPutLogItemToRequest(Key.CONTEXT_JOB.getValue(), request);
					checkAndPutLogItemToRequest(Key.CONTEXT_IDENTITY.getValue(), request);
					checkAndPutLogItemToRequest(Key.CONTEXT_CALL.getValue(), request);

					//Add Headers
					//addHeaderKeysToRequest(HttpFacetKeys.REQUEST_HEADER,request);
					return this;			
				}
				private void addHeaderKeysToRequest(String requestHeader,HttpServletRequest request) {
					String ObjectValue=get(requestHeader);
				}

				private LogItem checkAndPutLogItemToRequest(String key,HttpServletRequest request){
					if(get(key)!=null){
						request.setAttribute(key,get(key));
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
		return this.hasKey(key.getValue());
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
