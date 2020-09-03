package xyz.vopen.mixmicro.components.mongo.client.converters;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;

import java.lang.reflect.Array;
import java.util.List;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @author scotthernandez
 */
public class ShortConverter extends TypeConverter implements SimpleValueConverter {
  /** Creates the Converter. */
  public ShortConverter() {
    super(short.class, Short.class, short[].class, Short[].class);
  }

  @Override
  public Object decode(
      final Class targetClass, final Object val, final MappedField optionalExtraInfo) {
    if (val == null) {
      return null;
    }

    if (val instanceof Short) {
      return val;
    }

    if (val instanceof Number) {
      return ((Number) val).shortValue();
    }

    if (val instanceof List) {
      final Class<?> type = targetClass.isArray() ? targetClass.getComponentType() : targetClass;
      return convertToArray(type, (List<?>) val);
    }

    return Short.parseShort(val.toString());
  }

  Object convertToArray(final Class type, final List<?> values) {
    final Object array = Array.newInstance(type, values.size());
    for (int i = 0; i < values.size(); i++) {
      Array.set(array, i, ((Number) values.get(i)).shortValue());
    }
    return array;
  }
}
