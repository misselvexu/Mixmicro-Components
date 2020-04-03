package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.SqlExceptionUtil;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;

/**
 * Type that persists a {@link BigInteger} object.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class BigDecimalStringType extends BaseDataType {

  private static final BigDecimalStringType singleTon = new BigDecimalStringType();
  public static int DEFAULT_WIDTH = 255;

  private BigDecimalStringType() {
    super(SqlType.STRING, new Class<?>[] {BigDecimal.class});
  }

  /** Here for others to subclass. */
  protected BigDecimalStringType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static BigDecimalStringType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
    try {
      return new BigDecimal(defaultStr).toString();
    } catch (IllegalArgumentException e) {
      throw SqlExceptionUtil.create(
          "Problems with field "
              + fieldType
              + " parsing default BigDecimal string '"
              + defaultStr
              + "'",
          e);
    }
  }

  @Override
  public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos)
      throws SQLException {
    return results.getString(columnPos);
  }

  @Override
  public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos)
      throws SQLException {
    try {
      return new BigDecimal((String) sqlArg);
    } catch (IllegalArgumentException e) {
      throw SqlExceptionUtil.create(
          "Problems with column " + columnPos + " parsing BigDecimal string '" + sqlArg + "'", e);
    }
  }

  @Override
  public Object javaToSqlArg(FieldType fieldType, Object obj) {
    BigDecimal bigInteger = (BigDecimal) obj;
    return bigInteger.toString();
  }

  @Override
  public int getDefaultWidth() {
    return DEFAULT_WIDTH;
  }

  @Override
  public boolean isAppropriateId() {
    return false;
  }
}
