package com.autodesk.clientlib;

import org.apache.log4j.Logger;

import com.autodesk.clientlib.KeyPair.Key;

public class ThreadLocalExample implements Runnable{
	static final Logger example_logger=Logger.getLogger(ThreadLocalExample.class);
	
//----------------------------------------

	public void methodB(){
		LogItem.getInstance().put(Key.DURATION, "1000");

		String api_category=LogItem.getInstance().get(Key.API_CATEGORY);

		example_logger.info("Method B---------------------------------");
		example_logger.info("Value Got: "+api_category+" .Added Key.DURATION key-value pair");
		example_logger.info("Current Thread:"+Thread.currentThread()+'\n');

	}

//----------------------------------------
	@Override
	public void run() {
		LogItem.getInstance().put(FacetsKeys.CURRENT_STATE, "aborted");
		example_logger.info("Newo thread-----------------------------");
		example_logger.info(LogItem.getInstance().outputEvent());
		example_logger.info("Current Thread:"+Thread.currentThread()+'\n');

	}
	
//----------------------------------------

	public static void main(String[] args) {
		ThreadLocalExample myExample=new ThreadLocalExample();
		LogItem.getInstance().put(Key.API_CATEGORY,"file");
		example_logger.info("Main Method---------------------------------");
		example_logger.info(LogItem.getInstance().outputEvent());
		example_logger.info("Current Thread:"+Thread.currentThread()+'\n');

		myExample.methodB();
		
		example_logger.info("Main Method---------------------------------");
		example_logger.info(LogItem.getInstance().outputEvent());
		example_logger.info("Current Thread:"+Thread.currentThread()+'\n');
		//Returns
		new Thread(myExample).start();
	}

//----------------------------------------

/*	
Output
------

2013-04-30 17:01:32 ThreadLocalExample [INFO] Main Method---------------------------------
2013-04-30 17:01:32 ThreadLocalExample [INFO] {"api_category":"file"}
2013-04-30 17:01:32 ThreadLocalExample [INFO] Current Thread:Thread[main,5,main]

2013-04-30 17:01:32 ThreadLocalExample [INFO] Method B---------------------------------
2013-04-30 17:01:32 ThreadLocalExample [INFO] Value Got: file .Added Key.DURATION key-value pair
2013-04-30 17:01:32 ThreadLocalExample [INFO] Current Thread:Thread[main,5,main]

2013-04-30 17:01:32 ThreadLocalExample [INFO] Main Method---------------------------------
2013-04-30 17:01:32 ThreadLocalExample [INFO] {"api_category":"file","duration":"1000"}
2013-04-30 17:01:32 ThreadLocalExample [INFO] Current Thread:Thread[main,5,main]

2013-04-30 17:01:32 ThreadLocalExample [INFO] Newo thread-----------------------------
2013-04-30 17:01:32 ThreadLocalExample [INFO] {"compute_job_current_state":"aborted"}
2013-04-30 17:01:32 ThreadLocalExample [INFO] Current Thread:Thread[Thread-0,5,main]


*/	
}
