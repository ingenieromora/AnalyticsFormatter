package com.autodesk.clientlib;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.autodesk.clientlib.KeyPair.Key;

public class ThreadLocalTest implements Runnable{
	static final Logger example_logger=Logger.getLogger(ThreadLocalTest.class);
		
//----------------------------------------
		
		public void methodB(){
			LogItem.getInstance().put(Key.DURATION, "1000");

			LogItem.getInstance().get(Key.API_CATEGORY);
		}

//----------------------------------------

		@Override
		public void run() {
			LogItem.getInstance().put(FacetsKeys.CURRENT_STATE, "aborted");
			Assert.assertEquals("{\"api_category\":\"file\",\"duration\":\"1000\"}", LogItem.getInstance().outputEvent());
			Assert.assertEquals("{\"compute_job_current_state\":\"aborted\"},\"duration\":\"1000\"}", LogItem.getInstance().outputEvent());			

		}
		
//----------------------------------------
		@Test
		public void testMaintThread() throws InterruptedException {
			ThreadLocalExample myExample=new ThreadLocalExample();
			
			LogItem.getInstance().put(Key.API_CATEGORY,"file");
			Assert.assertEquals("{\"api_category\":\"file\"}", LogItem.getInstance().outputEvent());
			
			myExample.methodB();
			Assert.assertEquals("{\"api_category\":\"file\",\"duration\":\"1000\"}", LogItem.getInstance().outputEvent());
			//Returns
			Thread t=new Thread(myExample);
			t.start();
			t.join();
		}

}
