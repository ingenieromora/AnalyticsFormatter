package autodesk.com.clientlib;

import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;

import autodesk.com.clientlib.KeyPair.Key;

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
	
 private TreeMap<String, String> logAttrs = new TreeMap<String, String>();
 private EnumMap<Key, String> logEnumAttrs=new EnumMap<Key,String>(Key.class);

//----------------------------------  Constructors -------------------------------------------------

	/**
	 * Create an instance of Analytics Writer class.
	 * @author leandro.mora
	 * @version 2.0
	 * @return AnalyticsWriter instance
	 * @author t_moral
	 */
	public AnalyticsFormatter() {
	}

//----------------------------------  Methods -------------------------------------------------

	/**
	 * Associates the specified value with the specified key. If the key does already exist, the old value is replaced
	 * @param a Key instance with which the specified value is to be associated. Only the keys defined in enumeration Key can be put.
	 * @param value value to be associated with the specified key
	 * @version 2.0
	 * @return the current AnalyticsWriter instance
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
	 * @param a String key with which the specified value is to be associated. Only the keys defined in enumeration Key can be put.
	 * @param value value to be associated with the specified key
	 * @version 2.0
	 * @return the current AnalyticsWriter instance
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
	 * Removes a value associated with an specified key. 
	 * @param key with which the specified value is to be associated
	 * @return the current AnalyticsWriter instance
	 * @version 2.0
	 * @author t_moral
	 */
	public synchronized AnalyticsFormatter del(String key) {
		if(!key.isEmpty()){
			logAttrs.remove(key);
		}
		return this;
	}
	//----------------------------------
	
	/**
	 * Removes a value associated with an specified key. 
	 * @param key with which the specified value is to be associated
	 * @return the current AnalyticsWriter instance
	 * @version 2.0
	 * @author t_moral
	 */
	public synchronized AnalyticsFormatter del(Key key) {
		logEnumAttrs.remove(key);
		return this;
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
