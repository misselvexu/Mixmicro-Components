package xyz.vopen.mixmicro.components.enhance.dbm.db;

import xyz.vopen.mixmicro.components.enhance.dbm.field.DataPersister;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldConverter;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.types.BigDecimalStringType;

import java.util.List;

/**
 * Sqlite database type information used to create the tables, etc..
 *
 * <p>NOTE: We need this here because the Android and JDBC versions both subclasses it.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public abstract class BaseSqliteDatabaseType extends BaseDatabaseType {

  private static final FieldConverter booleanConverter = new BooleanNumberFieldConverter();

  @Override
  protected void appendLongType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
    /*
     * This is unfortunate. SQLIte requires that a generated-id have the string "INTEGER PRIMARY KEY AUTOINCREMENT"
     * even though the maximum generated value is 64-bit. See configureGeneratedId below.
     */
    if (fieldType.getSqlType() == SqlType.LONG && fieldType.isGeneratedId()) {
      sb.append("INTEGER");
    } else {
      sb.append("BIGINT");
    }
  }

  @Override
  protected void configureGeneratedId(
      String tableName,
      StringBuilder sb,
      FieldType fieldType,
      List<String> statementsBefore,
      List<String> statementsAfter,
      List<String> additionalArgs,
      List<String> queriesAfter) {
    /*
     * Even though the documentation talks about INTEGER, it is 64-bit with a maximum value of 9223372036854775807.
     * See http://www.sqlite.org/faq.html#q1 and http://www.sqlite.org/autoinc.html
     */
    if (fieldType.getSqlType() != SqlType.INTEGER && fieldType.getSqlType() != SqlType.LONG) {
      throw new IllegalArgumentException(
          "Sqlite requires that auto-increment generated-id be integer or long type");
    }
    sb.append("PRIMARY KEY AUTOINCREMENT ");
    // no additional call to configureId here
  }

  @Override
  protected boolean generatedIdSqlAtEnd() {
    return false;
  }

  @Override
  public boolean isVarcharFieldWidthSupported() {
    return false;
  }

  @Override
  public boolean isCreateTableReturnsZero() {
    // 'CREATE TABLE' statements seem to return 1 for some reason
    return false;
  }

  @Override
  public boolean isCreateIfNotExistsSupported() {
    return true;
  }

  @Override
  public FieldConverter getFieldConverter(DataPersister dataPersister, FieldType fieldType) {
    // we are only overriding certain types
    switch (dataPersister.getSqlType()) {
      case BOOLEAN:
        return booleanConverter;
      case BIG_DECIMAL:
        return BigDecimalStringType.getSingleton();
      default:
        return super.getFieldConverter(dataPersister, fieldType);
    }
  }

  @Override
  public void appendInsertNoColumns(StringBuilder sb) {
    sb.append("DEFAULT VALUES");
  }
}
