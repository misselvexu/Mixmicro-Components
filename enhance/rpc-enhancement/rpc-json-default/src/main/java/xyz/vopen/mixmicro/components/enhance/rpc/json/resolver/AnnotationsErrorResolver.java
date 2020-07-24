package xyz.vopen.mixmicro.components.enhance.rpc.json.resolver;

import com.fasterxml.jackson.databind.JsonNode;
import xyz.vopen.mixmicro.components.enhance.rpc.json.ErrorResolver;
import xyz.vopen.mixmicro.components.enhance.rpc.json.annotation.JsonRpcError;
import xyz.vopen.mixmicro.components.enhance.rpc.json.annotation.JsonRpcErrors;
import xyz.vopen.mixmicro.components.enhance.rpc.json.exception.ErrorData;
import xyz.vopen.mixmicro.components.enhance.rpc.json.utils.ReflectionUtil;

import java.lang.reflect.Method;
import java.util.List;

/**
 * {@link ErrorResolver} that uses annotations.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public enum AnnotationsErrorResolver implements ErrorResolver {

  /**
   *
   */
  INSTANCE;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public JsonError resolveError(Throwable thrownException, Method method, List<JsonNode> arguments) {
		JsonRpcError resolver = getResolverForException(thrownException, method);
		if (notFoundResolver(resolver)) {
			return null;
		}
		
		String message = hasErrorMessage(resolver) ? resolver.message() : thrownException.getMessage();
		return new JsonError(resolver.code(), message, new ErrorData(resolver.exception().getName(), message));
	}
	
	private JsonRpcError getResolverForException(Throwable thrownException, Method method) {
		JsonRpcErrors errors = ReflectionUtil.getAnnotation(method, JsonRpcErrors.class);
		if (hasAnnotations(errors)) {
			for (JsonRpcError errorDefined : errors.value()) {
				if (isExceptionInstanceOfError(thrownException, errorDefined)) {
					return errorDefined;
				}
			}
		}
		return null;
	}
	
	private boolean notFoundResolver(JsonRpcError resolver) {
		return resolver == null;
	}
	
	private boolean hasErrorMessage(JsonRpcError em) {
		// noinspection ConstantConditions
		return em.message() != null && em.message().trim().length() > 0;
	}
	
	private boolean hasAnnotations(JsonRpcErrors errors) {
		return errors != null;
	}
	
	private boolean isExceptionInstanceOfError(Throwable target, JsonRpcError em) {
		return em.exception().isInstance(target);
	}
	
}
