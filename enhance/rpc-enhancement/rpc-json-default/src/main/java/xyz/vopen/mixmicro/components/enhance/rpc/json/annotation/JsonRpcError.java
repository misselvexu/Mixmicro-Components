package xyz.vopen.mixmicro.components.enhance.rpc.json.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping exceptions in service methods to custom JsonRpc errors.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@SuppressWarnings("WeakerAccess")
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRpcError {
	
	/**
	 * @return the exception handled by the annotation.
	 */
	Class<? extends Throwable> exception();
	
	/**
	 * @return the exception code
	 */
	int code();
	
	/**
	 * @return the error message.
	 */
	String message() default "";
	
	/**
	 * @return the ata. If data is not specified, message from exception will be used. If the message is null,
	 * the data element be omitted in the error.
	 */
	String data() default "";
}
