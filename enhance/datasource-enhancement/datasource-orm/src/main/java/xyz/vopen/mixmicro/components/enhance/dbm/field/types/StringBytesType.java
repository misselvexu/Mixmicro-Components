package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.SqlExceptionUtil;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * Type that persists a String as a byte array.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class StringBytesType extends BaseDataType {

  private static final String DEFAULT_STRING_BYTES_CHARSET_NAME = "Unicode";

  private static final StringBytesType singleTon = new StringBytesType();

  private StringBytesType() {
    super(SqlType.BYTE_ARRAY);
  }

  /** Here for others to subclass. */
  protected StringBytesType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static StringBytesType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
    if (defaultStr == null) {
      return null;
    } else {
      try {
        return defaultStr.getBytes(getCharsetName(fieldType));
      } catch (UnsupportedEncodingException e) {
        throw SqlExceptionUtil.create("Could not convert default string: " + defaultStr, e);
      }
    }
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return results.getBytes(columnPos);
  }

  @Override
  public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos)
      throws SQLException {
    byte[] bytes = (byte[]) sqlArg;
    String charsetName = getCharsetName(fieldType);
    try {
      // NOTE: I can't use new String(bytes, Charset) because it was introduced in 1.6.
      return new String(bytes, charsetName);
    } catch (UnsupportedEncodingException e) {
      throw SqlExceptionUtil.create(
          "Could not convert string with charset name: " + charsetName, e);
    }
  }

  @Override
  public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
    String string = (String) javaObject;
    String charsetName = getCharsetName(fieldType);
    try {
      // NOTE: I can't use string.getBytes(Charset) because it was introduced in 1.6.
      return string.getBytes(charsetName);
    } catch (UnsupportedEncodingException e) {
      throw SqlExceptionUtil.create(
          "Could not convert string with charset name: " + charsetName, e);
    }
  }

  @Override
  public boolean isArgumentHolderRequired() {
    return true;
  }

  @Override
  public Object resultStringToJava(FieldType fieldType, String stringValue, int columnPos)
      throws SQLException {
    throw new SQLException("String-bytes type cannot be converted from string to Java");
  }

  @Override
  public Class<?> getPrimaryClass() {
    return String.class;
  }

  private String getCharsetName(FieldType fieldType) {
    if (fieldType == null || fieldType.getFormat() == null) {
      return DEFAULT_STRING_BYTES_CHARSET_NAME;
    } else {
      return fieldType.getFormat();
    }
  }
}
