package xyz.vopen.mixmicro.components.enhance.dbm.db;

import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;

import java.util.List;

/**
 * H2 database type information used to create the tables, etc..
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class H2DatabaseType extends BaseDatabaseType {

  private static final String DATABASE_URL_PORTION = "h2";
  private static final String DRIVER_CLASS_NAME = "org.h2.Driver";
  private static final String DATABASE_NAME = "H2";

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
  public void appendLimitValue(StringBuilder sb, long limit, Long offset) {
    sb.append("LIMIT ");
    if (offset != null) {
      sb.append(offset).append(',');
    }
    sb.append(limit).append(' ');
  }

  @Override
  public boolean isOffsetLimitArgument() {
    return true;
  }

  @Override
  public void appendOffsetValue(StringBuilder sb, long offset) {
    throw new IllegalStateException("Offset is part of the LIMIT in database type " + getClass());
  }

  @Override
  public boolean isTruncateSupported() {
    return true;
  }

  @Override
  public boolean isCreateIfNotExistsSupported() {
    return true;
  }
}
