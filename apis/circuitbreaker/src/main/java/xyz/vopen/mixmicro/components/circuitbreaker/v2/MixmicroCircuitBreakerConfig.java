package xyz.vopen.mixmicro.components.circuitbreaker.v2;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import lombok.*;

import java.io.Serializable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;

/**
 * {@link MixmicroCircuitBreakerConfig}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/22
 */
@Getter
@Setter
public class MixmicroCircuitBreakerConfig implements Serializable {

  private Float slowCallRateThreshold = 100.0f;

  private Duration slowCallDurationThreshold = Duration.ofMillis(60000);

  private Boolean automaticTransitionFromOpenToHalfOpenEnabled = true;
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

  private CircuitBreakerConfig.SlidingWindowType slidingWindowType = CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;
}
