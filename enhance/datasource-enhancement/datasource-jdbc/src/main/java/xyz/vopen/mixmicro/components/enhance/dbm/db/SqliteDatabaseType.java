package xyz.vopen.mixmicro.components.enhance.dbm.db;

/**
 * Sqlite database type information used to create the tables, etc..
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class SqliteDatabaseType extends BaseSqliteDatabaseType {

  private static final String DATABASE_URL_PORTION = "sqlite";
  private static final String DRIVER_CLASS_NAME = "org.sqlite.JDBC";
  private static final String DATABASE_NAME = "SQLite";

  public SqliteDatabaseType() {}

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
  public boolean isNestedSavePointsSupported() {
    return false;
  }

  @Override
  public void appendOffsetValue(StringBuilder sb, long offset) {
    throw new IllegalStateException("Offset is part of the LIMIT in database type " + getClass());
  }
}
