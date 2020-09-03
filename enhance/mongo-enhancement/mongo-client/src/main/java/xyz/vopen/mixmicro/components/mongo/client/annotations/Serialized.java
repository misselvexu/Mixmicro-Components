package xyz.vopen.mixmicro.components.mongo.client.annotations;

import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker for fields that should be (java) serialized
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @deprecated if this feature is needed, do the serialization manually in a lifecycle event
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Deprecated
public @interface Serialized {

  /** @return When true, compression is disabled on the resulting byte[] */
  boolean disableCompression() default false;

  /** @return the field name to use in the document. Defaults to the java field name. */
  String value() default Mapper.IGNORED_FIELDNAME;
}
