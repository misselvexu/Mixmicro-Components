package xyz.vopen.mixmicro.components.enhance.circuitbreaker.v2.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.MixmicroCircuitBreakable;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.MixmicroCircuitBreakerAction;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.event.DefaultEventConsumer;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.exception.MixmicroCircuitBreakerException;
import xyz.vopen.mixmicro.kits.Assert;
import xyz.vopen.mixmicro.kits.lang.NonNull;
import xyz.vopen.mixmicro.kits.lang.Nullable;
import xyz.vopen.mixmicro.kits.reflect.ReflectionKit;

import java.lang.reflect.Method;

import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static xyz.vopen.mixmicro.components.circuitbreaker.v2.MixmicroCircuitBreakable.DEFAULT_FALLBACK_METHOD_NAME;

/**
 * {@link AbstractMixmicroCircuitBreakerActionMethodInterceptor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/23
 */
public abstract class AbstractMixmicroCircuitBreakerActionMethodInterceptor implements MethodInterceptor {

    private static final Logger log = LoggerFactory.getLogger(AbstractMixmicroCircuitBreakerActionMethodInterceptor.class);

    protected final MixmicroCircuitBreakable breakable;

    private Method fallbackMethod;

    protected AbstractMixmicroCircuitBreakerActionMethodInterceptor(MixmicroCircuitBreakable breakable) {
        this.breakable = breakable;
        Assert.notNull(this.breakable, "Mixmicro Circuit Breakable Instance Object Must not be null .");
        fallbackMethod = ReflectionKit.getAccessibleMethod(this.breakable, DEFAULT_FALLBACK_METHOD_NAME, Throwable.class);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object[] args = invocation.getArguments();

        boolean isFallbackMethodOverride = false;
        boolean assignableFrom = false;
        boolean isAnnotationMethod = false;
        try {
            Method method = invocation.getMethod();

            if (method.isAnnotationPresent(MixmicroCircuitBreakerAction.class)) {
                isAnnotationMethod = true;
                MixmicroCircuitBreakerAction action = method.getAnnotation(MixmicroCircuitBreakerAction.class);

                String resourceName = action.name();

                Class customEventConsumer = action.customEventConsumer();
                // register first
                if (!customEventConsumer.isAssignableFrom(DefaultEventConsumer.class)){
                    this.registry(resourceName,customEventConsumer);
                }else {
                    this.registry(resourceName);
                }


                String methodName = action.fallbackMethod();

                Class<? extends Throwable>[] classes = action.customExceptions();



                if (DEFAULT_FALLBACK_METHOD_NAME.equalsIgnoreCase(methodName)) {
                    throw new MixmicroCircuitBreakerException(String.format("[==MCB==] custom fallback method name must not named : '%s'", DEFAULT_FALLBACK_METHOD_NAME));
                } else {
                    try {
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        Method customFallbackMethod = ReflectionKit.getAccessibleMethod(breakable, methodName, parameterTypes);
                        isFallbackMethodOverride = customFallbackMethod != null;

                        if (isFallbackMethodOverride) {
                            fallbackMethod = customFallbackMethod;
                        }
                    } catch (Exception e) {
                        log.warn("[==MCB==] found fallback method with name : [" + methodName + "] failed , fallback method must has the same signature with origin service method ", e);
                    }
                }

                Assert.notNull(fallbackMethod, "CircuitBreaker s fallback method must not be null .");

                long start = System.nanoTime();

                boolean acquired = this.tryAcquire(resourceName);

                if (acquired) {
                    try {
                        Object result = invocation.proceed();
                        return result;

                    } catch (Exception e) {

                        this.breakable.firing((System.nanoTime() - start), NANOSECONDS, e);

                        // if invoked happened custom exception , just throw exception
                        for (Class<? extends Throwable> aClass : classes) {
                            assignableFrom = e.getClass().isAssignableFrom(aClass);
                            if (assignableFrom) {
                                log.error("encounter custom exception : {}",e.getMessage());
                                throw e;
                            }
                        }

                        return this.invokedFallbackMethod(breakable, fallbackMethod, isFallbackMethodOverride ? args : e);
                    }
                }

                // DEFAULT RETURN NULL.
                // ONLY WHEN INVOKED HAPPENED EXCEPTION & FAILED ATTEMPTED MAX COUNTS .
                return this.invokedFallbackMethod(breakable, fallbackMethod, isFallbackMethodOverride ? args : null);

            } else {
                // execute real method directly
                return invocation.proceed();
            }

        } catch (Throwable e) {

            if (assignableFrom || !isAnnotationMethod) {
                throw e;
            }

            // exception.
            if (e instanceof MixmicroCircuitBreakerException) {

            }

            //
            return this.invokedFallbackMethod(
                    breakable,
                    fallbackMethod,
                    isFallbackMethodOverride ? args : new MixmicroCircuitBreakerException("CircuitBreaker s proxy method execute failed ", e));
        }
    }

    private Object invokedFallbackMethod(
            @NonNull Object instance, @NonNull Method method, @Nullable Object... args) {
        try {
            method.setAccessible(true);
            return method.invoke(instance, args);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("[==MCB==] fallback method execute failed, just return null.");
            return null;
        }
    }

    /**
     * Try to Register Context Instance
     *
     * @param resourceName action resource name
     * @throws MixmicroCircuitBreakerException maybe thrown {@link MixmicroCircuitBreakerException}
     */
    protected abstract void registry(String resourceName) throws MixmicroCircuitBreakerException;


    protected abstract void registry(String resourceName,Class eventConsumer) throws MixmicroCircuitBreakerException;


    /**
     * Try to required access permission
     *
     * @param resourceName action resource name
     * @return true / false , if return true , thread will execute real target service method .
     * @throws MixmicroCircuitBreakerException maybe thrown {@link MixmicroCircuitBreakerException}
     */
    protected abstract boolean tryAcquire(String resourceName) throws MixmicroCircuitBreakerException;
}
