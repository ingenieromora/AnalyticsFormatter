package com.autodesk.clientlib;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.autodesk.clientlib.KeyPair.Key;

public class ThreadLocalTest implements Runnable{
//Fields -----------------------------
	
	private static String threadValue="";	

//Getters and Setters -----------------------------
		
	public String getThreadValue() {
		return threadValue;
	}

	public void setThreadValue(String threadValue) {
		ThreadLocalTest.threadValue = threadValue;
	}
	
//Auxiliar Methods ----------------------------------------

	public void methodB(){
		LogItem.getInstance().put(Key.DURATION, "1000");

	}

//Thread Method ----------------------------------------
	
	@Override
	public synchronized void run() {
		LogItem.getInstance().put(FacetsKeys.CURRENT_STATE, "aborted");
		this.setThreadValue(LogItem.getInstance().outputEvent());

	}
	
// Test ----------------------------------------
	
	/**
	 * Test that validates the functionality of Thread Local Storage
	 * 
	 * @author t_moral
	 * @throws InterruptedException 
	 */
	@Test
	public void testMaintThread() throws InterruptedException{
		ThreadLocalTest myExample=new ThreadLocalTest();
		
		LogItem.getInstance().put(Key.API_CATEGORY,"file");
		Assert.assertEquals("{\"api_category\":\"file\"}", LogItem.getInstance().outputEvent());
		
		myExample.methodB();
		Assert.assertEquals("{\"api_category\":\"file\",\"duration\":\"1000\"}", LogItem.getInstance().outputEvent());

		Thread t=new Thread(myExample);
		t.start();
		t.join();
		Assert.assertEquals("{\"compute_job_current_state\":\"aborted\"}",threadValue);
		Assert.assertEquals("{\"api_category\":\"file\",\"duration\":\"1000\"}", LogItem.getInstance().outputEvent());

	}

}
