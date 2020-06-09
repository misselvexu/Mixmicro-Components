package xyz.vopen.mixmicro.components.enhance.spi.annotation;

import java.lang.annotation.*;

/**
 * Marks a field as to be inject by Spring's dependency injection facilities. This is an alternative
 * to the JSR-330 {@link javax.inject.Inject} annotation, adding required-vs-optional semantics.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/31
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectSpringBean {

  /**
   * Declares whether the annotated dependency is required.
   *
   * <p>Defaults to {@code true}.
   */
  boolean required() default true;
}
