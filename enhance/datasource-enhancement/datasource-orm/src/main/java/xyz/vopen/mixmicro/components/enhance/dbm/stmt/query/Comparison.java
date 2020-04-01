package xyz.vopen.mixmicro.components.enhance.dbm.stmt.query;

import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.ArgumentHolder;

import java.sql.SQLException;
import java.util.List;

/**
 * Internal interfaces which define a comparison operation.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
interface Comparison extends Clause {

  /** Return the column-name associated with the comparison. */
  public String getColumnName();

  /** Add the operation used in this comparison to the string builder. */
  public void appendOperation(StringBuilder sb);

  /** Add the value of the comparison to the string builder. */
  public void appendValue(DatabaseType databaseType, StringBuilder sb, List<ArgumentHolder> argList)
      throws SQLException;
}
