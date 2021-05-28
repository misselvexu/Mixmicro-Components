package xyz.vopen.mixmicro.components.enhance.apidoc.anonations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * rap mock support annotation
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface RapMock {

  /**
   * the limit of the parameter, for example: parameter of 'string|1-10' is '1-10'
   *
   * @return the limit rules of parameter
   */
  String limit() default "";

  /**
   * mock value
   *
   * @return mock value
   */
  String value() default "";
}
