package xyz.vopen.mixmicro.components.enhance.dbm.jdbc;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.ObjectCache;
import xyz.vopen.mixmicro.components.enhance.dbm.field.SqlType;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.IOUtils;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.StatementBuilder.StatementType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.CompiledStatement;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Wrapper around a {@link PreparedStatement} object which we delegate to.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class JdbcCompiledStatement implements CompiledStatement {

  private final PreparedStatement preparedStatement;
  private final StatementType type;
  private final boolean cacheStore;
  private ResultSetMetaData metaData = null;

  public JdbcCompiledStatement(
      PreparedStatement preparedStatement, StatementType type, boolean cacheStore) {
    this.preparedStatement = preparedStatement;
    this.type = type;
    this.cacheStore = cacheStore;
  }

  @Override
  public int getColumnCount() throws SQLException {
    if (metaData == null) {
      metaData = preparedStatement.getMetaData();
    }
    return metaData.getColumnCount();
  }

  @Override
  public String getColumnName(int column) throws SQLException {
    if (metaData == null) {
      metaData = preparedStatement.getMetaData();
    }
    return metaData.getColumnName(column + 1);
  }

  @Override
  public int runUpdate() throws SQLException {
    // this can be a UPDATE, DELETE, or ... just not a SELECT
    if (!type.isOkForUpdate()) {
      throw new IllegalArgumentException("Cannot call update on a " + type + " statement");
    }
    return preparedStatement.executeUpdate();
  }

  @Override
  public DatabaseResults runQuery(ObjectCache objectCache) throws SQLException {
    if (!type.isOkForQuery()) {
      throw new IllegalArgumentException("Cannot call query on a " + type + " statement");
    }
    return new JdbcDatabaseResults(
        preparedStatement, preparedStatement.executeQuery(), objectCache, cacheStore);
  }

  @Override
  public int runExecute() throws SQLException {
    if (!type.isOkForExecute()) {
      throw new IllegalArgumentException("Cannot call execute on a " + type + " statement");
    }
    preparedStatement.execute();
    return preparedStatement.getUpdateCount();
  }

  @Override
  public void close() throws IOException {
    try {
      preparedStatement.close();
    } catch (SQLException e) {
      throw new IOException("could not close prepared statement", e);
    }
  }

  @Override
  public void closeQuietly() {
    IOUtils.closeQuietly(this);
  }

  @Override
  public void cancel() throws SQLException {
    preparedStatement.cancel();
  }

  @Override
  public void setObject(int parameterIndex, Object obj, SqlType sqlType) throws SQLException {
    if (obj == null) {
      preparedStatement.setNull(parameterIndex + 1, TypeValMapper.getTypeValForSqlType(sqlType));
    } else {
      preparedStatement.setObject(
          parameterIndex + 1, obj, TypeValMapper.getTypeValForSqlType(sqlType));
    }
  }

  @Override
  public void setMaxRows(int max) throws SQLException {
    preparedStatement.setMaxRows(max);
  }

  @Override
  public void setQueryTimeout(long millis) throws SQLException {
    preparedStatement.setQueryTimeout(Long.valueOf(millis).intValue() / 1000);
  }

  /**
   * Called by {@link JdbcDatabaseResults#next()} to get more results into the existing ResultSet.
   */
  boolean getMoreResults() throws SQLException {
    return preparedStatement.getMoreResults();
  }
}
