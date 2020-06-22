package xyz.vopen.mixmicro.components.enhance.circuitbreaker.processor;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.circuitbreaker.exception.MixmicroCircuitBreakerException;
import xyz.vopen.mixmicro.kits.Assert;
import xyz.vopen.mixmicro.kits.lang.NonNull;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Mixmicro CircuitBreaker Intercept
 *
 * @author siran.yao
 * @date 2020/6/20 16:26
 */
public class MixmicroCircuitBreakerDecoratorIntercept implements MethodInterceptor {

  private static final Logger log = LoggerFactory.getLogger(MixmicroCircuitBreakerDecoratorIntercept.class);

  private static final String CIRCUIT_BREAKER_EXECUTE_METHOD_NAME = "executeWrapper";
  private static final String CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT = "publisherFailEvent";
  private static final String CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT_DIRECT_FALLBACK = "publisherFailEventDirectFallBack";
  private static final String CIRCUIT_BREAKER_FALLBACK_METHOD_NAME = "fallback";
  private static final String CIRCUIT_BREAKER_DEFAULT = "default";

  final private CircuitBreakerRegistry circuitBreakerRegistry;

  public MixmicroCircuitBreakerDecoratorIntercept(@NonNull CircuitBreakerRegistry circuitBreakerRegistry) {
    this.circuitBreakerRegistry = circuitBreakerRegistry;
    Assert.notNull(this.circuitBreakerRegistry, "mixmciro circuit breaker registry must not be null.");
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {

    Method method = invocation.getMethod();
    if (method.getName().equals(CIRCUIT_BREAKER_EXECUTE_METHOD_NAME)
        || method.getName().equals(CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT)
        || method.getName().equals(CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT_DIRECT_FALLBACK)) {

      String circuitBreakerName = invocation.getThis().getClass().getSimpleName();
      CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);

      Assert.notNull(circuitBreaker, "mixmicro circuit breaker instance must not be null with name: " + circuitBreakerName);

      long start = System.nanoTime();
      long durationInNanos = 0L;

      if (method.getName().equals(CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT)) {

        circuitBreaker.onError(System.nanoTime() - start, TimeUnit.MICROSECONDS, new ExecutionException("ee", new RuntimeException()));

      } else if (method.getName().equals(CIRCUIT_BREAKER_PUBLISHER_FAIL_EVENT_DIRECT_FALLBACK)) {

        circuitBreaker.onError(System.nanoTime() - start, TimeUnit.MICROSECONDS, MixmicroCircuitBreakerException.createCallNotPermittedException(circuitBreaker));

        invokeFallBack(invocation);

      } else if (method.getName().equals(CIRCUIT_BREAKER_EXECUTE_METHOD_NAME)) {

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
      if (!circuitBreaker.tryAcquirePermission()) {

        return invokeFallBack(invocation);
      }
    }

    return invocation;
  }

  /**
   * Invoke CircuitBreaker Service Fallback Method.
   * @param invocation target object instance
   * @return execute result .
   * @throws Throwable maybe thrown exception .
   */
  private Object invokeFallBack(MethodInvocation invocation) throws Throwable {

    Class<?> aClass = invocation.getThis().getClass();

    Method fallbackMethod = aClass.getMethod(CIRCUIT_BREAKER_FALLBACK_METHOD_NAME);

    return fallbackMethod.invoke(aClass.newInstance());
  }
}
