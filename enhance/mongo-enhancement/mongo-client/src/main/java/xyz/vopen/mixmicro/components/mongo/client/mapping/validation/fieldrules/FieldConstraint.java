package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ClassConstraint;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;

import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public abstract class FieldConstraint implements ClassConstraint {
  @Override
  public final void check(
      final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {
    for (final MappedField mf : mc.getPersistenceFields()) {
      check(mapper, mc, mf, ve);
    }
  }

  protected abstract void check(
      Mapper mapper, MappedClass mc, MappedField mf, Set<ConstraintViolation> ve);
}
