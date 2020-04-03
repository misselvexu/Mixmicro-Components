package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;

/**
 * Type that persists a boolean primitive.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class FloatObjectType extends BaseDataType {

  private static final FloatObjectType singleTon = new FloatObjectType();

  private FloatObjectType() {
    super(SqlType.FLOAT, new Class<?>[] {Float.class});
  }

  protected FloatObjectType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static FloatObjectType getSingleton() {
    return singleTon;
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return (Float) results.getFloat(columnPos);
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) {
    return Float.parseFloat(defaultStr);
  }

  @Override
  public boolean isEscapedValue() {
    return false;
  }
}
