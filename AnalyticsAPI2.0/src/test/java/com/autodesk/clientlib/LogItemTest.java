package com.autodesk.clientlib;


import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.autodesk.clientlib.KeyPair.Key;




public class LogItemTest {
	LogItem myAnalytics;

	@Before
	public void setup(){
		myAnalytics=new LogItem();
	}

//----------------------------------------------
	/**
	 * Test to evaluate that the category is properly set using the constructor.
	 * The parameter used for testing may not be appropiate for being used in dev enviroment. They are only used in a test context.
	 * @author t_moral
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
	 * The parameter used for testing may not be appropiate for being used in dev enviroment. They are only used in a test context.
	 * @author t_moral
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
 		 * The parameter used for testing may not be appropiate for being used in dev enviroment. They are only used in a test context.
		 * @author t_moral
		 */
		@Test
		public void testNullValue(){
			myAnalytics.put(Key.API_SCOPE, "F");
			myAnalytics.put(Key.API_CATEGORY,"file").put(Key.API_SCOPE, "").put(Key.FACETS_INCLUDED, "storage").put(Key.VERSION,"1.0.0");
			myAnalytics.put(Key.FACETS_INCLUDED, null);

			String expected="{\"api_category\":\"file\",\"version\":\"1.0.0\"}";
			Assert.assertEquals(expected,myAnalytics.outputEvent());

		}			


//----------------------------------------------
		/**
		 * Test to evaluate that if the user put in the key but the Value and key are not passed as parameter, that put method is omitted
		 * The parameter used for testing may not be appropiate for being used in dev enviroment. They are only used in a test context.		
		 * @author t_moral
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
		 * The parameter used for testing may not be appropiate for being used in dev enviroment. They are only used in a test context.
		 * @author t_moral
		 */
		@Test
		public void testPutHttp(){
			HttpServletRequest mockRequest=org.mockito.Mockito.mock(HttpServletRequest.class);
			
			Mockito.doReturn("PUT").when(mockRequest).getMethod();
			Mockito.doReturn("t_moral").when(mockRequest).getHeader(Key.CONTEXT_USER.getValue());
			Mockito.doReturn("site").when(mockRequest).getHeader(Key.CONTEXT_TENANT.getValue());
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
			headers.add("x-ads-timezone");
			headers.add("x-ads-tenants");
			Mockito.doReturn(headers.elements()).when(mockRequest).getHeaderNames();
			Mockito.doReturn("English").when(mockRequest).getHeader("x-ads-languages");
			Mockito.doReturn("Pacific").when(mockRequest).getHeader("x-ads-timezone");
			
			myAnalytics.put(Key.CONTEXT_CALL,"facets");
			
			myAnalytics.put(mockRequest);
			
			//General Evaluation
			Assert.assertEquals("t_moral",myAnalytics.get(Key.CONTEXT_USER));
			Assert.assertEquals("PUT",myAnalytics.get(HttpFacetKeys.METHOD));
			Assert.assertEquals("site",myAnalytics.get(Key.CONTEXT_TENANT));
			Assert.assertEquals("192.168.1.1",myAnalytics.get(HttpFacetKeys.REMOTEIP));
			Assert.assertEquals("http://autocad.com",myAnalytics.get(HttpFacetKeys.URL));
			Assert.assertEquals("150000",myAnalytics.get(HttpFacetKeys.REQUEST_LEN));
			
			//CheckAndPutTest
			Assert.assertNull(myAnalytics.get(Key.CONTEXT_SESSION));
			Assert.assertEquals("facets", myAnalytics.get(Key.CONTEXT_CALL));
			
			//Headers Test
			String expectedKeys="{\"x-ads-languages\":\"English\",\"x-ads-timezone\":\"Pacific\"}";
			Assert.assertEquals(expectedKeys,myAnalytics.get(HttpFacetKeys.REQUEST_HEADER));
			
			//Param Test
			String expectedParams="{\"product\":\"Autocad\",\"version\":\"2013\"}";
			Assert.assertEquals(expectedParams, myAnalytics.get(HttpFacetKeys.FORM_PARAMS));
		}
		
//----------------------------------------------
	/**
	 * Test that given some attributes the method returns the appropriate output string.
	 * The parameter used for testing may not be appropiate for being used in dev enviroment. They are only used in a test context.
	 * @author t_moral
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
		 * @author t_moral
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
		 * The parameter used for testing may not be appropiate for being used in dev enviroment. They are only used in a test context.
		 * @author t_moral
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
	
//----------------------------------------------
		
		/**
		 * Test for addPropsToHttp method.
		 * Test that having an input log item object, the information is set appropriately on the headers of a request passed as parameter to the addPropsToHttp 
		 * The parameter used for testing may not be appropiate for being used in dev enviroment. They are only used in a test context.
		 * @author t_moral
		 */
		@Test
		public void TestaddPropsToHttp(){
			myAnalytics.put(Key.CONTEXT_CALL, "service").put(Key.CONTEXT_USER,"t_moral").put(Key.CONTEXT_IDENTITY,"Oxigen_Acces_Token")
						.put(Key.CONTEXT_TENANT,"site").put(Key.CONTEXT_JOB,"job1").put(Key.CONTEXT_SESSION,"leoSession");
			
			String inputJsonHeader="{\"x-ads-languages\":\"English\",\"x-ads-timezone\":\"Pacific\"}";
			myAnalytics.put(HttpFacetKeys.REQUEST_HEADER,inputJsonHeader);
						
			HttpServletRequest mockRequest=org.mockito.Mockito.mock(HttpServletRequest.class);

			myAnalytics.addPropsToHttp(mockRequest);
			
			org.mockito.Mockito.verify(mockRequest).setAttribute(Key.CONTEXT_CALL.getValue(), "service");
			org.mockito.Mockito.verify(mockRequest).setAttribute(Key.CONTEXT_USER.getValue(), "t_moral");
			org.mockito.Mockito.verify(mockRequest).setAttribute(Key.CONTEXT_IDENTITY.getValue(), "Oxigen_Acces_Token");
			org.mockito.Mockito.verify(mockRequest).setAttribute(Key.CONTEXT_JOB.getValue(), "job1");
			org.mockito.Mockito.verify(mockRequest).setAttribute(Key.CONTEXT_TENANT.getValue(), "site");
			org.mockito.Mockito.verify(mockRequest).setAttribute(Key.CONTEXT_SESSION.getValue(), "leoSession");
			org.mockito.Mockito.verify(mockRequest).setAttribute("x-ads-languages", "English");
			org.mockito.Mockito.verify(mockRequest).setAttribute("x-ads-timezone", "Pacific");


		}
		
//----------------------------------------------
		
