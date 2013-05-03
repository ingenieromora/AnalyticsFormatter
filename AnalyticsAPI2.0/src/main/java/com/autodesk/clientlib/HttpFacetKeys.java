package com.autodesk.clientlib;

public class HttpFacetKeys {
	
	/**
	 * Version number of the specification used to generate this Facet. This is a three number value (e.g. "2.10.4") with each section (X.Y.Z) only being incremented under the following conditions:

    X - Only changed when the entire schema or fundamental representation of this Facet changes in some signficant way
    Y - A field has been fundamentally changed in meaning or has been removed or added
    Z - A minor change to the expected/specified contents of one or more fields (no structural/schema changes)
	 */
	public static final String VERSION="http_version";
	
	/**
	 * HTTP Method
	 */
	public static final String METHOD="http_method";
	
	/**
	 * Remote IP as source of HTTP request
	 */
	public static final String REMOTEIP="http_remoteip";
	
	/**
	 * HTTP Request URL (full – including query string)
	 */
	public static final String URL="http_url";
	
	/**
	 * 	HTTP Response Code
	 */
	public static final String RESPONSE_CODE="http_response_code";
	
	/**
	 * HTTP User Agent    {standardize representation to reflect origin correctly}
	 */
	public static final String USER_AGENT="http_user_agent";
	
	/**
	 * HTTP Headers (beginning with “x-ads”)
	 */
	public static final String REQUEST_HEADER="http_request_headers";

	/**
	 * The content length of the request
	 */
	public static final String REQUEST_LEN="http_request_len";
	
	/**
	 * Form parameters passed in HTTP body
	 */
	public static final String FORM_PARAMS="http_form_params";
	
	/**
	 * HTTP Response Headers
	 */
	public static final String RESPONSE_HEADERS="http_response_headers";

}
