package xyz.vopen.mixmicro.components.circuitbreaker.v2;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.Metrics;
import io.github.resilience4j.core.EventConsumer;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.exception.MixmicroCircuitBreakerDirectThrowException;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.exception.MixmicroCircuitBreakerException;
import xyz.vopen.mixmicro.kits.lang.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * {@link AbstractMixmicroResilience4jCircuitBreaker}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/22
 */
public abstract class AbstractMixmicroResilience4jCircuitBreaker implements MixmicroCircuitBreakerContext<CircuitBreaker>, MixmicroCircuitBreakable, EventConsumer {

  private final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.NANOSECONDS;

  private CircuitBreaker circuitBreaker;

  /**
   * Register {@link CircuitBreaker} With Proxy Service
   * @param circuitBreaker instance of {@link CircuitBreaker}
   */
  @Override
  public final void register(@NonNull CircuitBreaker circuitBreaker) throws MixmicroCircuitBreakerException {
    this.circuitBreaker = circuitBreaker;
  }

  /**
   * Records a failed call. This method must be invoked when a call failed.
   *
   * @param duration The elapsed time duration of the call
   * @param durationUnit The duration unit
   * @param throwable The throwable which must be recorded
   */
  @Override
  public void firing(long duration, TimeUnit durationUnit, Throwable throwable) {
    circuitBreaker.onError(duration, durationUnit, throwable);
  }

  /**
   * Biz Service Throw Custom Biz Exception .
   *
   * @param exception custom biz exception , WARN: sub-exception must extends {@link RuntimeException}
   * @throws MixmicroCircuitBreakerException maybe thrown {@link MixmicroCircuitBreakerException}
   */
  @Override
  public void firing(RuntimeException exception) throws MixmicroCircuitBreakerDirectThrowException {
    throw new MixmicroCircuitBreakerDirectThrowException(exception.getMessage(), exception);
  }

  /**
   * Records a failed call. This method must be invoked when a call failed.
   *
   * @param duration The elapsed time duration of the call
   * @param throwable The throwable which must be recorded
   */
  public void firing(long duration, Throwable throwable) {
    this.firing(duration, DEFAULT_TIMEUNIT, throwable);
  }

  /**
   * Records a successful call. This method must be invoked when a call was successful.
   *
   * @param duration The elapsed time duration of the call
   * @param durationUnit The duration unit
   */
  @Override
  public void ack(long duration, TimeUnit durationUnit) {
    circuitBreaker.onSuccess(duration, durationUnit);
  }


  /**
   * Returns the state of this CircuitBreaker.
   *
   * @return the state of this CircuitBreaker
   */
  @Override
  public CircuitBreakerStatus getStatus() {
    CircuitBreaker.State state = circuitBreaker.getState();
    return convertEnum(state,CircuitBreakerStatus.class);
  }

  @Override
  public void consumeEvent(Object event) {

  }

  /**
   * Records a successful call. This method must be invoked when a call was successful.
   *
   * @param duration The elapsed time duration of the call
   */
  public void ack(long duration) {
    this.ack(duration, DEFAULT_TIMEUNIT);
  }

  @Override
  public Object $fallback0(Throwable throwable) throws MixmicroCircuitBreakerException {
    if(throwable != null) {
      throwable.printStackTrace();
    }
    return null;
  }

  private static <enumFrom,enumTo> enumTo convertEnum(enumFrom from,Class<enumTo> to){
    enumTo rReturn = null;
    if (to.isEnum()){
      enumTo[] array = to.getEnumConstants();
      for (enumTo enu : array) {
        if (enu.toString().equals(from.toString())){
          rReturn = enu;
          break;
        }
      }
    }
    return rReturn;
  }
}
