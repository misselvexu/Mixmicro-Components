package xyz.vopen.mixmicro.components.enhance.rpc.json.core;

/**
 * <p>
 * A status code provider maps an HTTP status code to a Mixmicro RPC result code (e.g. -32000 -&gt; 500).
 * </p>
 * <p>
 * From version 2.0 on the Mixmicro RPC specification is not explicitly documenting the mapping of result/error codes, so
 * this provider can be used to configure application specific HTTP status codes for a given Mixmicro RPC error code.
 * </p>
 * <p>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface HttpStatusCodeProvider {
	
	/**
	 * Returns an HTTP status code for the given response and result code.
	 *
	 * @param resultCode the result code of the current Mixmicro RPC method call. This is used to look up the HTTP status
	 *                   code.
	 * @return the int representation of the HTTP status code that should be used by the Mixmicro RPC response.
	 */
	int getHttpStatusCode(int resultCode);
	
	/**
	 * Returns result code for the given HTTP status code
	 *
	 * @param httpStatusCode the int representation of the HTTP status code that should be used by the Mixmicro RPC response.
	 * @return resultCode the result code of the current Mixmicro RPC method call. This is used to look up the HTTP status
	 */
	Integer getJsonRpcCode(int httpStatusCode);
}
