package xyz.vopen.mixmicro.components.mongo.client.annotations;

import xyz.vopen.mixmicro.components.mongo.client.utils.IndexType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Define a field to be used in an index; */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface Field {
  /**
   * @return "Direction" of the indexing. Defaults to {@link IndexType#ASC}.
   * @see IndexType
   */
  IndexType type() default IndexType.ASC;

  /** @return Field name to index */
  String value();

  /**
   * @return The weight to use when creating a text index. This value only makes sense when
   *     direction is {@link IndexType#TEXT}
   */
  int weight() default -1;
}
