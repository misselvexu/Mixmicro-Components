package xyz.vopen.mixmicro.components.boot.dbm.multidatasource;

import xyz.vopen.mixmicro.components.enhance.dbm.support.ConnectionSource;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseConnection;
import xyz.vopen.mixmicro.components.boot.dbm.multidatasource.connection.MultiConnectionSourceFactoryBean;
import xyz.vopen.mixmicro.components.boot.dbm.multidatasource.exception.MultiDataSourceRepositoryException;

import java.io.Serializable;

/**
 * {@link AbstractNativeConnectionSource}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-14.
 */
public abstract class AbstractNativeConnectionSource extends MultiConnectionSourceFactoryBean {

  private final ThreadLocal<NativeConnection> local = new ThreadLocal<>();

  /**
   * This method will return connection source instance of {@link ConnectionSource}
   *
   * @return return initialized instance of {@link ConnectionSource}
   */
  @Override
  public ConnectionSource getConnection(String schema) {
    return super.getConnection(schema);
  }

  /**
   * This method will return database connection instance of {@link DatabaseConnection}
   *
   * @param schema database schema
   * @param tableName database schema target table name
   * @return instance of {@link DatabaseConnection}
   * @see #releaseDatabaseConnection()
   */
  public DatabaseConnection getDatabaseConnection(String schema, String tableName) {
    try {
      ConnectionSource connectionSource = getConnection(schema);
      DatabaseConnection databaseConnection = connectionSource.getReadWriteConnection(tableName);
      NativeConnection connection = new NativeConnection(connectionSource, databaseConnection);
      local.set(connection);
      return databaseConnection;
    } catch (Exception e) {
      throw new MultiDataSourceRepositoryException(e);
    }
  }

  /**
   * This method will release database connection instance of {@link DatabaseConnection}
   *
   * <p>
   */
  public void releaseDatabaseConnection() {
    try {
      NativeConnection connection = local.get();
      if (connection != null) {
        connection.releaseDatabaseConnection();
      }
    } catch (Exception ignored) {
    } finally{
      local.remove();
    }
  }

  private static class NativeConnection implements Serializable {
    ConnectionSource connectionSource;
    DatabaseConnection databaseConnection;

    public NativeConnection(
        ConnectionSource connectionSource, DatabaseConnection databaseConnection) {
      this.connectionSource = connectionSource;
      this.databaseConnection = databaseConnection;
    }

    void releaseDatabaseConnection() {
      try {
        if (connectionSource != null && databaseConnection != null) {
          connectionSource.releaseConnection(databaseConnection);
        }
      } catch (Exception ignored) {
      }
    }
  }
}
