package xyz.vopen.mixmicro.components.enhance.dbm.stmt.query;

import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.ArgumentHolder;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.Where;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Internal class handling the SQL 'in' query part. Used by {@link Where#in}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class In extends BaseComparison {

  private final boolean in;
  private Iterable<?> objects;

  public In(String columnName, FieldType fieldType, Iterable<?> objects, boolean in)
      throws SQLException {
    super(columnName, fieldType, null, true);
    this.objects = objects;
    this.in = in;
  }

  public In(String columnName, FieldType fieldType, Object[] objects, boolean in)
      throws SQLException {
    super(columnName, fieldType, null, true);
    // grrrr, Object[] should be Iterable
    this.objects = Arrays.asList(objects);
    this.in = in;
  }

  @Override
  public void appendOperation(StringBuilder sb) {
    if (in) {
      sb.append("IN ");
    } else {
      sb.append("NOT IN ");
    }
  }

  @Override
  public void appendValue(
      DatabaseType databaseType, StringBuilder sb, List<ArgumentHolder> columnArgList)
      throws SQLException {
    sb.append('(');
    boolean first = true;
    for (Object value : objects) {
      if (value == null) {
        throw new IllegalArgumentException("one of the IN values for '" + columnName + "' is null");
      }
      if (first) {
        first = false;
      } else {
        sb.append(',');
      }
      // for each of our arguments, add it to the output
      super.appendArgOrValue(databaseType, fieldType, sb, columnArgList, value);
    }
    sb.append(") ");
  }
}
