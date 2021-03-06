package xyz.vopen.mixmicro.components.enhance.rpc.json.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation to define the path of a Mixmicro RPC service.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface JsonRpcService {
	
	/**
	 * The path that the service is available at.
	 *
	 * @return the service path
	 */
	String value();
}
