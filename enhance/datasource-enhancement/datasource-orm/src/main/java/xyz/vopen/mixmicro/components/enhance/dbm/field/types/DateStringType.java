package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.SqlExceptionUtil;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Type that persists a {@link java.util.Date} object as a String.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DateStringType extends BaseDateType {

  private static final DateStringType singleTon = new DateStringType();
  public static int DEFAULT_WIDTH = 50;

  private DateStringType() {
    super(SqlType.STRING);
  }

  protected DateStringType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  protected DateStringType(SqlType sqlType) {
    super(sqlType);
  }

  public static DateStringType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
    DateStringFormatConfig formatConfig =
        convertDateStringConfig(fieldType, defaultDateFormatConfig);
    try {
      // we parse to make sure it works and then format it again
      return normalizeDateString(formatConfig, defaultStr);
    } catch (ParseException e) {
      throw SqlExceptionUtil.create(
          "Problems with field "
              + fieldType
              + " parsing default date-string '"
              + defaultStr
              + "' using '"
              + formatConfig
              + "'",
          e);
    }
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return results.getString(columnPos);
  }

  @Override
  public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos)
      throws SQLException {
    String value = (String) sqlArg;
    DateStringFormatConfig formatConfig =
        convertDateStringConfig(fieldType, defaultDateFormatConfig);
    try {
      return parseDateString(formatConfig, value);
    } catch (ParseException e) {
      throw SqlExceptionUtil.create(
          "Problems with column "
              + columnPos
              + " parsing date-string '"
              + value
              + "' using '"
              + formatConfig
              + "'",
          e);
    }
  }

  @Override
  public Object javaToSqlArg(FieldType fieldType, Object obj) {
    DateFormat dateFormat =
        convertDateStringConfig(fieldType, defaultDateFormatConfig).getDateFormat();
    return dateFormat.format((Date) obj);
  }

  @Override
  public Object makeConfigObject(FieldType fieldType) {
    String format = fieldType.getFormat();
    if (format == null) {
      return defaultDateFormatConfig;
    } else {
      return new DateStringFormatConfig(format);
    }
  }

  @Override
  public int getDefaultWidth() {
    return DEFAULT_WIDTH;
  }

  @Override
  public Object resultStringToJava(FieldType fieldType, String stringValue, int columnPos)
      throws SQLException {
    return sqlArgToJava(fieldType, stringValue, columnPos);
  }

  @Override
  public Class<?> getPrimaryClass() {
    return byte[].class;
  }
}
