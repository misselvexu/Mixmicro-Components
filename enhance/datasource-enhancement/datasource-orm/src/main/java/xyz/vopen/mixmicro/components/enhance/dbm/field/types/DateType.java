package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.SqlExceptionUtil;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;

/**
 * Type that persists a {@link java.util.Date} object.
 *
 * <p>NOTE: This is <i>not</i> the same as the {@link java.sql.Date} class that is handled by {@link
 * SqlDateType}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DateType extends BaseDateType {

  private static final DateType singleTon = new DateType();

  private DateType() {
    super(SqlType.DATE, new Class<?>[] {java.util.Date.class});
  }

  protected DateType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static DateType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
    DateStringFormatConfig dateFormatConfig =
        convertDateStringConfig(fieldType, getDefaultDateFormatConfig());
    try {
      return new Timestamp(parseDateString(dateFormatConfig, defaultStr).getTime());
    } catch (ParseException e) {
      throw SqlExceptionUtil.create(
          "Problems parsing default date string '"
              + defaultStr
              + "' using '"
              + dateFormatConfig
              + '\'',
          e);
    }
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return results.getTimestamp(columnPos);
  }

  @Override
  public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
    Timestamp value = (Timestamp) sqlArg;
    return new java.util.Date(value.getTime());
  }

  @Override
  public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
    java.util.Date date = (java.util.Date) javaObject;
    return new Timestamp(date.getTime());
  }

  @Override
  public boolean isArgumentHolderRequired() {
    return true;
  }

  /** Return the default date format configuration. */
  protected DateStringFormatConfig getDefaultDateFormatConfig() {
    return defaultDateFormatConfig;
  }
}
