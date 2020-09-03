package xyz.vopen.mixmicro.components.mongo.client.mapping.validation.fieldrules;

import org.bson.types.ObjectId;
import xyz.vopen.mixmicro.components.mongo.client.annotations.Serialized;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation;
import xyz.vopen.mixmicro.components.mongo.client.mapping.validation.ConstraintViolation.Level;
import xyz.vopen.mixmicro.components.mongo.client.utils.ReflectionUtils;

import java.util.Set;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class MapKeyDifferentFromString extends FieldConstraint {
  private static final String SUPPORTED = "(Map<String/Enum/Long/ObjectId/..., ?>)";

  @Override
  protected void check(
      final Mapper mapper,
      final MappedClass mc,
      final MappedField mf,
      final Set<ConstraintViolation> ve) {
    if (mf.isMap() && (!mf.hasAnnotation(Serialized.class))) {
      final Class<?> aClass = ReflectionUtils.getParameterizedClass(mf.getField(), 0);
      // WARN if not parameterized : null or Object...
      if (aClass == null || Object.class.equals(aClass)) {
        ve.add(
            new ConstraintViolation(
                Level.WARNING,
                mc,
                mf,
                getClass(),
                "Maps cannot be keyed by Object (Map<Object,?>); Use a parametrized type that is supported "
                    + SUPPORTED));
      } else if (!aClass.equals(String.class)
          && !aClass.equals(ObjectId.class)
          && !ReflectionUtils.isPrimitiveLike(aClass)) {
        ve.add(
            new ConstraintViolation(
                Level.FATAL,
                mc,
                mf,
                getClass(),
                "Maps must be keyed by a simple type "
                    + SUPPORTED
                    + "; "
                    + aClass
                    + " is not supported as a map key type."));
      }
    }
  }
}
