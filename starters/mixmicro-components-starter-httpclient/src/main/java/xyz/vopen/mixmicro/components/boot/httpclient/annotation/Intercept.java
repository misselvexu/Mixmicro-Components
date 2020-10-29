package xyz.vopen.mixmicro.components.boot.httpclient.annotation;

import xyz.vopen.mixmicro.components.boot.httpclient.interceptor.BasePathMatchInterceptor;

import java.lang.annotation.*;

/**
 * Automatically assign the parameter value on the annotation to the handleInterceptor instance
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@InterceptMark
public @interface Intercept {
  /**
   * Interceptor matching path pattern
   *
   * @return 拦截器匹配路径pattern Interceptor matching path pattern
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
  Class<? extends BasePathMatchInterceptor> handler();
}
