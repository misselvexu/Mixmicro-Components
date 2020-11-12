package xyz.vopen.mixmicro.components.boot.httpclient.degrade;

import java.lang.annotation.*;

/**
 * {@link Degrade}
 *
 * <p>Class Degrade Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/12
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Degrade {

  /** RT threshold or exception ratio threshold count. */
  double count();

  /** Degrade recover timeout (in seconds) when degradation occurs. */
  int timeWindow() default 5;

  /** Degrade strategy (0: average RT, 1: exception ratio). */
  DegradeStrategy degradeStrategy() default DegradeStrategy.AVERAGE_RT;
}
