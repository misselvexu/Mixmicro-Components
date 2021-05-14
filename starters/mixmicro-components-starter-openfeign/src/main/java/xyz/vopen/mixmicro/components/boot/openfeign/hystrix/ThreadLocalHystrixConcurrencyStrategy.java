package xyz.vopen.mixmicro.components.boot.openfeign.hystrix;

import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import xyz.vopen.mixmicro.components.boot.openfeign.core.FeignAttributes;

import java.util.Map;
import java.util.concurrent.Callable;

public class ThreadLocalHystrixConcurrencyStrategy extends HystrixConcurrencyStrategy {

  @Override
  public <T> Callable<T> wrapCallable(Callable<T> callable) {
    //    暂采用直接设置RequestAttributes FeignAttributes ThreadLocal值方式
    //    Thread currentThread = Thread.currentThread();
    //    Field inheritableThreadLocalsField = ReflectionUtils.findField(Thread.class,
    // "inheritableThreadLocals");
    //    ReflectionUtils.makeAccessible(inheritableThreadLocalsField);
    //    Object inheritableThreadLocals = ReflectionUtils.getField(inheritableThreadLocalsField,
    // currentThread);
    //    Field tableField = ReflectionUtils.findField(inheritableThreadLocals.getClass(), "table");
    //    ReflectionUtils.makeAccessible(tableField);
    //    Object table = ReflectionUtils.getField(tableField, inheritableThreadLocals);

    RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
    Map<String, String> feignAttributes = FeignAttributes.getAttributes();
    return () -> {
      RequestContextHolder.setRequestAttributes(requestAttributes);
      FeignAttributes.addAttributes(feignAttributes);
      return callable.call();
    };
  }
}
