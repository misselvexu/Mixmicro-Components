package xyz.vopen.mixmicro.components.circuitbreaker;

import xyz.vopen.mixmicro.components.circuitbreaker.exception.MixmicroCircuitBreakerException;

/**
 * @author: siran.yao
 * @date: 2020/6/22 19:49
 */
public class AbstractMixmicroCircuitBreaker implements MixmicroCircuitBreaker{
    private MixmicroCircuitBreaker mixmicroCircuitBreaker;

    @Override
    public void setSelf(Object proxyBean) {
        mixmicroCircuitBreaker = (MixmicroCircuitBreaker) proxyBean;
    }

    @Override
    public Object executeWrapper(Object... args) throws MixmicroCircuitBreakerException {
        return null;
    }

    @Override
    public Object fallback() {
        return null;
    }

    @Override
    public void publisherFailEvent() {
        mixmicroCircuitBreaker.publisherFailEvent();
    }

    @Override
    public void publisherFailEventDirectFallBack() {
        mixmicroCircuitBreaker.publisherFailEventDirectFallBack();
    }
}
