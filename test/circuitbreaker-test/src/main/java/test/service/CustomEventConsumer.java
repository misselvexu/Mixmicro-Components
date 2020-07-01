package test.service;

import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.AbstractMixmicroResilience4jCircuitBreaker;

/**
 * {@link CustomEventConsumer}
 *
 * @author <a href="mailto:siran0611@gmail.com">siran.yao</a>
 * @version ${project.version}
 * @date 2020/6/30
 */
public class CustomEventConsumer extends AbstractMixmicroResilience4jCircuitBreaker {

    @Override
    public void consumeEvent(Object event) {
        System.out.println("=++++++++++++++++++++++++++++++++++++++++++++++++" + event.getClass());
        if (event instanceof CircuitBreakerOnStateTransitionEvent)
        System.out.println("111" + event);
    }
}
