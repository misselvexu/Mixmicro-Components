package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.SqlExceptionUtil;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;

/**
 * Type that persists a {@link BigInteger} object as a NUMERIC SQL database field.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class BigDecimalNumericType extends BaseDataType {

  private static final BigDecimalNumericType singleTon = new BigDecimalNumericType();

  private BigDecimalNumericType() {
    // this has no classes because {@link BigDecimalString} is the default
    super(SqlType.BIG_DECIMAL);
  }

  /** Here for others to subclass. */
  protected BigDecimalNumericType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static BigDecimalNumericType getSingleton() {
    return singleTon;
  }

  @Override
  public Object parseDefaultString(FieldType fieldType, String defaultStr) throws SQLException {
    try {
      return new BigDecimal(defaultStr);
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
    return results.getBigDecimal(columnPos);
  }

  @Override
  public boolean isAppropriateId() {
    return false;
  }

  @Override
  public boolean isEscapedValue() {
    return false;
  }

  @Override
  public Class<?> getPrimaryClass() {
    return BigDecimal.class;
  }
}
