package xyz.vopen.mixmicro.components.mongo.client.converters;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;

import java.util.UUID;

/**
 * provided by http://code.google.com/p/mix-mongo/issues/detail?id=320
 *
 * @author stummb
 * @author scotthernandez
 */
public class UUIDConverter extends TypeConverter implements SimpleValueConverter {

  /** Creates the Converter. */
  public UUIDConverter() {
    super(UUID.class);
  }

  @Override
  public Object decode(
      final Class targetClass, final Object fromDBObject, final MappedField optionalExtraInfo) {
    return fromDBObject == null ? null : UUID.fromString((String) fromDBObject);
  }

  @Override
  public Object encode(final Object value, final MappedField optionalExtraInfo) {
    return value == null ? null : value.toString();
  }
}
