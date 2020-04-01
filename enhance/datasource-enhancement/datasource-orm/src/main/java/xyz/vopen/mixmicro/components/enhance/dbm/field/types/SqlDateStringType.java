package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Date;

/**
 * Type that persists a {@link java.sql.Date} object.
 *
 * <p>NOTE: This is <i>not</i> the same as the {@link java.util.Date} class handled with {@link
 * DateType}. If it recommended that you use the other Date class which is more standard to Java
 * programs.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class SqlDateStringType extends DateStringType {

  private static final SqlDateStringType singleTon = new SqlDateStringType();

  private SqlDateStringType() {
    super(SqlType.STRING);
  }

  /** Here for others to subclass. */
  protected SqlDateStringType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static SqlDateStringType getSingleton() {
    return singleTon;
  }

  @Override
  public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos)
      throws SQLException {
    Date date = (Date) super.sqlArgToJava(fieldType, sqlArg, columnPos);
    return new java.sql.Date(date.getTime());
  }

  @Override
  public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
    java.sql.Date date = (java.sql.Date) javaObject;
    return super.javaToSqlArg(fieldType, new Date(date.getTime()));
  }

  @Override
  public boolean isValidForField(Field field) {
    return (field.getType() == java.sql.Date.class);
  }
}
