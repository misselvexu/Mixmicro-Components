package xyz.vopen.mixmicro.components.enhance.dbm.stmt.query;

import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.ArgumentHolder;

import java.util.List;

/**
 * Raw part of the where to just stick in a string in the middle of the WHERE. It is up to the user
 * to do so properly.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class Raw implements Clause {

  private final String statement;
  private final ArgumentHolder[] args;

  public Raw(String statement, ArgumentHolder[] args) {
    this.statement = statement;
    this.args = args;
  }

  @Override
  public void appendSql(
      DatabaseType databaseType, String tableName, StringBuilder sb, List<ArgumentHolder> argList) {
    sb.append(statement);
    sb.append(' ');
    for (ArgumentHolder arg : args) {
      argList.add(arg);
    }
  }
}
