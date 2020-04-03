package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;

/**
 * Type that persists a Double object.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DoubleObjectType extends BaseDataType {

  private static final DoubleObjectType singleTon = new DoubleObjectType();

  private DoubleObjectType() {
    super(SqlType.DOUBLE, new Class<?>[] {Double.class});
  }

  protected DoubleObjectType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static DoubleObjectType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) {
    return Double.parseDouble(defaultStr);
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return (Double) results.getDouble(columnPos);
  }

  @Override
  public boolean isEscapedValue() {
    return false;
  }
}
