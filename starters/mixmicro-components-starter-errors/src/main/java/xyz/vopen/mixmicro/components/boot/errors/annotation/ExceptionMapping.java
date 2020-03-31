package xyz.vopen.mixmicro.components.boot.errors.annotation;

import org.springframework.http.HttpStatus;
import xyz.vopen.mixmicro.components.boot.errors.handlers.AnnotatedWebErrorHandler;

import java.lang.annotation.*;

/**
 * When an exception annotated with this annotation happens, the metadata encapsulated
 * in the annotation would help us to transform the language level exception to REST API
 * error code/status code combination.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @see ExposeAsArg
 * @see AnnotatedWebErrorHandler
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionMapping {

    /**
     * Corresponding HTTP status code for this particular exception.
     *
     * @return The error code.
     */
    String errorCode();

    /**
     * The mapping error code for this exception.
     *
     * @return The status code.
     */
    HttpStatus statusCode();
}
