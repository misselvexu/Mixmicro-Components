package xyz.vopen.mixmicro.components.enhance.dbm.stmt.query;

import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.ArgumentHolder;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.QueryBuilder.InternalQueryBuilderWrapper;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.Where;

import java.sql.SQLException;
import java.util.List;

/**
 * Internal class handling the SQL 'in' query part. Used by {@link Where#in}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class InSubQuery extends BaseComparison {

  private final InternalQueryBuilderWrapper subQueryBuilder;
  private final boolean in;

  public InSubQuery(
      String columnName,
      FieldType fieldType,
      InternalQueryBuilderWrapper subQueryBuilder,
      boolean in)
      throws SQLException {
    super(columnName, fieldType, null, true);
    this.subQueryBuilder = subQueryBuilder;
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
  public void appendValue(DatabaseType databaseType, StringBuilder sb, List<ArgumentHolder> argList)
      throws SQLException {
    sb.append('(');
    subQueryBuilder.appendStatementString(sb, argList);
    FieldType[] resultFieldTypes = subQueryBuilder.getResultFieldTypes();
    if (resultFieldTypes == null) {
      // we assume that if someone is doing a raw select, they know what they are doing
    } else if (resultFieldTypes.length != 1) {
      throw new SQLException(
          "There must be only 1 result column in sub-query but we found "
              + resultFieldTypes.length);
    } else if (fieldType.getSqlType() != resultFieldTypes[0].getSqlType()) {
      throw new SQLException(
          "Outer column "
              + fieldType
              + " is not the same type as inner column "
              + resultFieldTypes[0]);
    }
    sb.append(") ");
  }
}
