package xyz.vopen.mixmicro.components.enhance.dbm.stmt.query;

import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.ArgumentHolder;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.StatementBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Internal class handling the SQL SET part used by UPDATE statements. Used by {@link
 * StatementBuilder#updateColumnExpression(String, String)}.
 *
 * <p>It's not a comparison per se but does have a columnName = value form so it works.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class SetExpression extends BaseComparison {

  public SetExpression(String columnName, FieldType fieldType, String string) throws SQLException {
    super(columnName, fieldType, string, true);
  }

  @Override
  public void appendOperation(StringBuilder sb) {
    sb.append("= ");
  }

  @Override
  protected void appendArgOrValue(
      DatabaseType databaseType,
      FieldType fieldType,
      StringBuilder sb,
      List<ArgumentHolder> selectArgList,
      Object argOrValue) {
    // we know it is a string so just append it
    sb.append(argOrValue).append(' ');
  }
}
