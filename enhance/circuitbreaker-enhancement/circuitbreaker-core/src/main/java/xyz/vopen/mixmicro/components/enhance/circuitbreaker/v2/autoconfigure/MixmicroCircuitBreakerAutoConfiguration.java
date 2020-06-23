package xyz.vopen.mixmicro.components.enhance.circuitbreaker.v2.autoconfigure;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.common.CompositeCustomizer;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import xyz.vopen.mixmicro.components.enhance.circuitbreaker.v2.spring.MixmicroCircuitBreakerBeanPostProcessor;

import java.util.Collections;
import java.util.Map;

import static xyz.vopen.mixmicro.components.enhance.circuitbreaker.v2.autoconfigure.MixmicroCircuitBreakerProperties.MIXMICRO_CIRCUIT_BREAKER;

/**
 * {@link MixmicroCircuitBreakerAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/23
 */
@Configuration
@EnableConfigurationProperties(MixmicroCircuitBreakerProperties.class)
public class MixmicroCircuitBreakerAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(MixmicroCircuitBreakerAutoConfiguration.class);

  @Bean
  @Primary
  @ConditionalOnClass(CircuitBreakerRegistry.class)
  @ConditionalOnProperty(prefix = MIXMICRO_CIRCUIT_BREAKER, value = "type", havingValue = "r4j")
  @ConditionalOnBean(MixmicroCircuitBreakerProperties.class)
  public CircuitBreakerRegistry circuitBreakerRegistry(MixmicroCircuitBreakerProperties properties) {

    log.info("[==MCB==] starting create r4j CircuitBreakerRegistry instance .");

    Map<String, MixmicroCircuitBreakerProperties.MixmicroCircuitBreakerConfig> instances = properties.getInstances();
    CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();

    for (Map.Entry<String, MixmicroCircuitBreakerProperties.MixmicroCircuitBreakerConfig> entry : instances.entrySet()) {
      CircuitBreakerConfigurationProperties temp = new CircuitBreakerConfigurationProperties();
      CircuitBreakerConfigurationProperties.InstanceProperties instanceProperties = new CircuitBreakerConfigurationProperties.InstanceProperties();
      BeanUtils.copyProperties(entry.getValue(), instanceProperties);
      CircuitBreakerConfig circuitBreakerConfig = temp.createCircuitBreakerConfig(entry.getKey(), instanceProperties, new CompositeCustomizer<>(Collections.emptyList()));
      log.info("[==MCB==] registry [{}] info r4j registry .", entry.getKey());
      registry.circuitBreaker(entry.getKey(), circuitBreakerConfig);
    }

    return registry;
  }

  @Bean
  public MixmicroCircuitBreakerBeanPostProcessor mixmicroCircuitBreakerBeanPostProcessor(MixmicroCircuitBreakerProperties properties) {
    return new MixmicroCircuitBreakerBeanPostProcessor(properties);
  }

}
