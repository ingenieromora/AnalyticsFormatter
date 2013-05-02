package com.autodesk.clientlib;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.autodesk.clientlib.AnalyticsFormatter;
import com.autodesk.clientlib.FacetsKeys;
import com.autodesk.clientlib.KeyPair.Key;




public class AnalyticsFormatterTest {
	AnalyticsFormatter myAnalytics;

	@Before
	public void setup(){
		myAnalytics=new AnalyticsFormatter();
	}

//----------------------------------------------

	/**
	 * Test to evaluate that the category is properly set using the constructor.
	 * We remove the timestamp from the test because it is generated automatically
	 * @author leandro.mora
	 */
	@Test
	public void testConstructor(){
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
			myAnalytics.put(Key.API_CATEGORY,"file").put("", "").put(Key.FACETS_INCLUDED, "storage");
			
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
		public void testPutHttp(){
			HttpServletRequest mockRequest=org.mockito.Mockito.mock(HttpServletRequest.class);
			HttpSession mockSession=org.mockito.Mockito.mock(HttpSession.class);

			Mockito.doReturn(mockSession).when(mockRequest).getSession();
			Mockito.doReturn("leo_session").when(mockSession).toString();
			Mockito.doReturn("PUT").when(mockRequest).getMethod();
						
			myAnalytics.put(mockRequest);

			Assert.assertEquals("leo_session",myAnalytics.get(Key.CONTEXT_SESSION));
			Assert.assertEquals("PUT",myAnalytics.get(Key.API_METHOD));
			Assert.assertNull(myAnalytics.get(Key.API_LEVEL));
		}
//----------------------------------------------
		
	/**
	 * Test that given some attributes the method returns the appropriate output string.
	 * We remove the timestamp from the test because it is generated automatically
	 * @author leandro.mora
	 */
	@Test
	public void testKeyJoiner(){
		myAnalytics.put(Key.API_CATEGORY, "file")
					.put(FacetsKeys.CURRENT_STATE,"2.0.4");

		String expected="{\"api_category\":\"file\",\"compute_job_current_state\":\"2.0.4\"}";
	
		Assert.assertEquals(expected,myAnalytics.outputEvent());

	}
//----------------------------------------------
	
		/**
		 * Test that given some attributes the method returns the appropriate output string.
		 * We remove the timestamp from the test because it is generated automatically
		 * @author leandro.mora
		 */
		@Test
		public void testGet(){
			myAnalytics.put(Key.API_CATEGORY, "file")
						.put(FacetsKeys.CURRENT_STATE,"2.0.4");
			
			Assert.assertEquals("file", myAnalytics.get(Key.API_CATEGORY));
			Assert.assertEquals("2.0.4", myAnalytics.get(FacetsKeys.CURRENT_STATE));
			Assert.assertNull(myAnalytics.get(Key.CONTEXT_CALL));
			Assert.assertNull(myAnalytics.get(FacetsKeys.JOBID));		
		
		}
		
	//----------------------------------------------
		
		/**
		 * Test that given some attributes the method returns the appropriate output string.
		 * We remove the timestamp from the test because it is generated automatically
		 * @author leandro.mora
		 */
		@Test
		public void testHasKey(){
			myAnalytics.put(Key.API_CATEGORY, "file")
						.put(FacetsKeys.CURRENT_STATE,"2.0.4");
			
			Assert.assertTrue(myAnalytics.hasKey(Key.API_CATEGORY));
			Assert.assertTrue(myAnalytics.hasKey(FacetsKeys.CURRENT_STATE));
			Assert.assertFalse(myAnalytics.hasKey(Key.CONTEXT_CALL));
			Assert.assertFalse(myAnalytics.hasKey(FacetsKeys.JOBID));		
		
		}
	
}