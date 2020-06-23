package xyz.vopen.mixmicro.components.circuitbreaker.v2;

import lombok.*;

import java.io.Serializable;
import java.time.Duration;

/**
 * {@link MixmicroCircuitBreakerConfig}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/22
 */
@Getter
@Setter
public class MixmicroCircuitBreakerConfig implements Serializable {

  //
  private Duration waitDurationInOpenState = Duration.ofSeconds(50);

  //
  private Float failureRateThreshold = 50.0f;

  //
  private Integer slidingWindowSize = 3;

  //
  private Integer minimumNumberOfCalls = 3;

  //
  private Integer permittedNumberOfCallsInHalfOpenState = 3;

  //
  private Integer eventConsumerBufferSize = 5;

  private SlidingWindowType slidingWindowType = SlidingWindowType.COUNT_BASED;

  /**
   * Type Defined
   *
   * <p>
   */
  public enum SlidingWindowType {
    /**
     * Time Based
     *
     * <p>
     */
    TIME_BASED,

    /**
     * Count Based
     *
     * <p>
     */
    COUNT_BASED
  }
}
