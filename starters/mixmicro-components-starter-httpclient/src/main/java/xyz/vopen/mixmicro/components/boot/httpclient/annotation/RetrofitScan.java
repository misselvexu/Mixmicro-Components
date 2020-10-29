package xyz.vopen.mixmicro.components.boot.httpclient.annotation;

import org.springframework.context.annotation.Import;
import xyz.vopen.mixmicro.components.boot.httpclient.core.RetrofitClientRegistrar;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RetrofitClientRegistrar.class)
public @interface RetrofitScan {

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
