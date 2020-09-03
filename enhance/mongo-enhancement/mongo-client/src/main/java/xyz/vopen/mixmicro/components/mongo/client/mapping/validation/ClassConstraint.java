package xyz.vopen.mixmicro.components.mongo.client.mapping.validation;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;

import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public interface ClassConstraint {
  /**
   * Check that a MappedClass meets the constraint
   *
   * @param mc the MappedClass to check
   * @param ve the set of violations
   * @param mapper the Mapper to use for validation
   */
  void check(Mapper mapper, MappedClass mc, Set<ConstraintViolation> ve);
}
