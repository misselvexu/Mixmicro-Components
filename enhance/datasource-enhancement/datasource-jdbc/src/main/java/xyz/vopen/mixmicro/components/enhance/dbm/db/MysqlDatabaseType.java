package xyz.vopen.mixmicro.components.enhance.dbm.db;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;

import java.util.List;

/**
 * MySQL database type information used to create the tables, etc..
 *
 * <p><b>NOTE:</b> By default the tables are created with the ENGINE=InnoDB suffix (see {@link
 * #DEFAULT_CREATE_TABLE_SUFFIX}. Use {@link #setCreateTableSuffix} to change that to "" to use the
 * default MyISAM storage engine, to choose another engine, or set other settings. For more
 * information about engines, see the 'SHOW ENGINES;' results from the MySQL command line tool.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MysqlDatabaseType extends BaseDatabaseType {

  /**
   * Default suffix to the CREATE TABLE statement. Change with the {@link #setCreateTableSuffix}
   * method.
   */
  public static final String DEFAULT_CREATE_TABLE_SUFFIX = "ENGINE=InnoDB";

  private static final String DATABASE_URL_PORTION = "mysql";
  private static final String DRIVER_CLASS_NAME_OLD = "com.mysql.jdbc.Driver";
  private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
  private static final String DATABASE_NAME = "MySQL";
  private String createTableSuffix = DEFAULT_CREATE_TABLE_SUFFIX;

  @Override
  public boolean isDatabaseUrlThisType(String url, String dbTypePart) {
    return DATABASE_URL_PORTION.equals(dbTypePart);
  }

  @Override
  protected String[] getDriverClassNames() {
    return new String[] {DRIVER_CLASS_NAME, DRIVER_CLASS_NAME_OLD};
  }

  @Override
  public String getDatabaseName() {
    return DATABASE_NAME;
  }

  /** Set the string that is appended to the end of a CREATE TABLE statement. */
  public void setCreateTableSuffix(String createTableSuffix) {
    this.createTableSuffix = createTableSuffix;
  }

  @Override
  protected void appendDateType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
    /**
     * TIMESTAMP in MySQL does some funky stuff with the last-modification time. Values are 'not
     * null' by default with an automatic default of CURRENT_TIMESTAMP. Strange design decision.
     */
    sb.append("DATETIME");
  }

  @Override
  protected void appendBooleanType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
    sb.append("TINYINT(1)");
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
    sb.append("AUTO_INCREMENT ");
    configureId(sb, fieldType, statementsBefore, additionalArgs, queriesAfter);
  }

  @Override
  public void appendCreateTableSuffix(StringBuilder sb) {
    sb.append(createTableSuffix);
    sb.append(' ');
  }

  @Override
  public boolean isTruncateSupported() {
    return true;
  }

  @Override
  public boolean isCreateIfNotExistsSupported() {
    return true;
  }

  @Override
  public boolean isCreateIndexIfNotExistsSupported() {
    return false;
  }
}
