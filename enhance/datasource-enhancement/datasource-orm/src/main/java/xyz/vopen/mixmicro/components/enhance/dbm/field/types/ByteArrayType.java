package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.SqlExceptionUtil;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Type that persists a byte[] object.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class ByteArrayType extends BaseDataType {

  private static final ByteArrayType singleTon = new ByteArrayType();

  private ByteArrayType() {
    super(SqlType.BYTE_ARRAY);
  }

  /** Here for others to subclass. */
  protected ByteArrayType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static ByteArrayType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
    return defaultStr == null ? null : getBytesImpl(fieldType, defaultStr);
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return (byte[]) results.getBytes(columnPos);
  }

  @Override
  public boolean isArgumentHolderRequired() {
    return true;
  }

  @Override
  public boolean dataIsEqual(Object fieldObj1, Object fieldObj2) {
    if (fieldObj1 == null) {
      return (fieldObj2 == null);
    } else if (fieldObj2 == null) {
      return false;
    } else {
      return Arrays.equals((byte[]) fieldObj1, (byte[]) fieldObj2);
    }
  }

  @Override
  public Object resultStringToJava(FieldType fieldType, String stringValue, int columnPos)
      throws SQLException {
    return getBytesImpl(fieldType, stringValue);
  }

  @Override
  public Class<?> getPrimaryClass() {
    return byte[].class;
  }

  private Object getBytesImpl(FieldType fieldType, String stringValue) throws SQLException {
    if (fieldType == null || fieldType.getFormat() == null) {
      return stringValue.getBytes();
    } else {
      try {
        return stringValue.getBytes(fieldType.getFormat());
      } catch (UnsupportedEncodingException e) {
        throw SqlExceptionUtil.create("Could not convert default string: " + stringValue, e);
      }
    }
  }
}
