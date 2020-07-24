package xyz.vopen.mixmicro.components.enhance.rpc.json.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for holding an array of @JsonRpcError annotations
 * for a method.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@SuppressWarnings("WeakerAccess")
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRpcErrors {
	
	/**
	 * @return the errors list.
	 */
	JsonRpcError[] value();
}
