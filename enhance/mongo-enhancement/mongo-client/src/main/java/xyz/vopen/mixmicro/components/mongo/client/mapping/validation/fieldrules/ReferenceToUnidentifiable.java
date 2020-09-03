package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules;

import xyz.vopen.mixmicro.components.mongo.client.annotations.Id;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Reference;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappingException;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class ReferenceToUnidentifiable extends FieldConstraint {

  @Override
  protected void check(
      final Mapper mapper,
      final MappedClass mc,
      final MappedField mf,
      final Set<ConstraintViolation> ve) {
    if (mf.hasAnnotation(Reference.class)) {
      final Class realType = (mf.isSingleValue()) ? mf.getType() : mf.getSubClass();

      if (realType == null) {
        throw new MappingException("Type is null for this MappedField: " + mf);
      }

      if ((!realType.isInterface() && mapper.getMappedClass(realType).getIdField() == null)) {
        ve.add(
            new ConstraintViolation(
                Level.FATAL,
                mc,
                mf,
                getClass(),
                mf.getFullName()
                    + " is annotated as a @"
                    + Reference.class.getSimpleName()
                    + " but the "
                    + mf.getType().getName()
                    + " class is missing the @"
                    + Id.class.getSimpleName()
                    + " annotation"));
      }
    }
  }
}
