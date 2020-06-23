package xyz.vopen.mixmicro.components.circuitbreaker.v2;

import xyz.vopen.mixmicro.components.circuitbreaker.v2.exception.MixmicroCircuitBreakerException;
import xyz.vopen.mixmicro.kits.lang.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * {@link MixmicroCircuitBreakable}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/22
 */
public interface MixmicroCircuitBreakable {

  String DEFAULT_FALLBACK_METHOD_NAME = "$fallback0";

  /**
   * Records a failed call. This method must be invoked when a call failed.
   *
   * @param duration The elapsed time duration of the call
   * @param durationUnit The duration unit
   * @param throwable The throwable which must be recorded
   * @throws MixmicroCircuitBreakerException maybe thrown {@link MixmicroCircuitBreakerException}
   */
  void firing(long duration, TimeUnit durationUnit, Throwable throwable) throws MixmicroCircuitBreakerException;

  /**
   * Records a successful call. This method must be invoked when a call was successful.
   *
   * @param duration The elapsed time duration of the call
   * @param durationUnit The duration unit
   * @throws MixmicroCircuitBreakerException maybe thrown {@link MixmicroCircuitBreakerException}
   */
  void ack(long duration, TimeUnit durationUnit) throws MixmicroCircuitBreakerException;

  /**
   * Service Method Execute Failed, Framework will call {@link MixmicroCircuitBreakable#$fallback0(Throwable)} finally .
   *
   * @param throwable fallback with throwable .
   * @return fallback return result object.
   * @throws MixmicroCircuitBreakerException maybe thrown {@link MixmicroCircuitBreakerException}
   */
  default Object $fallback0(@Nullable Throwable throwable) throws MixmicroCircuitBreakerException {
    // default implements
    if(throwable != null) {
      throwable.printStackTrace();
    }
    return null;
  }
}
