package xyz.vopen.mixmicro.components.boot.httpclient.core;

import xyz.vopen.mixmicro.components.boot.httpclient.autoconfigure.MixHttpClientProperties;
import xyz.vopen.mixmicro.components.boot.httpclient.degrade.ClientBlockException;
import xyz.vopen.mixmicro.components.boot.httpclient.degrade.FallbackFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * {@link MixHttpClientInvocationHandler}
 *
 * <p>Class MixHttpClientInvocationHandler Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/12
 */
public class MixHttpClientInvocationHandler implements InvocationHandler {

  private final Object source;

  private final MixHttpClientProperties properties;

  private Object fallback;

  private FallbackFactory<?> fallbackFactory;


  public MixHttpClientInvocationHandler(Object source, Object fallback, FallbackFactory<?> fallbackFactory, MixHttpClientProperties properties) {
    this.source = source;
    this.properties = properties;
    this.fallback = fallback;
    this.fallbackFactory = fallbackFactory;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      return method.invoke(source, args);
    } catch (Throwable e) {
      Throwable cause = e.getCause();
      Object fallbackObject = getFallbackObject(cause);
      // 熔断逻辑
      if (cause instanceof ClientBlockException && properties.isEnableDegrade() && fallbackObject != null) {
        return method.invoke(fallbackObject, args);
      }
      throw cause;
    }
  }

  private Object getFallbackObject(Throwable cause) {
    if (fallback != null) {
      return fallback;
    }

    if (fallbackFactory != null) {
      return fallbackFactory.create(cause);
    }
    return null;
  }

}
