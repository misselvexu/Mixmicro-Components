package xyz.vopen.mixmicro.components.boot.httpclient;

import org.springframework.context.annotation.Import;
import xyz.vopen.mixmicro.components.boot.httpclient.core.MixHttpClientRegistrar;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MixHttpClientRegistrar.class)
public @interface MixHttpClientScan {

  /**
   * Scan package path Same meaning as basePackages
   *
   * @return basePackages
   */
  String[] value() default {};

  /**
   * Scan package path
   *
   * @return basePackages
   */
  String[] basePackages() default {};

  /**
   * Scan package classes
   *
   * @return Scan package classes
   */
  Class<?>[] basePackageClasses() default {};
}
