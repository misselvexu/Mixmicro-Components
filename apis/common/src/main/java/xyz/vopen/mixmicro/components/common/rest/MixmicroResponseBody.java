package xyz.vopen.mixmicro.components.common.rest;

import java.lang.annotation.*;

/**
 * Annotation that indicates a method return value should be bound to the web direct response body.
 * Supported for annotated handler methods.
 *
 * <p>As of version 4.0 this annotation can also be added on the type level in which case it is
 * inherited and does not need to be added on the method level.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/7
 * @see org.springframework.web.bind.annotation.RequestBody
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MixmicroResponseBody {

  /**
   * Direct result without json wrap.
   *
   * <p>if you set {@link #direct()} && {@link #json()} together , json will be ignore.
   *
   * @return false
   */
  boolean direct() default false;

  /**
   * Json Wrapper Enabled .
   *
   * @return true
   */
  boolean json() default true;
}
