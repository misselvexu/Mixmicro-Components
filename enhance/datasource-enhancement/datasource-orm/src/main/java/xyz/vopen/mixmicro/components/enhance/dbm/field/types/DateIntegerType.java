package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.SqlExceptionUtil;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;
import java.util.Date;

/**
 * Persists the {@link java.util.Date} Java class as integer seconds (not milliseconds) since epoch.
 *
 * <p>NOTE: This is <i>not</i> the same as the {@link java.sql.Date} class.
 *
 * @author Noor Dawod, noor@fineswap.com
 * @since September 9, 2016
 */
public class DateIntegerType extends BaseDateType {

  private static final DateIntegerType singleTon = new DateIntegerType();

  private DateIntegerType() {
    super(SqlType.INTEGER);
  }

  /** Here for others to subclass. */
  protected DateIntegerType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static DateIntegerType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
    try {
      return Integer.parseInt(defaultStr);
    } catch (NumberFormatException e) {
      throw SqlExceptionUtil.create(
          "Problems with field " + fieldType + " parsing default date-integer value: " + defaultStr,
          e);
    }
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return results.getInt(columnPos);
  }

  @Override
  public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
    return new Date(((Integer) sqlArg) * 1000L);
  }

  @Override
  public Object javaToSqlArg(FieldType fieldType, Object obj) {
    Date date = (Date) obj;
    return (int) (date.getTime() / 1000);
  }

  @Override
  public boolean isEscapedValue() {
    return false;
  }

  @Override
  public Class<?> getPrimaryClass() {
    return Date.class;
  }
}
