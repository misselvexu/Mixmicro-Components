package xyz.vopen.mixmicro.components.boot.snowflake;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Snowflake Enable Annotation
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 23/10/2018.
 * @deprecated use boot auto-configuration instead of .
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(value = SnowflakeBeanRegistrar.class)
public @interface EnableSnowflake {

  /**
   * Work Id For Services ,Default Value <code>1L</code>
   *
   * @return workId
   */
  String workerId() default "1";

  /**
   * auto generate worker id & datacenter id
   *
   * @return true / false
   */
  boolean auto() default true;
}
