package xyz.vopen.mixmicro.components.mongo.client.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Properties for capped collections; used in {@link Entity}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CappedAt {
  /** @return count of items to cap at (defaults to unlimited) */
  long count() default 0;

  /** @return size to cap at (defaults to 1MB) */
  long value() default 1024 * 1024;
}
