package xyz.vopen.mixmicro.components.enhance.dbm.db;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;

import java.util.List;

/**
 * IBM DB2 database type information used to create the tables, etc..
 *
 * <p><b>WARNING:</b> I have not tested this unfortunately because of a lack of access to a DB2
 * instance. Love to get 1-2 hours of access to an database to test/tweak this. Undoubtably is it
 * wrong. Please contact us if you'd like to help with this class.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class Db2DatabaseType extends BaseDatabaseType {

  private static final String DATABASE_URL_PORTION = "db2";
  private static final String DATABASE_NAME = "DB2";
  private static final String DRIVER_CLASS_NAME = "COM.ibm.db2.jdbc.app.DB2Driver";

  @Override
  public boolean isDatabaseUrlThisType(String url, String dbTypePart) {
    return DATABASE_URL_PORTION.equals(dbTypePart);
  }

  @Override
  protected String[] getDriverClassNames() {
    return new String[] {DRIVER_CLASS_NAME};
  }

  @Override
  public String getDatabaseName() {
    return DATABASE_NAME;
  }

  @Override
  protected void appendBooleanType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
    sb.append("SMALLINT");
  }

  @Override
  protected void appendByteType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
    sb.append("SMALLINT");
  }

  @Override
  protected void appendByteArrayType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
    sb.append("VARCHAR [] FOR BIT DATA");
  }

  @Override
  protected void appendSerializableType(StringBuilder sb, FieldType fieldType, int fieldWidth) {
    sb.append("VARCHAR [] FOR BIT DATA");
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
    sb.append("GENERATED ALWAYS AS IDENTITY ");
    configureId(sb, fieldType, statementsBefore, additionalArgs, queriesAfter);
  }

  @Override
  public void appendEscapedEntityName(StringBuilder sb, String name) {
    sb.append('\"').append(name).append('\"');
  }

  @Override
  public boolean isOffsetSqlSupported() {
    // there is no easy way to do this in this database type
    return false;
  }
}
