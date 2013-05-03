package com.autodesk.clientlib;

import org.apache.log4j.Logger;

import com.autodesk.clientlib.LogItem;
import com.autodesk.clientlib.FacetsKeys;
import com.autodesk.clientlib.KeyPair.Key;


public class LogItemExample {
	static final Logger as_looger = Logger.getLogger(LogItemExample.class);
	
			/**
			 * Example to show how the application works
			 * @author leandro.mora
			 */
			public static void main(String[] args) {
				LogItem myAnalytics=new LogItem();

				//We put the atributes that we would like to add to our Analytics Formatter instance 
				myAnalytics.put(Key.API_CATEGORY,"file")
							.put(Key.API_SCOPE, "F")
							.put(Key.VERSION, "2.0.4")//Put appropiate t
							.put(Key.DURATION,"5000")	
							.put(Key.CONTEXT_USER,"account")
							.put(Key.STATUS,"ok")
							.put(FacetsKeys.CURRENT_STATE, "complete")
							.put(FacetsKeys.JOB_ENTRIES, "2");
				

				//We get an output from the analytics instance and use it. In this case, we log the information. 
				as_looger.info(myAnalytics.outputEvent());
				
				//Log generated:
				/**2013-04-25 12:00:33 
				AnalyticsFormatterExample [INFO] 
				{"api_category":"file","api_scope":"F",
				"compute_job_current_state":"complete",
				"compute_job_retries":"2","context_user":"account",
				"duration":"5000","status":"ok","version":"2.0.4"}
				 **/

			}
}
