package xyz.vopen.mixmicro.components.enhance.circuitbreaker.autoconfigure;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.common.CompositeCustomizer;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.vopen.mixmicro.components.enhance.circuitbreaker.MixmicroCircuitBreakerConfigProperties;
import xyz.vopen.mixmicro.components.enhance.circuitbreaker.processor.MixmicroCircuitBreakerDecoratorProcessor;

import java.util.Collections;
import java.util.Map;

/**
 * @author: siran.yao
 * @date: 2020/6/19 22:25
 */
@Configuration
@EnableConfigurationProperties(MixmicroCircuitBreakerConfigProperties.class)
@Import(MixmicroCircuitBreakerDecoratorProcessor.class)
public class CircuitBreakerAutoConfiguration {

    @Autowired
    private MixmicroCircuitBreakerConfigProperties mixmicroCircuitBreakerConfigProperties;

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry(){
        Map<String, CircuitBreakerConfigurationProperties.InstanceProperties> instances =
                mixmicroCircuitBreakerConfigProperties.getInstances();

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.ofDefaults();
        for (Map.Entry<String, CircuitBreakerConfigurationProperties.InstanceProperties> entry : instances.entrySet()) {

            CircuitBreakerConfig circuitBreakerConfig = mixmicroCircuitBreakerConfigProperties
                    .createCircuitBreakerConfig(
                            entry.getKey(),
                            entry.getValue(),
                            new CompositeCustomizer<>(Collections.emptyList()));

            registry.circuitBreaker(entry.getKey(),circuitBreakerConfig);
        }
        return registry;
    }
}
