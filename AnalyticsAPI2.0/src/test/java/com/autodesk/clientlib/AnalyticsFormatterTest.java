package com.autodesk.clientlib;


import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

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
		 * Test to evaluate that the put method with a HTTPServletRequest as parameter.
		 * @author leandro.mora
		 */
		@Test
		public void testPutHttp(){
			HttpServletRequest mockRequest=org.mockito.Mockito.mock(HttpServletRequest.class);
			
			Mockito.doReturn("PUT").when(mockRequest).getMethod();
			Mockito.doReturn("t_moral").when(mockRequest).getHeader("x-ads-ctx-user");
			Mockito.doReturn("site").when(mockRequest).getHeader("x-ads-ctx-tenant");
			Mockito.doReturn("192.168.1.1").when(mockRequest).getRemoteAddr();
			
			StringBuffer urlBF=new StringBuffer();
			urlBF.append("http://autocad.com");
			Mockito.doReturn(urlBF).when(mockRequest).getRequestURL();
			
			Mockito.doReturn(150000).when(mockRequest).getContentLength();
			
			TreeMap<String,String> paramMap=new TreeMap<>();
			paramMap.put("product", "Autocad");
			paramMap.put("version","2013");
			Mockito.doReturn(paramMap).when(mockRequest).getParameterMap();
			 
			Vector<String> headers=new Vector<>();
			headers.add("cookie");
			headers.add("x-ads-job");
			headers.add("x-ads-languages");
			headers.add("x-ads-tenants");
			Mockito.doReturn(headers.elements()).when(mockRequest).getHeaderNames();
			Mockito.doReturn("English").when(mockRequest).getHeader("x-ads-languages");

			myAnalytics.put(mockRequest);
			
			//General Evaluation
			Assert.assertEquals("t_moral",myAnalytics.get(Key.CONTEXT_USER));
			Assert.assertEquals("PUT",myAnalytics.get(HttpFacetKeys.METHOD));
			Assert.assertEquals("site",myAnalytics.get(Key.CONTEXT_TENANT));
			Assert.assertEquals("192.168.1.1",myAnalytics.get(HttpFacetKeys.REMOTEIP));
			Assert.assertEquals("http://autocad.com",myAnalytics.get(HttpFacetKeys.URL));
			Assert.assertEquals("150000",myAnalytics.get(HttpFacetKeys.REQUEST_LEN));
			Assert.assertNull(myAnalytics.get(Key.CONTEXT_SESSION));
			//Headers Evaluation
			Assert.assertEquals("English",myAnalytics.get("x-ads-languages"));
			Assert.assertNull(myAnalytics.get("cookie"));
			Assert.assertNull(myAnalytics.get("cookie"));
			//Param Evaluation
			Assert.assertEquals("Autocad", myAnalytics.get("product"));
			Assert.assertEquals("2013", myAnalytics.get("version"));

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
