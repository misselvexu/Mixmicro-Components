package xyz.vopen.mixmicro.components.enhance.dbm.stmt;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.CloseableIterator;
import xyz.vopen.mixmicro.components.enhance.dbm.repository.GenericRawResults;
import xyz.vopen.mixmicro.components.enhance.dbm.repository.ObjectCache;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.IOUtils;
import xyz.vopen.mixmicro.components.enhance.dbm.support.CompiledStatement;
import xyz.vopen.mixmicro.components.enhance.dbm.support.ConnectionSource;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler for our raw results objects which does the conversion for various different results:
 * String[], Object[], and user defined T.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class RawResultsImpl<T> implements GenericRawResults<T> {

  private final String[] columnNames;
  private SelectIterator<T, Void> iterator;

  public RawResultsImpl(
      ConnectionSource connectionSource,
      DatabaseConnection connection,
      String query,
      Class<?> clazz,
      CompiledStatement compiledStmt,
      GenericRowMapper<T> rowMapper,
      ObjectCache objectCache)
      throws SQLException {
    iterator =
        new SelectIterator<T, Void>(
            clazz, null, rowMapper, connectionSource, connection, compiledStmt, query, objectCache);
    /*
     * NOTE: we _have_ to get these here before the results object is closed if there are no results
     */
    this.columnNames = iterator.getRawResults().getColumnNames();
  }

  @Override
  public int getNumberColumns() {
    return columnNames.length;
  }

  @Override
  public String[] getColumnNames() {
    return columnNames;
  }

  @Override
  public List<T> getResults() throws SQLException {
    List<T> results = new ArrayList<T>();
    try {
      while (iterator.hasNext()) {
        results.add(iterator.next());
      }
      return results;
    } finally {
      IOUtils.closeThrowSqlException(this, "raw results iterator");
    }
  }

  @Override
  public T getFirstResult() throws SQLException {
    try {
      if (iterator.hasNextThrow()) {
        return iterator.nextThrow();
      } else {
        return null;
      }
    } finally {
      IOUtils.closeThrowSqlException(this, "raw results iterator");
    }
  }

  @Override
  public CloseableIterator<T> iterator() {
    return iterator;
  }

  @Override
  public CloseableIterator<T> closeableIterator() {
    return iterator;
  }

  @Override
  public void close() throws IOException {
    if (iterator != null) {
      iterator.close();
      iterator = null;
    }
  }
}
