package xyz.vopen.mixmicro.components.circuitbreaker.v2;

import lombok.Data;

import java.io.Serializable;
import java.time.Duration;
import java.util.function.Predicate;

/**
 * {@link MixmicroCircuitBreakerConfig}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/22
 */
@Data
public class MixmicroCircuitBreakerConfig implements Serializable {

  private Duration waitDurationInOpenState;

  private Duration slowCallDurationThreshold;

  private Float failureRateThreshold;

  private Float slowCallRateThreshold;

  private Integer slidingWindowSize;

  private Integer minimumNumberOfCalls;

  private Integer permittedNumberOfCallsInHalfOpenState;

  private Boolean automaticTransitionFromOpenToHalfOpenEnabled;

  private Boolean writableStackTraceEnabled;

  private Boolean allowHealthIndicatorToFail;

  private Integer eventConsumerBufferSize;

  private Boolean registerHealthIndicator;

  private Class<Predicate<Throwable>> recordFailurePredicate;

  private Class<? extends Throwable>[] recordExceptions;

  private Class<? extends Throwable>[] ignoreExceptions;

  private String baseConfig;

  /*
   * flag to enable Exponential backoff policy or not for retry policy delay
   */

  private Boolean enableExponentialBackoff;
  /*
   * exponential backoff multiplier value
   */
  private Double exponentialBackoffMultiplier;

  /*
   * flag to enable randomized delay  policy or not for retry policy delay
   */

  private Boolean enableRandomizedWait;
  /*
   * randomized delay factor value
   */
  private Double randomizedWaitFactor;
}
