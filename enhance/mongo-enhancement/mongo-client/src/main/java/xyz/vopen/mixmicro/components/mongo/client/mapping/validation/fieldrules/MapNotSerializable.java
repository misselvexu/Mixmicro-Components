package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules;

import xyz.vopen.mixmicro.components.mongo.client.annotations.Serialized;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;
import xyz.vopen.mixmicro.components.mongo.client.utils.ReflectionUtils;

import java.io.Serializable;
import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class MapNotSerializable extends FieldConstraint {

  @Override
  protected void check(
      final Mapper mapper,
      final MappedClass mc,
      final MappedField mf,
      final Set<ConstraintViolation> ve) {
    if (mf.isMap()) {
      if (mf.hasAnnotation(Serialized.class)) {
        final Class<?> keyClass = ReflectionUtils.getParameterizedClass(mf.getField(), 0);
        final Class<?> valueClass = ReflectionUtils.getParameterizedClass(mf.getField(), 1);
        if (keyClass != null) {
          if (!Serializable.class.isAssignableFrom(keyClass)) {
            ve.add(
                new ConstraintViolation(
                    Level.FATAL,
                    mc,
                    mf,
                    getClass(),
                    "Key class (" + keyClass.getName() + ") is not Serializable"));
          }
        }
        if (valueClass != null) {
          if (!Serializable.class.isAssignableFrom(valueClass)) {
            ve.add(
                new ConstraintViolation(
                    Level.FATAL,
                    mc,
                    mf,
                    getClass(),
                    "Value class (" + valueClass.getName() + ") is not Serializable"));
          }
        }
      }
    }
  }
}
