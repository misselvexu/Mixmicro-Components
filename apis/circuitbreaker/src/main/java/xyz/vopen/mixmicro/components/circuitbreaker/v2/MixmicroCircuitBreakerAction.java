package xyz.vopen.mixmicro.components.circuitbreaker.v2;

import java.lang.annotation.*;

/**
 * {@link MixmicroCircuitBreakerAction}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/22
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MixmicroCircuitBreakerAction {

  /**
   * Action Name Defined
   *
   * <p>default: class-full-name#mehod-name
   *
   * @return name of action for circuit breaker
   */
  String name() default "default";

  /**
   * Target Fallback Method Name
   * @return method name
   */
  String fallbackMethod() default "fallback0";
}
