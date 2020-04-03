package xyz.vopen.mixmicro.components.enhance.dbm.db;

/**
 * MariaDB database type information used to create the tables, etc.. It is an extension of MySQL.
 *
 * @author kratorius
 */
public class MariaDbDatabaseType extends MysqlDatabaseType {

  private static final String DATABASE_URL_PORTION = "mariadb";
  private static final String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
  private static final String DATABASE_NAME = "MariaDB";

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
}
