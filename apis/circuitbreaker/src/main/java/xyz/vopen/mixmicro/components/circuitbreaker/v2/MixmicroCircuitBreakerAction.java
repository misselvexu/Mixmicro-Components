package xyz.vopen.mixmicro.components.circuitbreaker.v2;

import xyz.vopen.mixmicro.components.circuitbreaker.v2.event.DefaultEventConsumer;

import java.lang.annotation.*;

import static xyz.vopen.mixmicro.components.circuitbreaker.v2.MixmicroCircuitBreakable.DEFAULT_FALLBACK_METHOD_NAME;

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
  String fallbackMethod() default DEFAULT_FALLBACK_METHOD_NAME;

  /**
   * Action Custom Exceptions Defined.
   *
   * If nothing is set, it is not turned on by default.
   * Conversely, an exception is thrown when an exception is encountered in the Settings
   *
   * @return
   */
  Class<? extends Throwable>[] customExceptions() default {};

  /**
   * Action Custom Event Consumer.
   *
   * If nothing is set, it is not turned on by {@link DefaultEventConsumer}
   * Conversely, an Event publish by CircuitBreaker published will be caught {@link io.github.resilience4j.core.EventConsumer}
   *
   * @return
   */
  Class customEventConsumer() default DefaultEventConsumer.class;
}
