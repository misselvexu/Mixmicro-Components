package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.classrules;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ClassConstraint;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;

import java.util.Map;
import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class EntityCannotBeMapOrIterable implements ClassConstraint {

  @Override
  public void check(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {

    if (mc.getEntityAnnotation() != null
        && (Map.class.isAssignableFrom(mc.getClazz())
            || Iterable.class.isAssignableFrom(mc.getClazz()))) {
      ve.add(
          new ConstraintViolation(
              Level.FATAL, mc, getClass(), "Entities cannot implement Map/Iterable"));
    }
  }
}
