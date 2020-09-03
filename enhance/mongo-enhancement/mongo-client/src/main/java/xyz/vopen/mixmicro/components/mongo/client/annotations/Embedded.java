package xyz.vopen.mixmicro.components.mongo.client.annotations;

import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface Embedded {

  /**
   * @return the concrete class to instantiate.
   * @deprecated unused in 2.x
   */
  @Deprecated
  Class<?> concreteClass() default Object.class;

  /**
   * @return The name of the Mongo value to store the field. Defaults to the name of the field being
   *     annotated.
   */
  String value() default Mapper.IGNORED_FIELDNAME;
}
