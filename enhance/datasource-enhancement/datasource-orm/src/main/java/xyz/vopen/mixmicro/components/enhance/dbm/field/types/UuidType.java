package xyz.vopen.mixmicro.components.enhance.dbm.field.types;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.SqlExceptionUtil;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Type that persists a {@link UUID} object using a database String. This is not to be confused with
 * the native UUID types supported by some databases.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class UuidType extends BaseDataType {

  private static final UuidType singleTon = new UuidType();
  public static int DEFAULT_WIDTH = 48;

  private UuidType() {
    super(SqlType.STRING, new Class<?>[] {UUID.class});
  }

  protected UuidType(SqlType sqlType) {
    super(sqlType);
  }

  /** Here for others to subclass. */
  protected UuidType(SqlType sqlType, Class<?>[] classes) {
    super(sqlType, classes);
  }

  public static UuidType getSingleton() {
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
  public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos)
      throws SQLException {
    String uuidStr = (String) sqlArg;
    try {
      return java.util.UUID.fromString(uuidStr);
    } catch (IllegalArgumentException e) {
      throw SqlExceptionUtil.create(
          "Problems with column " + columnPos + " parsing UUID-string '" + uuidStr + "'", e);
    }
  }

  @Override
  public Object javaToSqlArg(FieldType fieldType, Object obj) {
    UUID uuid = (UUID) obj;
    return uuid.toString();
  }

  @Override
  public boolean isValidGeneratedType() {
    return true;
  }

  @Override
  public boolean isSelfGeneratedId() {
    return true;
  }

  @Override
  public Object generateId() {
    return java.util.UUID.randomUUID();
  }

  @Override
  public int getDefaultWidth() {
    return DEFAULT_WIDTH;
  }
}
