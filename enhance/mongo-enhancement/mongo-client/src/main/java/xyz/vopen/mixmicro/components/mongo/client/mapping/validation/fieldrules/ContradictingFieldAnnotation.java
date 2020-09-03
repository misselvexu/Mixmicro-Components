package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.lang.annotation.Annotation;
import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class ContradictingFieldAnnotation extends FieldConstraint {

  private final Class<? extends Annotation> a1;
  private final Class<? extends Annotation> a2;

  /**
   * Creates a ContradictingFieldAnnotation validation with the two incompatible annotations.
   *
   * @param a1 the first annotation
   * @param a2 the second annotation
   */
  public ContradictingFieldAnnotation(
      final Class<? extends Annotation> a1, final Class<? extends Annotation> a2) {
    this.a1 = a1;
    this.a2 = a2;
  }

  @Override
  protected final void check(
      final Mapper mapper,
      final MappedClass mc,
      final MappedField mf,
      final Set<ConstraintViolation> ve) {
    if (mf.hasAnnotation(a1) && mf.hasAnnotation(a2)) {
      ve.add(
          new ConstraintViolation(
              Level.FATAL,
              mc,
              mf,
              getClass(),
              String.format(
                  "A field can be either annotated with @%s OR @%s, but not both.",
                  a1.getSimpleName(), a2.getSimpleName())));
    }
  }
}
