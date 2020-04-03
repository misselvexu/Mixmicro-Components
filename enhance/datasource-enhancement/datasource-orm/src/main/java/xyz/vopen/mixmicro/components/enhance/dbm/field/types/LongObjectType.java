package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;

/**
 * Type that persists a Long object.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LongObjectType extends BaseDataType {

  private static final LongObjectType singleTon = new LongObjectType();

  private LongObjectType() {
    super(SqlType.LONG, new Class<?>[] {Long.class});
  }

  protected LongObjectType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static LongObjectType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) {
    return Long.parseLong(defaultStr);
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return (Long) results.getLong(columnPos);
  }

  @Override
  public Object convertIdNumber(Number number) {
    return (Long) number.longValue();
  }

  @Override
  public boolean isEscapedValue() {
    return false;
  }

  @Override
  public boolean isValidGeneratedType() {
    return true;
  }

  @Override
  public boolean isValidForVersion() {
    return true;
  }

  @Override
  public Object moveToNextValue(Object currentValue) {
    if (currentValue == null) {
      return (Long) 1L;
    } else {
      return ((Long) currentValue) + 1L;
    }
  }
}
