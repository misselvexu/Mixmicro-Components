package xyz.vopen.mixmicro.components.mongo.client.converters;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @author scotthernandez
 */
public class IdentityConverter extends TypeConverter {

  /**
   * Creates the Converter.
   *
   * @param types the types to pass through this converter
   */
  public IdentityConverter(final Class... types) {
    super(types);
  }

  @Override
  public Object decode(
      final Class targetClass, final Object fromDBObject, final MappedField optionalExtraInfo) {
    return fromDBObject;
  }

  @Override
  protected boolean isSupported(final Class c, final MappedField optionalExtraInfo) {
    return true;
  }
}
