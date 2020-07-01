package xyz.vopen.mixmicro.components.enhance.circuitbreaker.v2.proxy;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent;
import io.github.resilience4j.core.EventConsumer;
import io.github.resilience4j.core.EventProcessor;
import org.springframework.beans.factory.ObjectProvider;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.MixmicroCircuitBreakable;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.AbstractMixmicroResilience4jCircuitBreaker;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.exception.MixmicroCircuitBreakerException;

/**
 * {@link MixmicroResilience4jCircuitBreakerActionMethodInterceptor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/23
 */
public class MixmicroResilience4jCircuitBreakerActionMethodInterceptor extends AbstractMixmicroCircuitBreakerActionMethodInterceptor {

    private final ObjectProvider<CircuitBreakerRegistry> circuitBreakerRegistryObjectProvider;

    public MixmicroResilience4jCircuitBreakerActionMethodInterceptor(
            MixmicroCircuitBreakable breakable,
            ObjectProvider<CircuitBreakerRegistry> circuitBreakerRegistryObjectProvider) {
        super(breakable);
        this.circuitBreakerRegistryObjectProvider = circuitBreakerRegistryObjectProvider;
    }

    @Override
    protected void registry(String resourceName) throws MixmicroCircuitBreakerException {

        CircuitBreakerRegistry registry = circuitBreakerRegistryObjectProvider.getIfAvailable();

        if (registry != null) {
            if (breakable instanceof AbstractMixmicroResilience4jCircuitBreaker) {
                AbstractMixmicroResilience4jCircuitBreaker breaker = (AbstractMixmicroResilience4jCircuitBreaker) breakable;
                CircuitBreaker circuitBreaker = registry.circuitBreaker(resourceName);
                breaker.register(circuitBreaker);
            }
        }

    }

    @Override
    protected void registry(String resourceName, Class eventConsumer) throws MixmicroCircuitBreakerException {
        CircuitBreakerRegistry registry = circuitBreakerRegistryObjectProvider.getIfAvailable();

        if (registry != null) {
            if (breakable instanceof AbstractMixmicroResilience4jCircuitBreaker) {
                AbstractMixmicroResilience4jCircuitBreaker breaker = (AbstractMixmicroResilience4jCircuitBreaker) breakable;
                CircuitBreaker circuitBreaker = registry.circuitBreaker(resourceName);
                try {
                    EventConsumer consumer = (EventConsumer) eventConsumer.newInstance();
                    EventProcessor processor = (EventProcessor) circuitBreaker.getEventPublisher();
                    if (!processor.hasConsumers()) {
                        processor.onEvent(consumer);
                    }
                    breaker.register(circuitBreaker);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Try to required access permission
     *
     * @param resourceName action resource name
     * @return true / false
     * @throws MixmicroCircuitBreakerException maybe thrown {@link MixmicroCircuitBreakerException}
     */
    @Override
    protected boolean tryAcquire(String resourceName) throws MixmicroCircuitBreakerException {

        CircuitBreakerRegistry registry = circuitBreakerRegistryObjectProvider.getIfAvailable();

        if (registry != null) {
            CircuitBreaker circuitBreaker = registry.circuitBreaker(resourceName);
            return circuitBreaker.tryAcquirePermission();
        }

        return false;
    }
}
