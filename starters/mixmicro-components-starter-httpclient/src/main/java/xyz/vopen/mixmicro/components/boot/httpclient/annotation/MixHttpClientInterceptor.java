package xyz.vopen.mixmicro.components.boot.httpclient.annotation;

import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.AbstractPathMatchInterceptor;

import java.lang.annotation.*;

/**
 * Automatically assign the parameter value on the annotation to the handleInterceptor instance
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@InterceptorComponent
public @interface MixHttpClientInterceptor {
  /**
   * Interceptor matching path pattern
   *
   * @return Interceptor matching path pattern
   */
  String[] include() default {"/**"};

  /**
   * Interceptor excludes matching, excludes specified path interception
   *
   * @return Exclude specified path interception pattern
   */
  String[] exclude() default {};

  /**
   * First obtain the corresponding Bean from the spring container, if not, use reflection to create
   * one!
   *
   * @return Interceptor handler
   */
  Class<? extends AbstractPathMatchInterceptor> handler();
}
