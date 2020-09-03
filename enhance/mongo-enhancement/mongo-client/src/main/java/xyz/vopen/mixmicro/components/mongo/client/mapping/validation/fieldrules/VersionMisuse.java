package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules;

import xyz.vopen.mixmicro.components.mongo.client.ObjectFactory;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Version;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.util.Set;

import static java.lang.String.format;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class VersionMisuse extends FieldConstraint {

  private ObjectFactory creator;

  /**
   * Creates a version validator.
   *
   * @param creator the ObjectFactory to use
   */
  public VersionMisuse(final ObjectFactory creator) {
    this.creator = creator;
  }

  @Override
  protected void check(
      final Mapper mapper,
      final MappedClass mc,
      final MappedField mf,
      final Set<ConstraintViolation> ve) {
    if (mf.hasAnnotation(Version.class)) {
      final Class<?> type = mf.getType();
      if (Long.class.equals(type) || long.class.equals(type)) {

        // TODO: Replace this will a read ObjectFactory call -- requires Mapper instance.
        final Object testInstance = creator.createInstance(mc.getClazz());

        // check initial value
        if (Long.class.equals(type)) {
          if (mf.getFieldValue(testInstance) != null) {
            ve.add(
                new ConstraintViolation(
                    Level.FATAL,
                    mc,
                    mf,
                    getClass(),
                    format(
                        "When using @%s on a Long field, it must be initialized to null.",
                        Version.class.getSimpleName())));
          }
        } else if (long.class.equals(type)) {
          if ((Long) mf.getFieldValue(testInstance) != 0L) {
            ve.add(
                new ConstraintViolation(
                    Level.FATAL,
                    mc,
                    mf,
                    getClass(),
                    format(
                        "When using @%s on a long field, it must be initialized to 0.",
                        Version.class.getSimpleName())));
          }
        }
      } else {
        ve.add(
            new ConstraintViolation(
                Level.FATAL,
                mc,
                mf,
                getClass(),
                format(
                    "@%s can only be used on a Long/long field.", Version.class.getSimpleName())));
      }
    }
  }
}
