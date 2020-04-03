package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Base class for all of the {@link java.sql.Date} class types.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public abstract class BaseDateType extends BaseDataType {

  protected static final DateStringFormatConfig defaultDateFormatConfig =
      new DateStringFormatConfig("yyyy-MM-dd HH:mm:ss.SSSSSS");

  protected BaseDateType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  protected BaseDateType(SqlType sqlType) {
    super(sqlType);
  }

  protected static DateStringFormatConfig convertDateStringConfig(
      FieldType fieldType, DateStringFormatConfig defaultDateFormatConfig) {
    if (fieldType == null) {
      return defaultDateFormatConfig;
    }
    DateStringFormatConfig configObj = (DateStringFormatConfig) fieldType.getDataTypeConfigObj();
    if (configObj == null) {
      return defaultDateFormatConfig;
    } else {
      return (DateStringFormatConfig) configObj;
    }
  }

  protected static Date parseDateString(DateStringFormatConfig formatConfig, String dateStr)
      throws ParseException {
    DateFormat dateFormat = formatConfig.getDateFormat();
    return dateFormat.parse(dateStr);
  }

  protected static String normalizeDateString(DateStringFormatConfig formatConfig, String dateStr)
      throws ParseException {
    DateFormat dateFormat = formatConfig.getDateFormat();
    Date date = dateFormat.parse(dateStr);
    return dateFormat.format(date);
  }

  @Override
  public boolean isValidForVersion() {
    return true;
  }

  @Override
  public Object moveToNextValue(Object currentValue) {
    long newVal = System.currentTimeMillis();
    if (currentValue == null) {
      return new Date(newVal);
    } else if (newVal == ((Date) currentValue).getTime()) {
      return new Date(newVal + 1L);
    } else {
      return new Date(newVal);
    }
  }

  @Override
  public boolean isValidForField(Field field) {
    return (field.getType() == Date.class);
  }

  protected static class DateStringFormatConfig {
    final String dateFormatStr;
    private final ThreadLocal<DateFormat> threadLocal =
        new ThreadLocal<DateFormat>() {
          @Override
          protected DateFormat initialValue() {
            return new SimpleDateFormat(dateFormatStr);
          }
        };

    public DateStringFormatConfig(String dateFormatStr) {
      this.dateFormatStr = dateFormatStr;
    }

    public DateFormat getDateFormat() {
      return threadLocal.get();
    }

    @Override
    public String toString() {
      return dateFormatStr;
    }
  }
}
