package xyz.vopen.mixmicro.components.enhance.dbm.stmt;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;
import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.table.TableInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * Assists in building sql DELETE statements for a particular table in a particular database.
 *
 * @param <T> The class that the code will be operating on.
 * @param <ID> The class of the ID column associated with the class. The T class does not require an
 *     ID field. The class needs an ID parameter however so you can use Void or Object to satisfy
 *     the compiler.
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class DeleteBuilder<T, ID> extends StatementBuilder<T, ID> {

  // NOTE: any fields here should be added to the clear() method below

  public DeleteBuilder(DatabaseType databaseType, TableInfo<T, ID> tableInfo, Repository<T, ID> repository) {
    super(databaseType, tableInfo, repository, StatementType.DELETE);
  }

  /**
   * Build and return a prepared delete that can be used by {@link Repository#delete(PreparedDelete)}
   * method. If you change the where or make other calls you will need to re-call this method to
   * re-prepare the statement for execution.
   */
  public PreparedDelete<T> prepare() throws SQLException {
    return super.prepareStatement(null, false);
  }

  /** A short cut to {@link Repository#delete(PreparedDelete)}. */
  public int delete() throws SQLException {
    return repository.delete(prepare());
  }

  @Override
  public void reset() {
    // NOTE: this is here because it is in the other sub-classes
    super.reset();
  }

  @Override
  protected void appendStatementStart(StringBuilder sb, List<ArgumentHolder> argList) {
    sb.append("DELETE FROM ");
    databaseType.appendEscapedEntityName(sb, tableInfo.getTableName());
    sb.append(' ');
  }

  @Override
  protected void appendStatementEnd(StringBuilder sb, List<ArgumentHolder> argList) {
    // noop
  }
}
