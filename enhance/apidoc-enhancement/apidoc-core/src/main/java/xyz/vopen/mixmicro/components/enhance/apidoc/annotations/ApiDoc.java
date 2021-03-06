package xyz.vopen.mixmicro.components.enhance.apidoc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/05/27
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface ApiDoc {

  /**
   * result class
   *
   * @return class type
   */
  Class<?> value() default DefaultClass.class;

  /** result class */
  Class<?> result() default DefaultClass.class;

  /**
   * string result: for simple result
   *
   * @return string result
   */
  String stringResult() default "";

  /** request url */
  String url() default "";

  /** request method */
  String method() default "get";

  interface DefaultClass {}
}
