package xyz.vopen.mixmicro.components.enhance.rpc.json.exception;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Unchecked Exception thrown by a Mixmicro RPC client when
 * an error occurs.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@SuppressWarnings({"serial", "unused", "WeakerAccess"})
public class JsonRpcClientException extends RuntimeException {
	
	private final int code;
	private final JsonNode data;
	
	/**
	 * Creates the exception.
	 *
	 * @param code    the code from the server
	 * @param message the message from the server
	 * @param data    the data from the server
	 */
	public JsonRpcClientException(int code, String message, JsonNode data) {
		super(message);
		this.code = code;
		this.data = data;
	}
	
	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * @return the data
	 */
	public JsonNode getData() {
		return data;
	}
	
}
