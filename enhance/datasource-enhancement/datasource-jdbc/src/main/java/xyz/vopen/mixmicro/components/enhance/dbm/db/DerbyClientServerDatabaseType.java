package xyz.vopen.mixmicro.components.enhance.dbm.db;

/**
 * Derby database type information used to create the tables, etc.. This is for client connections
 * to a remote Derby server. For embedded databases, you should use {@link
 * DerbyEmbeddedDatabaseType}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DerbyClientServerDatabaseType extends DerbyEmbeddedDatabaseType {

  private static final String DRIVER_CLASS_NAME = "org.apache.derby.jdbc.ClientDriver";
  private static final String DATABASE_NAME = "Derby Client/Server";

  @Override
  public boolean isDatabaseUrlThisType(String url, String dbTypePart) {
    if (!DATABASE_URL_PORTION.equals(dbTypePart)) {
      return false;
    }
    // jdbc:derby://localhost:1527/MyDbTest;create=true';
    String[] parts = url.split(":");
    return (parts.length >= 3 && parts[2].startsWith("//"));
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
