package com.globant.akn;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import com.globant.akn.KeyPair;
import com.globant.akn.KeyPair.Key;

public class AnalyticsLogguerTest {
	AnalyticsWriter analytics;
	@Before
	public void setup(){
		analytics=new AnalyticsWriter();
	}
	
	/**
	 * Test that check the TestCase01
	 * @author leandro.mora
	 */
	@Test
	public void testCase1(){
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

		System.out.println(analytics.toString());
		String expected="{\"ADSK_EXEC_TIME\":\"50\",\"ADSK_ID\":\"201010041558230\",\"ADSK_RECORD_SRC\":\"autocad\",\"ADSK_REQ_DATE\":\"1365428841955\",\"ADSK_SRC_CONSUMER_ID\":\"autocad\",\"ADSK_STATUS\":\"200\",\"ADSK_USER_TOKEN\":\"6aHXtUgi8hZm5+/TVsdmxqXmalM\",\"BYTES_OUTPUT\":\"500\",\"CATEGORY\":\"AP_DEV\",\"HTTP_REQ_URL\":\"/storage/folders/v1/user/201010041558230/service/my/folder/@root\",\"HTTP_STATUS\":\"TRUE\",\"INSTANCE_TYPE\":\"CALL\",\"MODULE\":\"folder\",\"OPERATION\":\"List\"}";
		expected="{\"ADSK_EXEC_TIME\":\"50\",\"ADSK_ID\":\"201010041558230\",\"ADSK_RECORD_SRC\":\"autocad\",\"ADSK_SRC_CONSUMER_ID\":\"autocad\",\"ADSK_STATUS\":\"200\",\"ADSK_USER_TOKEN\":\"6aHXtUgi8hZm5+/TVsdmxqXmalM\",\"BYTES_OUTPUT\":\"500\",\"CATEGORY\":\"AP_DEV\",\"HTTP_REQ_URL\":\"/storage/folders/v1/user/201010041558230/service/my/folder/@root\",\"HTTP_STATUS\":\"TRUE\",\"INSTANCE_TYPE\":\"CALL\",\"MODULE\":\"folder\",\"OPERATION\":\"List\"}";

		Assert.assertEquals(expected, analytics.toString());
	}
//10:47:22.052 [main] INFO  c.a.analytics.api.AnalyticsLogger 
//- {\"ADSK_EXEC_TIME\":\"50\",\"ADSK_ID\":\"201010041558230\",\"ADSK_RECORD_SRC\":\"autocad\",\"ADSK_REQ_DATE\":\"1365428841955\",\"ADSK_SRC_CONSUMER_ID\":\"autocad\",\"ADSK_STATUS\":\"200\",\"ADSK_USER_TOKEN\":\"6aHXtUgi8hZm5+/TVsdmxqXmalM\",\"BYTES_OUTPUT\":\"500\",\"CATEGORY\":\"AP_DEV\",\"HTTP_REQ_URL\":\"/storage/folders/v1/user/201010041558230/service/my/folder/@root\",\"HTTP_STATUS\":\"TRUE\",\"INSTANCE_TYPE\":\"CALL\",\"MODULE\":\"folder\",\"OPERATION\":\"List\"}


}
