package autodesk.com;


import junit.framework.Assert;
import org.junit.Test;

import autodesk.com.KeyPair.Key;



public class AnalyticsFormatterTest {
	AnalyticsFormatter myAnalytics;
	
	public static String removesTimestamp(String output){
		
		String initial=output.substring(0,output.indexOf(",\"ADSK_REQ_DATE\":"));
		String last=output.substring(output.indexOf("\"ADSK_REQ_DATE\":"));
		if(last.contains(",")){
			last=last.substring(last.indexOf(","));
		}else{
			last=last.substring(last.indexOf("}"));
		}
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
		//myAnalytics=new AnalyticsFormatter("DEV");
		myAnalytics=new AnalyticsFormatter();

		myAnalytics.put(Key.ADSK_ID,"201010041558230").put(Key.MODULE, "folder");
		
		String expected="{\"ADSK_ID\":\"201010041558230\",\"MODULE\":\"folder\"}";
		Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
	}
	
//----------------------------------------------

	/**
	 * Test to evaluate that if the user put in the key but Key is not passed as parameter, that put method is omitted
	 * Test to evaluate that the category is properly set using the constructor.
	 * We remove the timestamp from the test because it is generated automatically
	 * @author leandro.mora
	 */
	@Test
	public void testNullKey(){
		//myAnalytics=new AnalyticsFormatter("DEV");
		myAnalytics=new AnalyticsFormatter();

		myAnalytics.put(Key.ADSK_ID,"201010041558230").put("", "folder");
		
		String expected="{\"ADSK_ID\":\"201010041558230\"}";
		Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
	}

//----------------------------------------------

		/**
		 * Test to evaluate that if the user put in the key but Value is not passed as parameter, that put method is omitted
		 * We remove the timestamp from the test because it is generated automatically
		 * @author leandro.mora
		 */
		@Test
		public void testNullValue(){
		//	myAnalytics=new AnalyticsFormatter("DEV");
			myAnalytics=new AnalyticsFormatter();

			myAnalytics.put(Key.ADSK_ID,"201010041558230").put(Key.INSTANCE_TYPE, "").put(Key.MODULE, "folder");
			
			String expected="{\"ADSK_ID\":\"201010041558230\",\"MODULE\":\"folder\"}";
			Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
		}			


//----------------------------------------------

		/**
		 * Test to evaluate that if the user put in the key but the Value and key are not passed as parameter, that put method is omitted
		 * We remove the timestamp from the test because it is generated automatically
		 * @author leandro.mora
		 */
		@Test
		public void testNullValueAndNullKey(){
			myAnalytics=new AnalyticsFormatter();

			myAnalytics.put(Key.ADSK_ID,"201010041558230").put("", "").put(Key.MODULE, "folder");
			
			String expected="{\"ADSK_ID\":\"201010041558230\",\"MODULE\":\"folder\"}";
			Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
		}
		
//----------------------------------------------
		
	/**
	 * Test that given some attributes the method returns the appropriate output string.
	 * We remove the timestamp from the test because it is generated automatically
	 * @author leandro.mora
	 */
	@Test
	public void testKeyJoiner(){
		myAnalytics=new AnalyticsFormatter();
		myAnalytics.put(Key.MODULE, "folder")
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
        .put("HTTP_REQ_URL", "/storage/folders/v1/user/201010041558230/service/my/folder/@root");//TODO Change it by a static variable

		String expected="{\"ADSK_EXEC_TIME\":\"50\",\"ADSK_ID\":\"201010041558230\",\"ADSK_RECORD_SRC\":\"autocad\",\"ADSK_SRC_CONSUMER_ID\":\"autocad\",\"ADSK_STATUS\":\"200\",\"ADSK_USER_TOKEN\":\"6aHXtUgi8hZm5+/TVsdmxqXmalM\",\"BYTES_OUTPUT\":\"500\",\"CATEGORY\":\"AP_DEV\",\"HTTP_REQ_URL\":\"/storage/folders/v1/user/201010041558230/service/my/folder/@root\",\"HTTP_STATUS\":\"TRUE\",\"INSTANCE_TYPE\":\"CALL\",\"MODULE\":\"folder\",\"OPERATION\":\"List\"}";
	
		Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
	}
		
//----------------------------------------------
	
	/**
	 * Test to evaluate that the del operation works
	 * We remove the timestamp from the test because it is generated automatically
	 * @author leandro.mora
	 */
	@Test
	public void testDel(){
		myAnalytics=new AnalyticsFormatter();

		myAnalytics.put("ADSK_ID","201010041558230")
		.put(Key.MODULE, "folder")
		.put("HTTP_STATUS", "folder")
		.put(Key.CATEGORY,"AP_DEV");
		
		myAnalytics.del("HTTP_STATUS");//TODO Change it by a static variable
		myAnalytics.del(Key.CATEGORY);
		myAnalytics.del(Key.BYTES_OUTPUT); // we test that it doesn't return an error
		String expected="{\"ADSK_ID\":\"201010041558230\",\"MODULE\":\"folder\"}";
		Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
	}

	
}
