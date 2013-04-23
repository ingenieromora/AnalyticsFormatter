package autodesk.com;


import junit.framework.Assert;
import org.junit.Test;

import autodesk.com.KeyPair.Key;



public class AnalyticsFormatterTest {
	AnalyticsFormatter analytics;
	
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
		//analytics=new AnalyticsFormatter("DEV");
		analytics=new AnalyticsFormatter();

		analytics.put("ADSK_ID","201010041558230").put("MODULE", "folder");
		
		String expected="{\"ADSK_ID\":\"201010041558230\",\"MODULE\":\"folder\"}";
		Assert.assertEquals(expected,removesTimestamp(analytics.outputEvent()));
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
		//analytics=new AnalyticsFormatter("DEV");
		analytics=new AnalyticsFormatter();

		analytics.put("ADSK_ID","201010041558230").put("", "folder");
		
		String expected="{\"ADSK_ID\":\"201010041558230\"}";
		Assert.assertEquals(expected,removesTimestamp(analytics.outputEvent()));
	}

//----------------------------------------------

		/**
		 * Test to evaluate that if the user put in the key but Value is not passed as parameter, that put method is omitted
		 * We remove the timestamp from the test because it is generated automatically
		 * @author leandro.mora
		 */
		@Test
		public void testNullValue(){
		//	analytics=new AnalyticsFormatter("DEV");
			analytics=new AnalyticsFormatter();

			analytics.put("ADSK_ID","201010041558230").put("INSTANCE_TYPE", "").put("MODULE", "folder");
			
			String expected="{\"ADSK_ID\":\"201010041558230\",\"MODULE\":\"folder\"}";
			Assert.assertEquals(expected,removesTimestamp(analytics.outputEvent()));
		}			


//----------------------------------------------

		/**
		 * Test to evaluate that if the user put in the key but the Value and key are not passed as parameter, that put method is omitted
		 * We remove the timestamp from the test because it is generated automatically
		 * @author leandro.mora
		 */
		@Test
		public void testNullValueAndNullKey(){
			analytics=new AnalyticsFormatter();

			analytics.put("ADSK_ID","201010041558230").put("", "").put("MODULE", "folder");
			
			String expected="{\"ADSK_ID\":\"201010041558230\",\"MODULE\":\"folder\"}";
			Assert.assertEquals(expected,removesTimestamp(analytics.outputEvent()));
		}
		
//----------------------------------------------
		
	/**
	 * Test that given some attributes the method returns the appropriate output string.
	 * We remove the timestamp from the test because it is generated automatically
	 * @author leandro.mora
	 */
	@Test
	public void testKeyJoiner(){
		analytics=new AnalyticsFormatter();
		analytics.put("MODULE", "folder")
		.put("ADSK_ID","201010041558230")
		.put(Key.CATEGORY,"AP_DEV")
		.put(Key.ADSK_SRC_CONSUMER_ID,"autocad")
		.put("ADSK_RECORD_SRC","autocad")
		.put("ADSK_USER_TOKEN","6aHXtUgi8hZm5+/TVsdmxqXmalM")
		.put("INSTANCE_TYPE","CALL")
        .put("OPERATION", "List")
        .put("ADSK_STATUS", "200")
        .put("HTTP_STATUS", "200")
        .put("HTTP_STATUS", "TRUE")
        .put("ADSK_EXEC_TIME", "50")
        .put("BYTES_OUTPUT", "500")
        .put("HTTP_REQ_URL", "/storage/folders/v1/user/201010041558230/service/my/folder/@root");

		String expected="{\"ADSK_EXEC_TIME\":\"50\",\"ADSK_ID\":\"201010041558230\",\"ADSK_RECORD_SRC\":\"autocad\",\"ADSK_SRC_CONSUMER_ID\":\"autocad\",\"ADSK_STATUS\":\"200\",\"ADSK_USER_TOKEN\":\"6aHXtUgi8hZm5+/TVsdmxqXmalM\",\"BYTES_OUTPUT\":\"500\",\"CATEGORY\":\"AP_DEV\",\"HTTP_REQ_URL\":\"/storage/folders/v1/user/201010041558230/service/my/folder/@root\",\"HTTP_STATUS\":\"TRUE\",\"INSTANCE_TYPE\":\"CALL\",\"MODULE\":\"folder\",\"OPERATION\":\"List\"}";
	
		Assert.assertEquals(expected,removesTimestamp(analytics.outputEvent()));
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

		analytics.put("ADSK_ID","201010041558230")
		.put("MODULE", "folder")
		.put("CATEGORY","AP_DEV");
		
		analytics.del("CATEGORY");
		analytics.del("BYTES_OUTPUT"); // we test that it doesn't return an error
		String expected="{\"ADSK_ID\":\"201010041558230\",\"MODULE\":\"folder\"}";
		Assert.assertEquals(expected,removesTimestamp(analytics.outputEvent()));
	}

	
}
