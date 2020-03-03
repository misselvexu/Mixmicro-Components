package xyz.vopen.mixmicro.components.boot.snowflake;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * Snowflake Enable Annotation
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 23/10/2018.
 */
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
}
