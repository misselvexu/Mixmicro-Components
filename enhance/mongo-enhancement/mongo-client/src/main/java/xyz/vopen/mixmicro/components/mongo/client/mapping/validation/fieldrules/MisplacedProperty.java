package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules;

import xyz.vopen.mixmicro.components.mongo.client.annotations.Property;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class MisplacedProperty extends FieldConstraint {

  @Override
  protected void check(
      final Mapper mapper,
      final MappedClass mc,
      final MappedField mf,
      final Set<ConstraintViolation> ve) {
    // a field can be a Value, Reference, or Embedded
    if (mf.hasAnnotation(Property.class)) {
      // make sure that the property type is supported
      if (mf.isSingleValue()
          && !mf.isTypeMongoCompatible()
          && !mapper.getConverters().hasSimpleValueConverter(mf)) {
        ve.add(
            new ConstraintViolation(
                Level.WARNING,
                mc,
                mf,
                getClass(),
                mf.getFullName()
                    + " is annotated as @"
                    + Property.class.getSimpleName()
                    + " but is a type that cannot be mapped simply (type is "
                    + mf.getType().getName()
                    + ")."));
      }
    }
  }
}
