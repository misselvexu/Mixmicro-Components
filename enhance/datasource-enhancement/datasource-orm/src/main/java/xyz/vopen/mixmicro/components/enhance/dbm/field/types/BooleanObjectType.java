package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;

/**
 * Type that persists a Boolean object.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class BooleanObjectType extends BaseDataType {

  private static final BooleanObjectType singleTon = new BooleanObjectType();

  private BooleanObjectType() {
    super(SqlType.BOOLEAN, new Class<?>[] {Boolean.class});
  }

  protected BooleanObjectType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  protected BooleanObjectType(SqlType sqlType) {
    super(sqlType);
  }

  public static BooleanObjectType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) {
    return Boolean.parseBoolean(defaultStr);
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return (Boolean) results.getBoolean(columnPos);
  }

  @Override
  public boolean isEscapedValue() {
    return false;
  }

  @Override
  public boolean isAppropriateId() {
    return false;
  }
}
