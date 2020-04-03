package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;

/**
 * Type that persists a String object.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class StringType extends BaseDataType {

  private static final StringType singleTon = new StringType();
  public static int DEFAULT_WIDTH = 255;

  private StringType() {
    super(SqlType.STRING, new Class<?>[] {String.class});
  }

  protected StringType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  protected StringType(SqlType sqlType) {
    super(sqlType);
  }

  public static StringType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) {
    return defaultStr;
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return results.getString(columnPos);
  }

  @Override
  public int getDefaultWidth() {
    return DEFAULT_WIDTH;
  }
}
