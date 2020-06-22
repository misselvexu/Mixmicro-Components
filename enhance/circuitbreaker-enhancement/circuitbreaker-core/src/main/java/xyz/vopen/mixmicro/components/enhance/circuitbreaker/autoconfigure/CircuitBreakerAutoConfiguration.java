package xyz.vopen.mixmicro.components.enhance.circuitbreaker.autoconfigure;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.common.CompositeCustomizer;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigurationProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.mixmicro.components.enhance.circuitbreaker.MixmicroCircuitBreakerConfigProperties;
import xyz.vopen.mixmicro.components.enhance.circuitbreaker.processor.MixmicroCircuitBreakerDecoratorProcessor;

import java.util.Collections;
import java.util.Map;

/**
 * CircuitBreaker Auto Configuration
 *
 * @author siran.yao
 * @date 2020/6/19 22:25
 */
@Configuration
@ConditionalOnClass(CircuitBreakerRegistry.class)
@EnableConfigurationProperties(MixmicroCircuitBreakerConfigProperties.class)
public class CircuitBreakerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public CircuitBreakerRegistry circuitBreakerRegistry(MixmicroCircuitBreakerConfigProperties mixmicroCircuitBreakerConfigProperties) {

    Map<String, CircuitBreakerConfigurationProperties.InstanceProperties> instances = mixmicroCircuitBreakerConfigProperties.getInstances();

    CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();
    for (Map.Entry<String, CircuitBreakerConfigurationProperties.InstanceProperties> entry : instances.entrySet()) {

      CircuitBreakerConfig circuitBreakerConfig =
          mixmicroCircuitBreakerConfigProperties.createCircuitBreakerConfig(
              entry.getKey(), entry.getValue(), new CompositeCustomizer<>(Collections.emptyList()));

      registry.circuitBreaker(entry.getKey(), circuitBreakerConfig);

    }

    return registry;
  }

  @Bean
  public MixmicroCircuitBreakerDecoratorProcessor mixmicroCircuitBreakerDecoratorProcessor(CircuitBreakerRegistry circuitBreakerRegistry) {
    return new MixmicroCircuitBreakerDecoratorProcessor(circuitBreakerRegistry);
  }
}
