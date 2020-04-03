package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

import java.lang.reflect.Field;
import java.sql.Timestamp;

/**
 * Type that persists a {@link java.sql.Date} object.
 *
 * <p>NOTE: This is <i>not</i> the same as the {@link java.util.Date} class handled with {@link
 * DateType}. If it recommended that you use the other Date class which is more standard to Java
 * programs.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class SqlDateType extends DateType {

  private static final SqlDateType singleTon = new SqlDateType();
  private static final DateStringFormatConfig sqlDateFormatConfig =
      new DateStringFormatConfig("yyyy-MM-dd");

  private SqlDateType() {
    super(SqlType.DATE, new Class<?>[] {java.sql.Date.class});
  }

  /** Here for others to subclass. */
  protected SqlDateType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static SqlDateType getSingleton() {
    return singleTon;
  }

  @Override
  public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
    Timestamp value = (Timestamp) sqlArg;
    return new java.sql.Date(value.getTime());
  }

  @Override
  public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
    java.sql.Date date = (java.sql.Date) javaObject;
    return new Timestamp(date.getTime());
  }

  @Override
  protected DateStringFormatConfig getDefaultDateFormatConfig() {
    return sqlDateFormatConfig;
  }

  @Override
  public boolean isValidForField(Field field) {
    return (field.getType() == java.sql.Date.class);
  }
}
