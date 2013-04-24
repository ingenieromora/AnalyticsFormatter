package autodesk.com.clientlib;

public class KeyPair {

	public enum Key {
				
		/**
		 * Time of the beginning of this action in UTC down to millisecond resolution.
		 */
		start_time,
		
		/**
		 * Version number of the specification used to generate this Log. This is a three number value (e.g. "2.10.4")
		 * with each section (X.Y.Z) only being incremented under the following conditions:
			X - Only changed when the entire schema or fundamental representation of logging Actions changes in some signficant way
			Y - A field has been fundamentally changed in meaning or has been removed or added
			Z - A minor change to the expected/specified contents of one or more fields (no structural/schema changes)
			The current version of this specification is 1.0.0

		 */
		version,
				
		/**
		 * Unique identifier of the specific server that generated this action.
		 */
		origin_server,
		
		/**
		 * Duration (in milliseconds) of the entire action end-to-end.
		 */
		duration,
		
		/**
		 * The result of the operation.   Failures MUST be reported using this. Success should be encoded as "ok".
		 */
		status,
		
		/**
		 * Identifies the broad tenant-context (akin to “site” or “account”) within which this operation is occurring.    Typically this will correspond to an outermost contextual delimiter for a collaboration space, the company owning and operating the context, and/or the account associated with this context.    In A360 Pro, this corresponds directly to Site.
		 * This attribute should be passed as a HTTP header property by the calling client. The name that should be used for this attribute is x-ads-ctx-tenant.
		 */
		context_tenant,
		
		/**
		 * Unique identifier of the user responsible for this action.   This may be an internal or external user.    For external users this should be a hash-code/GUID that does not provide personally identifiable information.   For internal users this should be marked with a prefix “adsk:” identifying it as internal.
		 */
		context_user,
		
		/**
		 * Unique identifier for the User Session related to this action where a User Session is defined as one of:
			The period of time a user accesses an Autodesk application from start to end.   (Sometimes this will not be available in which case the below should be used.)
			A specific period of time during which a particular user performs activities against a particular product.   (This would normally be a subset of the above definition.)
		 */
		context_session,
		
		/**
		 * Unique identifier for the User Job related to this action where a User Job is defined as: A specific compute/asynchronous job requested by a particular user through his product activity.
		 * This attribute should be passed as a HTTP header property by the calling client. The name that should be used for this attribute is x-ads-ctx-job.
		 */
		context_job,
		
		/**
		 * Assigned at the outer-most API level, this unique value represents the entire call-tree resulting from that particular API call.    Once established it should be passed to each successive API (sync and async) to allow recreation of the call-stack.
		 */
		context_call,
		
		/**
		 * Oxygen access token
			This attribute should be passed as a HTTP header property by the calling client. 
			The name that should be used for this attribute is x-ads-ctx-identity
		 */
		context_identity,
				
		/**
		 * Name of the service generating this action.  (e.g. “Translation”)
		 */
		source_service,
		
		/**
		 * The name of the consuming service.   This should mirror the service names used in “source_service” below.    For example when Translation downloads a file from Nitrogen, the Nitrogen “Download” API should log “translation” as the consumer_src of the Action.
		 */
		consumer_src,
		
		/**
		 * The API category being used.  (e.g. “file”, “folder”, “comment”, “heartbeat”)
		 */
		api_category,
		
		/**
		 * The name of the method being called. Most useful if the the api_category and http_method combined do not provide sufficiently unambiguous information about with method was used.
		 */
		api_method,
		
		/**
		 * Specifies whether this Action represents the entire transactional scope for a particular API or whether it represents the begin or end of an asynchronous transaction.   It should be one of:
		“F” – Represents a full transaction, beginning to end.
		“B” – Represents the beginning of a transaction.
		“E” – Represents the end of a transaction. 
		 */
		api_scope,
		
		/**
		 * Represents the tier of API that this action represents, this is one of:
			“primary” – This represents the primary API to a service that is typically called externally.   (e.g. WebDAV methods to Nitrogen would be primary APIs.)
			“internal” – This represents APIs or transactions that are internal to a service and are typically not externally triggered.
			“status” – This represents a sub-transaction within the internal functioning of a service.    Many of these may occur within the context of an API or overall service operation.
		 */
		api_level,
		
		/**
		 * A list of action facets attached to this record.  (e.g. “storage, http” or “translation”)
		 */
		facets_included,
		
		;
		
		
		private Key() {}
		
		
	}
	
	private Key key;
	private String value;
	
	/**
	 * Creates KeyPair instance with specified Key and value
	 * @param key key with which the specified value is to be associated. Only the keys defined in enumeration Key can be used.
	 * @param value value to be associated with the specified key
	 */
	public KeyPair(Key key, String value) {
		
		this.setKey(key);
		this.setValue(value);
	}
	
	/**
	 * Sets the Key of KeyPair instance with which the specified value is to be associated
	 * @param key key with which the specified value is to be associated. Only the keys defined in enumeration Key can be used.
	 */
	public void setKey(Key key) {
		
		this.key = key;
	}
	
	/**
	 * Returns the key with which the specified value is to be associated.
	 * @return the key with which the specified value is to be associated.
	 */
	public Key getKey() {
		
		return key;
	}
	
	/**
	 * Sets the value of KeyPair instance to be associated with the specified key
	 * @param value value to be associated with the specified KeyPair instance
	 */
	public void setValue(String value) {
		
		this.value = value;
	}
	
	/**
	 * Returns the value of the KeyPair instance to be associated with the specified key
	 * @return the value of the KeyPair instance to be associated with the specified key
	 */
	public String getValue() {
		
		return value;
	}
}
