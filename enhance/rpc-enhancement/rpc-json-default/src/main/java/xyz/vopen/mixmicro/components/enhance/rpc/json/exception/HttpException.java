package xyz.vopen.mixmicro.components.enhance.rpc.json.exception;

import java.io.IOException;

/**
 * Unchecked exception aimed to preserve error response body in case of http error.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@SuppressWarnings("WeakerAccess")
public class HttpException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public HttpException(String message, IOException cause) {
		super(message, cause);
	}
}
