package xyz.vopen.mixmicro.components.mongo.client.converters;

import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappingException;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @author scotthernandez
 */
public class CharacterConverter extends TypeConverter implements SimpleValueConverter {
  /** Creates the Converter. */
  public CharacterConverter() {
    super(char.class, Character.class);
  }

  @Override
  public Object decode(
      final Class targetClass, final Object fromDBObject, final MappedField optionalExtraInfo) {
    if (fromDBObject == null) {
      return null;
    }

    if (fromDBObject instanceof String) {
      final char[] chars = ((String) fromDBObject).toCharArray();
      if (chars.length == 1) {
        return chars[0];
      } else if (chars.length == 0) {
        return (char) 0;
      }
    }
    throw new MappingException(
        "Trying to map multi-character data to a single character: " + fromDBObject);
  }

  @Override
  public Object encode(final Object value, final MappedField optionalExtraInfo) {
    return value == null || value.equals('\0') ? null : String.valueOf(value);
  }
}
