package xyz.vopen.mixmicro.components.enhance.rpc.json.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for annotating service parameters as
 * JsonRpc params by name.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRpcParam {
	
	/**
	 * @return the parameter's name.
	 */
	String value();
	
}
