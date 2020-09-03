package xyz.vopen.mixmicro.components.mongo.client.converters;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.utils.ReflectionUtils;

import java.util.List;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @author scotthernandez
 */
public class StringConverter extends TypeConverter implements SimpleValueConverter {
  /** Creates the Converter. */
  public StringConverter() {
    super(String.class, String[].class);
  }

  @Override
  public Object decode(
      final Class targetClass, final Object fromDBObject, final MappedField optionalExtraInfo) {
    if (fromDBObject == null) {
      return null;
    }

    if (targetClass.equals(fromDBObject.getClass())) {
      return fromDBObject;
    }

    if (fromDBObject instanceof List) {
      final Class<?> type = targetClass.isArray() ? targetClass.getComponentType() : targetClass;
      return ReflectionUtils.convertToArray(type, (List<?>) fromDBObject);
    }

    if (targetClass.equals(String[].class)) {
      return new String[] {fromDBObject.toString()};
    }

    return fromDBObject.toString();
  }
}
