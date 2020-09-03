package xyz.vopen.mixmicro.components.mongo.client.annotations;

import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Reference {
  /**
   * @return the concrete class to instantiate.
   * @deprecated unimplemented
   */
  @Deprecated
  Class<?> concreteClass() default Object.class;

  /** @return whether only _id should be stored versus storing a DBRef */
  boolean idOnly() default false;

  /** @return if true, Ignore any reference that don't resolve (aren't in mongodb) */
  boolean ignoreMissing() default false;

  /**
   * @return if true, Create a proxy around the reference which will be resolved on the first method
   *     call.
   */
  boolean lazy() default false;

  /**
   * @return The name of the Mongo value to store the field. Defaults to the name of the field being
   *     annotated.
   */
  String value() default Mapper.IGNORED_FIELDNAME;
}
