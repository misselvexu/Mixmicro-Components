package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules;

import xyz.vopen.mixmicro.components.mongo.client.annotations.Reference;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class LazyReferenceOnArray extends FieldConstraint {

  @Override
  protected void check(
      final Mapper mapper,
      final MappedClass mc,
      final MappedField mf,
      final Set<ConstraintViolation> ve) {
    final Reference ref = mf.getAnnotation(Reference.class);
    if (ref != null && ref.lazy()) {
      final Class type = mf.getType();
      if (type.isArray()) {
        ve.add(
            new ConstraintViolation(
                Level.FATAL,
                mc,
                mf,
                getClass(),
                "The lazy attribute cannot be used for an Array. If you need a lazy array "
                    + "please use ArrayList instead."));
      }
    }
  }
}