		/**
		 * Test that given some attributes the method returns the appropriate output string.
		 * The parameter used for testing may not be appropiate for being used in dev enviroment. They are only used in a test context.
		 * @author t_moral
		 */
		@Test
		public void testOutputEvent(){
			//PUT HTTP METHOD

			myAnalytics.put(Key.API_CATEGORY, "file")
						.put(HttpFacetKeys.VERSION,"2.0.4");
			
			//PUT HTTP METHOD
			HttpServletRequest mockRequest=org.mockito.Mockito.mock(HttpServletRequest.class);
			
			Mockito.doReturn("t_moral").when(mockRequest).getHeader("x-ads-ctx-user");
			StringBuffer urlBF=new StringBuffer();
			urlBF.append("http://autocad.com");
			Mockito.doReturn(urlBF).when(mockRequest).getRequestURL();
			Mockito.doReturn("PUT").when(mockRequest).getMethod();
			Mockito.doReturn("192.168.1.1").when(mockRequest).getRemoteAddr();
			Mockito.doReturn(1500).when(mockRequest).getContentLength();

			//Parameters
			TreeMap<String,String> paramMap=new TreeMap<>();
			paramMap.put("product", "Autocad");
			paramMap.put("version","2013");
			Mockito.doReturn(paramMap).when(mockRequest).getParameterMap();
			//Headers 
			Vector<String> headers=new Vector<>();
			headers.add("x-ads-languages");
			headers.add("x-ads-timezone");
			Mockito.doReturn(headers.elements()).when(mockRequest).getHeaderNames();
			Mockito.doReturn("English").when(mockRequest).getHeader("x-ads-languages");
			Mockito.doReturn("Pacific").when(mockRequest).getHeader("x-ads-timezone");
			
			myAnalytics.put(mockRequest);
		
			String expected="{\"api_category\":\"file\"," +
					"\"http_form_params\":\"{\\\"product\\\":\\\"Autocad\\\",\\\"version\\\":\\\"2013\\\"}\"," +
					"\"http_method\":\"PUT\",\"http_remoteip\":\"192.168.1.1\"," +
					"\"http_request_headers\":\"{\\\"x-ads-languages\\\":\\\"English\\\",\\\"x-ads-timezone\\\":\\\"Pacific\\\"}\"," +
					"\"http_request_len\":\"1500\",\"http_url\":\"http://autocad.com\"," +
					"\"http_version\":\"1.0.0\",\"x-ads-ctx-user\":\"t_moral\"}";
			Assert.assertEquals(expected, myAnalytics.outputEvent());
			
			String expectedHeader="{\"x-ads-languages\":\"English\",\"x-ads-timezone\":\"Pacific\"}";
			Assert.assertEquals(expectedHeader, myAnalytics.get(HttpFacetKeys.REQUEST_HEADER));
		}
}
