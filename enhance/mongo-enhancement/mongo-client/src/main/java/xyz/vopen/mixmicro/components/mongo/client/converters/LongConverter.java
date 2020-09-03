package xyz.vopen.mixmicro.components.mongo.client.converters;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.utils.ReflectionUtils;

import java.util.List;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @author scotthernandez
 */
public class LongConverter extends TypeConverter implements SimpleValueConverter {

  /** Creates the Converter. */
  public LongConverter() {
    super(long.class, Long.class, long[].class, Long[].class);
  }

  @Override
  public Object decode(
      final Class targetClass, final Object val, final MappedField optionalExtraInfo) {
    if (val == null) {
      return null;
    }

    if (val instanceof Long) {
      return val;
    }

    if (val instanceof Number) {
      return ((Number) val).longValue();
    }

    if (val instanceof List) {
      final Class<?> type = targetClass.isArray() ? targetClass.getComponentType() : targetClass;
      return ReflectionUtils.convertToArray(type, (List<?>) val);
    }

    return Long.parseLong(val.toString());
  }
}
