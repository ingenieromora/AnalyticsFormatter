package autodesk.com;


import junit.framework.Assert;

import org.apache.log4j.Logger;
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
		myAnalytics=new AnalyticsFormatter();

		myAnalytics.put(Key.api_category,"file").put(Key.api_level, "primary");
		
		String expected="{\"api_category\":\"file\",\"api_level\":\"primary\"}";
//		Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
		Assert.assertEquals(expected,myAnalytics.outputEvent());

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
		myAnalytics=new AnalyticsFormatter();

		myAnalytics.put(Key.api_category,"file").put("", "folder");
		
		String expected="{\"api_category\":\"file\"}";
//		Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
		Assert.assertEquals(expected,myAnalytics.outputEvent());

	}

//----------------------------------------------

		/**
		 * Test to evaluate that if the user put in the key but Value is not passed as parameter, that put method is omitted
		 * We remove the timestamp from the test because it is generated automatically
		 * @author leandro.mora
		 */
		@Test
		public void testNullValue(){
			myAnalytics=new AnalyticsFormatter();

			myAnalytics.put(Key.api_category,"file").put(Key.api_scope, "").put(Key.facets_included, "storage");
			
			String expected="{\"api_category\":\"file\",\"facets_included\":\"storage\"}";
//			Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
			Assert.assertEquals(expected,myAnalytics.outputEvent());

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

			myAnalytics.put(Key.api_category,"file").put("", "").put(Key.facets_included, "storage");
			
			String expected="{\"api_category\":\"file\",\"facets_included\":\"storage\"}";
//			Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
			Assert.assertEquals(expected,myAnalytics.outputEvent());

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
		myAnalytics.put(Key.api_category, "file")
		.put(FacetsKeys.CURRENT_STATE,"2.0.4");

		String expected="{\"api_category\":\"file\",\"compute_job_current_state\":\"2.0.4\"}";
	
//		Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
		Assert.assertEquals(expected,myAnalytics.outputEvent());

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

		myAnalytics.put(Key.api_category,"file")
		.put(FacetsKeys.CURRENT_STATE, "2.0.4")
		.put(Key.status,"ok");
		
		myAnalytics.del(FacetsKeys.CURRENT_STATE);//TODO Change it by a static variable
		myAnalytics.del(Key.api_category);
		myAnalytics.del(Key.consumer_src); // we test that it doesn't return an error
		String expected="{\"status\":\"ok\"}";
//		Assert.assertEquals(expected,removesTimestamp(myAnalytics.outputEvent()));
		Assert.assertEquals(expected,myAnalytics.outputEvent());

	}

	
}
