package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;

/**
 * Type that persists a Short object.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ShortObjectType extends BaseDataType {

  private static final ShortObjectType singleTon = new ShortObjectType();

  private ShortObjectType() {
    super(SqlType.SHORT, new Class<?>[] {Short.class});
  }

  protected ShortObjectType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static ShortObjectType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) {
    return Short.parseShort(defaultStr);
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return (Short) results.getShort(columnPos);
  }

  @Override
  public Object convertIdNumber(Number number) {
    return (Short) number.shortValue();
  }

  @Override
  public boolean isEscapedValue() {
    return false;
  }

  @Override
  public boolean isValidForVersion() {
    return true;
  }

  @Override
  public Object moveToNextValue(Object currentValue) {
    if (currentValue == null) {
      return (short) 1;
    } else {
      return (short) (((Short) currentValue) + 1);
    }
  }
}
