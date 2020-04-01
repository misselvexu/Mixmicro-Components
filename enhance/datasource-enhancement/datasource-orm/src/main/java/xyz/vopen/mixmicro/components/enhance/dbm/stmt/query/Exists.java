package xyz.vopen.mixmicro.components.enhance.dbm.stmt.query;

import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.ArgumentHolder;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.QueryBuilder.InternalQueryBuilderWrapper;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.Where;

import java.sql.SQLException;
import java.util.List;

/**
 * Internal class handling the SQL 'EXISTS' query part. Used by {@link Where#exists}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class Exists implements Clause {

  private final InternalQueryBuilderWrapper subQueryBuilder;

  public Exists(InternalQueryBuilderWrapper subQueryBuilder) {
    this.subQueryBuilder = subQueryBuilder;
  }

  @Override
  public void appendSql(
      DatabaseType databaseType, String tableName, StringBuilder sb, List<ArgumentHolder> argList)
      throws SQLException {
    sb.append("EXISTS (");
    subQueryBuilder.appendStatementString(sb, argList);
    sb.append(") ");
  }
}
