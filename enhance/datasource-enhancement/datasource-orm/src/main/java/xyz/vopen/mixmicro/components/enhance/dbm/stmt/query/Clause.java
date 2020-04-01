package xyz.vopen.mixmicro.components.enhance.dbm.stmt.query;

import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.ArgumentHolder;

import java.sql.SQLException;
import java.util.List;

/**
 * Internal marker class for query clauses.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface Clause {

  /**
   * Add to the string-builder the appropriate SQL for this clause.
   *
   * @param tableName Name of the table to prepend to any column names or null to be ignored.
   */
  public void appendSql(
      DatabaseType databaseType, String tableName, StringBuilder sb, List<ArgumentHolder> argList)
      throws SQLException;
}
