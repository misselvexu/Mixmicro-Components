package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules;

import xyz.vopen.mixmicro.components.mongo.client.annotations.Reference;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.LazyFeatureDependencies;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class LazyReferenceMissingDependencies extends FieldConstraint {

  @Override
  protected void check(
      final Mapper mapper,
      final MappedClass mc,
      final MappedField mf,
      final Set<ConstraintViolation> ve) {
    final Reference ref = mf.getAnnotation(Reference.class);
    if (ref != null) {
      if (ref.lazy()) {
        if (!LazyFeatureDependencies.testDependencyFullFilled()) {
          ve.add(
              new ConstraintViolation(
                  Level.SEVERE,
                  mc,
                  mf,
                  getClass(),
                  "Lazy references need CGLib and Proxytoys in the classpath."));
        }
      }
    }
  }
}
