package xyz.vopen.mixmicro.components.mongo.client.converters;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.utils.ReflectionUtils;

import java.util.List;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @author scotthernandez
 */
public class IntegerConverter extends TypeConverter implements SimpleValueConverter {
  /** Creates the Converter. */
  public IntegerConverter() {
    super(int.class, Integer.class, int[].class, Integer[].class);
  }

  @Override
  public Object decode(
      final Class targetClass, final Object val, final MappedField optionalExtraInfo) {
    if (val == null) {
      return null;
    }

    if (val instanceof Integer) {
      return val;
    }

    if (val instanceof Number) {
      return ((Number) val).intValue();
    }

    if (val instanceof List) {
      final Class<?> type = targetClass.isArray() ? targetClass.getComponentType() : targetClass;
      return ReflectionUtils.convertToArray(type, (List<?>) val);
    }

    return Integer.parseInt(val.toString());
  }
}
