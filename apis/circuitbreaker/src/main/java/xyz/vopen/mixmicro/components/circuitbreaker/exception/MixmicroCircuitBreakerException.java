package xyz.vopen.mixmicro.components.circuitbreaker.exception;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;

/**
 * @author: siran.yao
 * @date: 2020/6/21 15:15
 */
public class MixmicroCircuitBreakerException extends RuntimeException{

    private MixmicroCircuitBreakerException(String message, boolean writableStackTrace) {
        super(message, null, false, writableStackTrace);
    }

    public static MixmicroCircuitBreakerException createCallNotPermittedException(CircuitBreaker circuitBreaker){

        boolean writableStackTraceEnabled = circuitBreaker.getCircuitBreakerConfig().isWritableStackTraceEnabled();

        String message = String.format("Mixmicro CircuitBreaker '%s' is %s and dose not permit further calls",
                circuitBreaker.getName(),circuitBreaker.getState());

        return new MixmicroCircuitBreakerException(message,writableStackTraceEnabled);
    }

    public static MixmicroCircuitBreakerException createFallBackException(String message){

        return new MixmicroCircuitBreakerException(message,false);
    }

}
