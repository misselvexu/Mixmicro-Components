package xyz.vopen.mixmicro.components.enhance.rpc.json;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Implementations of this interface are able to intercept Requests by throwing Exceptions.  This allows for
 * implementation of Authentication, Authorization and Sanitizing of the Request.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public interface RequestInterceptor {
	
	/**
	 * This method will be invoked prior to any Mixmicro RPC service being invoked. If an exception is thrown an error will
	 * be sent back to the client.
	 *
	 * @param request is the request object.
	 * @throws Throwable a exception that stops the request
	 */
	void interceptRequest(JsonNode request) throws Throwable;
	
}
