package com.globant.akn;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.globant.akn.KeyPair.Key;

public class AnalyticsFormatterTest {
	AnalyticsFormatter analytics;
	
	public static String removesTimestamp(String output){
		
		String initial=output.substring(0,output.indexOf("\"ADSK_REQ_DATE\":"));
		String last=output.substring(output.indexOf("\"ADSK_REQ_DATE\":"));
		last=last.substring(last.indexOf(",")+1);
		return initial+last;
	}
	
//----------------------------------------------

	/**
	 * Test to evaluate that the category is properly set using the constructor.
	 * We remove the timestamp from the test because it is generated automatically
	 * @author leandro.mora
	 */
	@Test
	public void testConstructor(){
		analytics=new AnalyticsFormatter("DEV");
		
		analytics.put(Key.ADSK_ID,"201010041558230")
		.put(Key.MODULE, "folder");
		
		String expected="{\"ADSK_ID\":\"201010041558230\",\"CATEGORY\":\"DEV\",\"MODULE\":\"folder\"}";
		Assert.assertEquals(expected,removesTimestamp(analytics.output()));
	}
	
//----------------------------------------------
	
	/**
	 * Test that given some attributes the method returns the appropriate output string.
	 * We remove the timestamp from the test because it is generated automatically
	 * @author leandro.mora
	 */
	@Test
	public void testKey(){
		analytics=new AnalyticsFormatter();
		analytics.put(Key.MODULE, "folder")
		.put(Key.ADSK_ID,"201010041558230")
		.put(Key.CATEGORY,"AP_DEV")
		.put(Key.ADSK_SRC_CONSUMER_ID,"autocad")
		.put(Key.ADSK_RECORD_SRC,"autocad")
		.put(Key.ADSK_USER_TOKEN,"6aHXtUgi8hZm5+/TVsdmxqXmalM")
		.put(Key.INSTANCE_TYPE,"CALL")
        .put(Key.OPERATION, "List")
        .put(Key.ADSK_STATUS, "200")
        .put(Key.HTTP_STATUS, "200")
        .put(Key.HTTP_STATUS, "TRUE")
        .put(Key.ADSK_EXEC_TIME, "50")
        .put(Key.BYTES_OUTPUT, "500")
        .put(Key.HTTP_REQ_URL, "/storage/folders/v1/user/201010041558230/service/my/folder/@root");

		String expected="{\"ADSK_EXEC_TIME\":\"50\",\"ADSK_ID\":\"201010041558230\",\"ADSK_RECORD_SRC\":\"autocad\",\"ADSK_SRC_CONSUMER_ID\":\"autocad\",\"ADSK_STATUS\":\"200\",\"ADSK_USER_TOKEN\":\"6aHXtUgi8hZm5+/TVsdmxqXmalM\",\"BYTES_OUTPUT\":\"500\",\"CATEGORY\":\"AP_DEV\",\"HTTP_REQ_URL\":\"/storage/folders/v1/user/201010041558230/service/my/folder/@root\",\"HTTP_STATUS\":\"TRUE\",\"INSTANCE_TYPE\":\"CALL\",\"MODULE\":\"folder\",\"OPERATION\":\"List\"}";
		Assert.assertEquals(expected,removesTimestamp(analytics.output()));
	}
	
//----------------------------------------------
	
	/**
	 * Test that given some attributes the method returns the appropriate output string.
	 * We added a key twice in order to test that the first key is replaced by the second one.
	 * We remove the timestamp from the test because it is generated automatically
	 * @author leandro.mora
	 */
	@Test
	public void testKeyPair(){
		analytics=new AnalyticsFormatter();

		analytics.put(new KeyPair[]{
				new KeyPair(Key.ADSK_EXEC_TIME, "50"),
				new KeyPair(Key.ADSK_ID, "201010041558230"),
				new KeyPair(Key.ADSK_SRC_CONSUMER_ID,"autocad"),
				new KeyPair(Key.ADSK_STATUS, "200"),
				new KeyPair(Key.ADSK_USER_TOKEN,"6aHXtUgi8hZm5+/TVsdmxqXmalM"),
				new KeyPair(Key.BYTES_OUTPUT, "500"),
				new KeyPair(Key.HTTP_REQ_URL, "/storage/folders/v1/user/201010041558230/service/my/folder/@root"),
				new KeyPair(Key.HTTP_STATUS, "200"),
				new KeyPair(Key.HTTP_STATUS, "TRUE"),
				new KeyPair(Key.MODULE, "folder"),
				new KeyPair(Key.OPERATION, "List"),
	    });
	
		String expected= "{\"ADSK_EXEC_TIME\":\"50\",\"ADSK_ID\":\"201010041558230\"," +
				"\"ADSK_SRC_CONSUMER_ID\":\"autocad\",\"ADSK_STATUS\":\"200\",\"ADSK_USER_TOKEN\":\"6aHXtUgi8hZm5+/TVsdmxqXmalM\"," +
				"\"BYTES_OUTPUT\":\"500\",\"HTTP_REQ_URL\":\"/storage/folders/v1/user/201010041558230/service/my/folder/@root\"," +
				"\"HTTP_STATUS\":\"TRUE\",\"MODULE\":\"folder\",\"OPERATION\":\"List\"}";
		Assert.assertEquals(expected,removesTimestamp(analytics.output()));
	}
	
//----------------------------------------------
	
	/**
	 * Test to evaluate that the del operation works
	 * We remove the timestamp from the test because it is generated automatically
	 * @author leandro.mora
	 */
	@Test
	public void testDel(){
		analytics=new AnalyticsFormatter();

		analytics.put(Key.ADSK_ID,"201010041558230")
		.put(Key.MODULE, "folder")
		.put(Key.CATEGORY,"AP_DEV");
		
		analytics.del(Key.CATEGORY);
		analytics.del(Key.BYTES_OUTPUT); // we test that it doesn't return an error
		String expected="{\"ADSK_ID\":\"201010041558230\",\"MODULE\":\"folder\"}";
		Assert.assertEquals(expected,removesTimestamp(analytics.output()));
	}
	
	
}
