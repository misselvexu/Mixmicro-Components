package xyz.vopen.mixmicro.components.mongo.client.converters;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @author scotthernandez
 */
public class EnumConverter extends TypeConverter implements SimpleValueConverter {

  @Override
  @SuppressWarnings("unchecked")
  public Object decode(
      final Class targetClass, final Object fromDBObject, final MappedField optionalExtraInfo) {
    if (fromDBObject == null) {
      return null;
    }
    return Enum.valueOf(targetClass, fromDBObject.toString());
  }

  @Override
  public Object encode(final Object value, final MappedField optionalExtraInfo) {
    if (value == null) {
      return null;
    }

    return getName((Enum) value);
  }

  @Override
  protected boolean isSupported(final Class c, final MappedField optionalExtraInfo) {
    return c.isEnum();
  }

  private <T extends Enum> String getName(final T value) {
    return value.name();
  }
}
