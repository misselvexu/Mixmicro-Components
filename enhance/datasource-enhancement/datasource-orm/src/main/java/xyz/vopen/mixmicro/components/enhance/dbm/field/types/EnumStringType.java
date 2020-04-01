package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Type that persists an enum as its name produced by call @{link {@link Enum#name()}. You can also
 * use the {@link EnumIntegerType} to persist it's enum ordinal value. If you want to use the {@link
 * Enum#toString()} instead, see the {@link EnumToStringType}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class EnumStringType extends BaseEnumType {

  private static final EnumStringType singleTon = new EnumStringType();
  public static int DEFAULT_WIDTH = 100;

  private EnumStringType() {
    super(SqlType.STRING, new Class<?>[] {Enum.class});
  }

  /** Here for others to subclass. */
  protected EnumStringType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static EnumStringType getSingleton() {
    return singleTon;
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return results.getString(columnPos);
  }

  @Override
  public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos)
      throws SQLException {
    if (fieldType == null) {
      return sqlArg;
    }
    String value = (String) sqlArg;
    @SuppressWarnings("unchecked")
    Map<String, Enum<?>> enumStringMap = (Map<String, Enum<?>>) fieldType.getDataTypeConfigObj();
    if (enumStringMap == null) {
      return enumVal(fieldType, value, null, fieldType.getUnknownEnumVal());
    } else {
      return enumVal(fieldType, value, enumStringMap.get(value), fieldType.getUnknownEnumVal());
    }
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) {
    return defaultStr;
  }

  @Override
  public Object javaToSqlArg(FieldType fieldType, Object obj) {
    Enum<?> enumVal = (Enum<?>) obj;
    return getEnumName(enumVal);
  }

  @Override
  public Object makeConfigObject(FieldType fieldType) throws SQLException {
    Map<String, Enum<?>> enumStringMap = new HashMap<String, Enum<?>>();
    Enum<?>[] constants = (Enum<?>[]) fieldType.getType().getEnumConstants();
    if (constants == null) {
      throw new SQLException(
          "Could not get enum-constants for field "
              + fieldType
              + ", not an enum or maybe generic?");
    }
    for (Enum<?> enumVal : constants) {
      enumStringMap.put(getEnumName(enumVal), enumVal);
    }
    return enumStringMap;
  }

  @Override
  public int getDefaultWidth() {
    return DEFAULT_WIDTH;
  }

  protected String getEnumName(Enum<?> enumVal) {
    return enumVal.name();
  }
}
