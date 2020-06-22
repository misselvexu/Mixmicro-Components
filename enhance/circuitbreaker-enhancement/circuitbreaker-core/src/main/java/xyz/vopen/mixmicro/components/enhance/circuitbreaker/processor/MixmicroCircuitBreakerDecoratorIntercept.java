package xyz.vopen.mixmicro.components.enhance.circuitbreaker.processor;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent;
import io.github.resilience4j.circuitbreaker.internal.InMemoryCircuitBreakerRegistry;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import xyz.vopen.mixmicro.components.circuitbreaker.exception.MixmicroCircuitBreakerException;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author: siran.yao
 * @date: 2020/6/20 16:26
 */
public class MixmicroCircuitBreakerDecoratorIntercept implements MethodInterceptor {

    private static final String CIRCUIT_BREAKER_EXECUTE_METHOD_NAME = "executeWrapper";
    private static final String CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT = "publisherFailEvent";
    private static final String CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT_DIRECT_FALLBACK = "publisherFailEventDirectFallBack";
    private static final String CIRCUIT_BREAKER_FALLBACK_METHOD_NAME = "fallback";
    private static final String CIRCUIT_BREAKER_DEFAULT = "default";

    private CircuitBreakerRegistry circuitBreakerRegistry;

    public MixmicroCircuitBreakerDecoratorIntercept(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (method.getName().equals(CIRCUIT_BREAKER_EXECUTE_METHOD_NAME)
                || method.getName().equals(CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT)
                || method.getName().equals(CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT_DIRECT_FALLBACK)) {

            String circuitBreakerName = invocation.getThis().getClass().getSimpleName();
            CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
            if (circuitBreaker == null)
                circuitBreaker = circuitBreakerRegistry.circuitBreaker(CIRCUIT_BREAKER_DEFAULT);

            long start = System.nanoTime();
            long durationInNanos = 0L;

            if (method.getName().equals(CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT)) {
                circuitBreaker.onError(System.nanoTime() - start,
                        TimeUnit.MICROSECONDS,
                        new ExecutionException("ee",new RuntimeException()));
            } else if (method.getName().equals(CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT_DIRECT_FALLBACK)) {

                circuitBreaker.onError(System.nanoTime() - start,
                        TimeUnit.MICROSECONDS,
                        MixmicroCircuitBreakerException.createCallNotPermittedException(circuitBreaker));

                invokeFallBack(invocation);
            }
            else if (method.getName().equals(CIRCUIT_BREAKER_EXECUTE_METHOD_NAME)) {
                boolean allowed = circuitBreaker.tryAcquirePermission();

                if (allowed) {
                    circuitBreaker.acquirePermission();
                    try {
                        Object proceed = invocation.proceed();
                        durationInNanos = System.nanoTime() - start;
                        circuitBreaker.onSuccess(durationInNanos, TimeUnit.NANOSECONDS);
                        return proceed;
                    } catch (Exception e) {
                        circuitBreaker.onError(durationInNanos, TimeUnit.NANOSECONDS, e);
                        return invokeFallBack(invocation);
                    }
                } else {
                    // fallback
                    return invokeFallBack(invocation);
                }
            }
            if (!circuitBreaker.tryAcquirePermission())
                return invokeFallBack(invocation);
        }
        return invocation;
    }

    private Object invokeFallBack(MethodInvocation invocation) throws Throwable {
        Class<?> aClass = invocation.getThis().getClass();
        Method method1 = aClass.getMethod(CIRCUIT_BREAKER_FALLBACK_METHOD_NAME);

        return method1.invoke(aClass.newInstance());
    }
}
