package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

import java.lang.reflect.Field;
import java.sql.SQLException;

/**
 * Base class for the enum classes to provide a utility method.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public abstract class BaseEnumType extends BaseDataType {

  protected BaseEnumType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  protected BaseEnumType(SqlType sqlType) {
    super(sqlType);
  }

  protected static Enum<?> enumVal(
      FieldType fieldType, Object val, Enum<?> enumVal, Enum<?> unknownEnumVal)
      throws SQLException {
    if (enumVal != null) {
      return enumVal;
    } else if (unknownEnumVal == null) {
      throw new SQLException("Cannot get enum value of '" + val + "' for field " + fieldType);
    } else {
      return unknownEnumVal;
    }
  }

  @Override
  public boolean isValidForField(Field field) {
    return field.getType().isEnum();
  }
}
