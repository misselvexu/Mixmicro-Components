package xyz.vopen.mixmicro.components.enhance.dbm.stmt.query;

import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.ArgumentHolder;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.Where;

import java.sql.SQLException;
import java.util.List;

/**
 * Internal class handling the SQL 'IS NOT NULL' comparison query part. Used by {@link
 * Where#isNull}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class IsNotNull extends BaseComparison {

  public IsNotNull(String columnName, FieldType fieldType) throws SQLException {
    super(columnName, fieldType, null, false);
  }

  @Override
  public void appendOperation(StringBuilder sb) {
    sb.append("IS NOT NULL ");
  }

  @Override
  public void appendValue(
      DatabaseType databaseType, StringBuilder sb, List<ArgumentHolder> argList) {
    // there is no value
  }
}
