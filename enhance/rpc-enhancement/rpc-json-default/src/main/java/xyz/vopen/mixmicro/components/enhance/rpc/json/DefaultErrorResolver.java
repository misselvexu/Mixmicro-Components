package xyz.vopen.mixmicro.components.enhance.rpc.json;

import com.fasterxml.jackson.databind.JsonNode;

import java.lang.reflect.Method;
import java.util.List;

import static xyz.vopen.mixmicro.components.enhance.rpc.json.ErrorResolver.JsonError.ERROR_NOT_HANDLED;

/**
 * An {@link ErrorResolver} that puts type information into the
 * data portion of the error.  This {@link ErrorResolver} always
 * returns a {@link ErrorResolver.JsonError JsonError}.
 */
@SuppressWarnings("WeakerAccess")
public enum DefaultErrorResolver implements ErrorResolver {
	INSTANCE;
	
	/**
	 * {@inheritDoc}
	 */
	public JsonError resolveError(Throwable t, Method method, List<JsonNode> arguments) {
		return new JsonError(ERROR_NOT_HANDLED.code, t.getMessage(), new ErrorData(t.getClass().getName(), t.getMessage()));
	}
	
}
