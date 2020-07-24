package xyz.vopen.mixmicro.components.enhance.rpc.json;

import xyz.vopen.mixmicro.components.enhance.rpc.json.client.JsonRpcClient;

/**
 * Request ID generator interface. This allows {@link JsonRpcClient} to use different strategy to
 * generate the ID for the request.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface RequestIDGenerator {
	
	/**
	 * Generate the request ID for each Mixmicro RPC request.
	 *
	 * @return The request id. This can be of any type. It is used to match the response with the request that it is replying to.
	 */
	String nextId();
}
