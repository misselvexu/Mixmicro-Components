package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules;

import xyz.vopen.mixmicro.components.mongo.client.annotations.Embedded;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Id;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Property;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Reference;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.util.Set;

/** @author ScottHenandez */
public class IdDoesNotMix extends FieldConstraint {

  @Override
  protected void check(
      final Mapper mapper,
      final MappedClass mc,
      final MappedField mf,
      final Set<ConstraintViolation> ve) {
    // an @Id field can not be a Value, Reference, or Embedded
    if (mf.hasAnnotation(Id.class)) {
      if (mf.hasAnnotation(Reference.class)
          || mf.hasAnnotation(Embedded.class)
          || mf.hasAnnotation(Property.class)) {
        ve.add(
            new ConstraintViolation(
                Level.FATAL,
                mc,
                mf,
                getClass(),
                mf.getFullName()
                    + " is annotated as @"
                    + Id.class.getSimpleName()
                    + " and cannot be mixed with other annotations (like @Reference)"));
      }
    }
  }
}
