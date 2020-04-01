package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;

/**
 * Type that persists a Byte object.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ByteObjectType extends BaseDataType {

  private static final ByteObjectType singleTon = new ByteObjectType();

  private ByteObjectType() {
    super(SqlType.BYTE, new Class<?>[] {Byte.class});
  }

  protected ByteObjectType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static ByteObjectType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) {
    return Byte.parseByte(defaultStr);
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return (Byte) results.getByte(columnPos);
  }

  @Override
  public Object convertIdNumber(Number number) {
    return (Byte) number.byteValue();
  }

  @Override
  public boolean isEscapedValue() {
    return false;
  }
}
