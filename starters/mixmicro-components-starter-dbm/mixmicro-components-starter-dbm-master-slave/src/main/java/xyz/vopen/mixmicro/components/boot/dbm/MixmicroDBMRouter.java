package xyz.vopen.mixmicro.components.boot.dbm;

import java.lang.annotation.*;

/**
 * {@link MixmicroDBMRouter}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 7/17/20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface MixmicroDBMRouter {

  /**
   * Judge whether route to master database only or not.
   *
   * @return default false .
   */
  boolean isMasterRouteOnly() default false;

}
