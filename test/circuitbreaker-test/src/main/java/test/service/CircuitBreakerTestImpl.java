package test.service;

import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import xyz.vopen.mixmicro.components.circuitbreaker.AbstractMixmicroCircuitBreaker;
import xyz.vopen.mixmicro.components.circuitbreaker.MixmicroCircuitBreaker;
import xyz.vopen.mixmicro.components.circuitbreaker.exception.MixmicroCircuitBreakerException;

/**
 * @author: siran.yao
 * @date: 2020/6/22 17:43
 */
@Service
public class CircuitBreakerTestImpl extends AbstractMixmicroCircuitBreaker {

    private MixmicroCircuitBreaker mixmicroCircuitBreaker;


    @Override
    public Object executeWrapper(Object... args) throws MixmicroCircuitBreakerException {
        if (args[0].equals("normal")) {
            return "hello-normal";
        }

        publisherFailEvent();
        return "hello-abnormal";
    }

    @Override
    public Object fallback() {
        return "服务降级";
    }
}
