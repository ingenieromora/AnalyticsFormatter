package com.globant.akn;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Set;
import com.globant.akn.KeyPair.Key;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

//import com.autodesk.analytics.api.KeyPair.Key;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonSerializationContext;
//import com.google.gson.JsonSerializer;

public class LogAttrMapSerializer implements JsonSerializer<EnumMap<Key, String>> {

	@Override
	public JsonElement serialize(EnumMap<Key, String> logAttrs,
			                     Type type,
			                     JsonSerializationContext context) {
		
		JsonObject jsonObject = new JsonObject();
		
		Set<Key> keySet = logAttrs.keySet();
		Iterator<Key> it = keySet.iterator();
		while(it.hasNext()) {
			
			Key key = it.next();
			String prop = String.format("%d", key.getCode());
			jsonObject.addProperty(prop, logAttrs.get(key));
		}
		
		return jsonObject;
	}

}
