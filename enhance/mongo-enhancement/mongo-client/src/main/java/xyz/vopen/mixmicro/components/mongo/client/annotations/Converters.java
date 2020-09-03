package xyz.vopen.mixmicro.components.mongo.client.annotations;

import xyz.vopen.mixmicro.components.mongo.client.converters.TypeConverter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares converters required for this entity/class; converters are registered globally at time of
 * the meta-data discovery.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Converters {
  /** @return The Converters to apply to a field. */
  Class<? extends TypeConverter>[] value();
}
