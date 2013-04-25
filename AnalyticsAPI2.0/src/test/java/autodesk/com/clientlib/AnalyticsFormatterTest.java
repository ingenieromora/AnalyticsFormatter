package autodesk.com.clientlib;


import junit.framework.Assert;

import org.junit.Test;

import autodesk.com.clientlib.KeyPair.Key;



public class AnalyticsFormatterTest {
	AnalyticsFormatter myAnalytics;


//----------------------------------------------

	/**
	 * Test to evaluate that the category is properly set using the constructor.
	 * We remove the timestamp from the test because it is generated automatically
	 * @author leandro.mora
	 */
	@Test
	public void testConstructor(){
		myAnalytics=new AnalyticsFormatter();

		myAnalytics.put(Key.API_CATEGORY,"file").put(Key.API_LEVEL, "primary");
		
		String expected="{\"api_category\":\"file\",\"api_level\":\"primary\"}";
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

		myAnalytics.put(Key.API_CATEGORY,"file").put("", "folder");
		
		String expected="{\"api_category\":\"file\"}";
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

			myAnalytics.put(Key.API_CATEGORY,"file").put(Key.API_SCOPE, "").put(Key.FACETS_INCLUDED, "storage");
			
			String expected="{\"api_category\":\"file\",\"facets_included\":\"storage\"}";
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

			myAnalytics.put(Key.API_CATEGORY,"file").put("", "").put(Key.FACETS_INCLUDED, "storage");
			
			String expected="{\"api_category\":\"file\",\"facets_included\":\"storage\"}";
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
		myAnalytics.put(Key.API_CATEGORY, "file")
		.put(FacetsKeys.CURRENT_STATE,"2.0.4");

		String expected="{\"api_category\":\"file\",\"compute_job_current_state\":\"2.0.4\"}";
	
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

		myAnalytics.put(Key.API_CATEGORY,"file")
		.put(FacetsKeys.CURRENT_STATE, "2.0.4")
		.put(Key.STATUS,"ok");
		
		myAnalytics.del(FacetsKeys.CURRENT_STATE);
		myAnalytics.del(Key.API_CATEGORY);
		myAnalytics.del(Key.CONSUMER_SRC); // we test that it doesn't return an error
		String expected="{\"status\":\"ok\"}";
		Assert.assertEquals(expected,myAnalytics.outputEvent());

	}

	
}
