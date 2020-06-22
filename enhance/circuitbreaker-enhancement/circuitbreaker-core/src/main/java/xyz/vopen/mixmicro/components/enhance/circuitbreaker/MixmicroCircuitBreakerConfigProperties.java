package xyz.vopen.mixmicro.components.enhance.circuitbreaker;

import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

import static xyz.vopen.mixmicro.components.enhance.circuitbreaker.MixmicroCircuitBreakerConfigProperties.MIXMICRO_CIRCUIT_BREAKER;

/**
 * Circuit Breaker Config Properties Class
 *
 * @author siran.yao
 * @date 2020/6/20 15:18
 */
@ConfigurationProperties(prefix = MIXMICRO_CIRCUIT_BREAKER)
public class MixmicroCircuitBreakerConfigProperties extends CircuitBreakerConfigurationProperties {

  public static final String MIXMICRO_CIRCUIT_BREAKER = "mixmicro.circuit.breaker";

  private int circuitBreakerAspectOrder = Ordered.LOWEST_PRECEDENCE - 3;
}
