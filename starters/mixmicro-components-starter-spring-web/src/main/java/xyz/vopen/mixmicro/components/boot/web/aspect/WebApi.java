package xyz.vopen.mixmicro.components.boot.web.aspect;

import java.lang.annotation.*;

/**
 * {@link WebApi}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface WebApi {

  /**
   * Api description Defined
   *
   * @return description
   */
  String description() default "";
}
