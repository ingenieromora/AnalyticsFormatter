package com.autodesk.clientlib;

/**
 * Class that contains the information related with facets in String constants.
 * @version 2.0.0
 * @author leandro.mora
 *
 */
public class FacetsKeys {
	/**
	 * Version number of the specification used to generate this Facet. This is a three number value (e.g. "2.10.4") with each section (X.Y.Z) only being incremented under the following conditions:

		X - Only changed when the entire schema or fundamental representation of this Facet changes in some signficant way
		Y - A field has been fundamentally changed in meaning or has been removed or added
		Z - A minor change to the expected/specified contents of one or more fields (no structural/schema changes)
	 */
	public static final String VERSION="compute_job_version";
	
	/**
	 * The job id association with this action
	 */
	public static final String JOBID="compute_job_version";
	
	/**
	 * Most compute actions involve job state changes. This field will contain the current state of the job i.e. the state at the end of this action
		"idle"
		"queued"
		"cancelled"
		"timed out"
	 	"delivered"
		"busy"
		"complete"
		"aborted"
	 */
	public static final String CURRENT_STATE="compute_job_current_state";
	
	/**
	 * Number of retries allowed for this job
	 */
	public static final String JOB_ENTRIES="compute_job_retries";
	
	/**
	 * The Hydrogen channel to which this job was submitted
	 */
	public static final String JOB_CHANNEL="compute_job_channel";
}
