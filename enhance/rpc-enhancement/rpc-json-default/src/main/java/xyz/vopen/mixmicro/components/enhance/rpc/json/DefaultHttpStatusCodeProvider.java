package xyz.vopen.mixmicro.components.enhance.rpc.json;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static xyz.vopen.mixmicro.components.enhance.rpc.json.ErrorResolver.JsonError.BULK_ERROR;
import static xyz.vopen.mixmicro.components.enhance.rpc.json.ErrorResolver.JsonError.CUSTOM_SERVER_ERROR_LOWER;
import static xyz.vopen.mixmicro.components.enhance.rpc.json.ErrorResolver.JsonError.CUSTOM_SERVER_ERROR_UPPER;
import static xyz.vopen.mixmicro.components.enhance.rpc.json.ErrorResolver.JsonError.ERROR_NOT_HANDLED;
import static xyz.vopen.mixmicro.components.enhance.rpc.json.ErrorResolver.JsonError.INTERNAL_ERROR;
import static xyz.vopen.mixmicro.components.enhance.rpc.json.ErrorResolver.JsonError.INVALID_REQUEST;
import static xyz.vopen.mixmicro.components.enhance.rpc.json.ErrorResolver.JsonError.METHOD_NOT_FOUND;
import static xyz.vopen.mixmicro.components.enhance.rpc.json.ErrorResolver.JsonError.METHOD_PARAMS_INVALID;
import static xyz.vopen.mixmicro.components.enhance.rpc.json.ErrorResolver.JsonError.PARSE_ERROR;

/**
 * This default implementation of a {@link HttpStatusCodeProvider} follows the rules defined in the
 * <a href="http://www.jsonrpc.org/historical/json-rpc-over-http.html">JSON-RPC over HTTP</a> document.
 */
@SuppressWarnings("WeakerAccess")
public enum DefaultHttpStatusCodeProvider implements HttpStatusCodeProvider {
	INSTANCE;
	
	final Map<Integer, ErrorResolver.JsonError> httpStatus2JsonError = new HashMap<Integer, ErrorResolver.JsonError>();
	
	private DefaultHttpStatusCodeProvider() {
		httpStatus2JsonError.put(HttpURLConnection.HTTP_INTERNAL_ERROR, INTERNAL_ERROR);
		httpStatus2JsonError.put(HttpURLConnection.HTTP_NOT_FOUND, METHOD_NOT_FOUND);
		httpStatus2JsonError.put(HttpURLConnection.HTTP_BAD_REQUEST, PARSE_ERROR);
	}
	
	
	@Override
	public int getHttpStatusCode(int resultCode) {
		if (resultCode == 0) return HttpURLConnection.HTTP_OK; // Toha: pure java constants
		
		if (isErrorCode(resultCode)) {
			return HttpURLConnection.HTTP_INTERNAL_ERROR;
		} else if (resultCode == INVALID_REQUEST.code || resultCode == PARSE_ERROR.code) {
			return HttpURLConnection.HTTP_BAD_REQUEST;
		} else if (resultCode == METHOD_NOT_FOUND.code) {
			return HttpURLConnection.HTTP_NOT_FOUND;
		}
		
		return HttpURLConnection.HTTP_OK;
	}
	
	private boolean isErrorCode(int result) {
		for (ErrorResolver.JsonError error : Arrays.asList(INTERNAL_ERROR, METHOD_PARAMS_INVALID, ERROR_NOT_HANDLED, BULK_ERROR)) {
			if (error.code == result) return true;
		}
		return CUSTOM_SERVER_ERROR_UPPER >= result && result >= CUSTOM_SERVER_ERROR_LOWER;
	}
	
	@Override
	public Integer getJsonRpcCode(int httpStatusCode) {
		return httpStatus2JsonError.containsKey(httpStatusCode)
				? httpStatus2JsonError.get(httpStatusCode).getCode()
				: null;
	}
}
