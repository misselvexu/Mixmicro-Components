package xyz.vopen.mixmicro.components.enhance.circuitbreaker.v2.autoconfigure;

import com.google.common.collect.Maps;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.MixmicroCircuitBreakerConfig;

import java.io.Serializable;
import java.util.Map;

import static xyz.vopen.mixmicro.components.enhance.circuitbreaker.v2.autoconfigure.MixmicroCircuitBreakerProperties.MIXMICRO_CIRCUIT_BREAKER;

/**
 * {@link MixmicroCircuitBreakerProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/22
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = MIXMICRO_CIRCUIT_BREAKER)
public class MixmicroCircuitBreakerProperties implements Serializable {

  public static final String MIXMICRO_CIRCUIT_BREAKER = "mixmicro.circuit-breaker";

  private CircuitBreakerType type = CircuitBreakerType.R4J;

  private Map<String, MixmicroCircuitBreakerConfig> instances = Maps.newHashMap();

  public enum CircuitBreakerType {

    /**
     * Resilience4j Implements
     *
     * <p>
     */
    R4J,

    /**
     * Alibaba Sentinel Implements
     *
     * <p>
     */
    STL
  }
}
