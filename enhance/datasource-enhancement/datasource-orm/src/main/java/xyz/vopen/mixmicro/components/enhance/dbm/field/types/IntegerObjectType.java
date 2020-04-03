package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;

/**
 * Type that persists a Integer object.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class IntegerObjectType extends BaseDataType {

  private static final IntegerObjectType singleTon = new IntegerObjectType();

  private IntegerObjectType() {
    super(SqlType.INTEGER, new Class<?>[] {Integer.class});
  }

  protected IntegerObjectType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static IntegerObjectType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) {
    return Integer.parseInt(defaultStr);
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return (Integer) results.getInt(columnPos);
  }

  @Override
  public Object convertIdNumber(Number number) {
    return (Integer) number.intValue();
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
      return (Integer) 1;
    } else {
      return ((Integer) currentValue) + 1;
    }
  }
}
