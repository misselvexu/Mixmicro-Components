package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Type that persists a {@link java.sql.Timestamp} object as a String.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class TimeStampStringType extends DateStringType {

  private static final TimeStampStringType singleTon = new TimeStampStringType();

  private TimeStampStringType() {
    super(SqlType.STRING);
  }

  /** Here for others to subclass. */
  protected TimeStampStringType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static TimeStampStringType getSingleton() {
    return singleTon;
  }

  @Override
  public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos)
      throws SQLException {
    Date date = (Date) super.sqlArgToJava(fieldType, sqlArg, columnPos);
    return new Timestamp(date.getTime());
  }

  @Override
  public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
    Timestamp timeStamp = (Timestamp) javaObject;
    return super.javaToSqlArg(fieldType, new Date(timeStamp.getTime()));
  }

  @Override
  public boolean isValidForField(Field field) {
    return (field.getType() == java.sql.Timestamp.class);
  }

  @Override
  public Object moveToNextValue(Object currentValue) {
    long newVal = System.currentTimeMillis();
    if (currentValue == null) {
      return new java.sql.Timestamp(newVal);
    } else if (newVal == ((java.sql.Timestamp) currentValue).getTime()) {
      return new java.sql.Timestamp(newVal + 1L);
    } else {
      return new java.sql.Timestamp(newVal);
    }
  }
}
