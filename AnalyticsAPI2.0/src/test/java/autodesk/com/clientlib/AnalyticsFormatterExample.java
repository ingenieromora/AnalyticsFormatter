package autodesk.com.clientlib;

import org.apache.log4j.Logger;

import autodesk.com.clientlib.KeyPair.Key;

public class AnalyticsFormatterExample {
	static final Logger as_looger = Logger.getLogger(AnalyticsFormatterExample.class);
	
			/**
			 * Example to show how the application works
			 * @author leandro.mora
			 */
			public static void main(String[] args) {
				AnalyticsFormatter myAnalytics=new AnalyticsFormatter();

				//We put the atributes that we would like to add to our Analytics Formatter instance 
				myAnalytics.put(Key.api_category,"file")
								.put(FacetsKeys.CURRENT_STATE, "complete")
								.put(Key.status,"ok");
				

				//We get an output from the analytics instance and use it. In this case, we log the information. 
				as_looger.info(myAnalytics.outputEvent());
				
				//Log generated:
				//2013-04-24 15:38:07 AnalyticsFormatterExample [INFO] {"api_category":"file","compute_job_current_state":"complete","status":"ok"}

			}
}
