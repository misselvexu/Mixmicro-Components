package xyz.vopen.mixmicro.components.enhance.rpc.json;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Resolves client {@link Throwable}s from server generated {@link ObjectNode}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface ExceptionResolver {
	
	/**
	 * Resolves the exception from the given Mixmicro RPC
	 * response {@link ObjectNode}.
	 *
	 * @param response the response
	 * @return the exception
	 */
	Throwable resolveException(ObjectNode response);
	
}
