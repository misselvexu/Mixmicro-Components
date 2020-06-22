package xyz.vopen.mixmicro.components.circuitbreaker;

import xyz.vopen.mixmicro.components.circuitbreaker.exception.MixmicroCircuitBreakerException;

/**
 * mixmicro circuitbreaker proxy interface {@link MixmicroCircuitBreaker}
 *
 * @author: siran.yao
 * @date: 2020/6/20 14:24
 */
public interface MixmicroCircuitBreaker<T>  {
    void setSelf(Object proxyBean);

    /**
     * wrapper method support fallback
     *
     * @param args
     * @return
     * @throws MixmicroCircuitBreakerException
     * @see MixmicroCircuitBreakerException
     */
    T executeWrapper(Object... args) throws MixmicroCircuitBreakerException;

    /**
     * encounter exception will invoke this method
     *
     * @return
     */
    T fallback();

    /**
     * default method. when yourself method encounter unknown exception,
     * you can invoke this. will add circuitBreaker fail count. when reach the fail ratio you set
     * will invoke fallback
     */
     void publisherFailEvent();


    /**
     * compare method.publisherFailEvent , forced perform fallback.
     */
     void publisherFailEventDirectFallBack();
}
