package xyz.vopen.mixmicro.components.enhance.dbm.stmt.mapped;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;
import xyz.vopen.mixmicro.components.enhance.dbm.repository.ObjectCache;
import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseConnection;
import xyz.vopen.mixmicro.components.enhance.dbm.table.TableInfo;

import java.sql.SQLException;

/**
 * Mapped statement for querying for an object by a single field.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MappedQueryForFieldEq<T, ID> extends BaseMappedQuery<T, ID> {

  private final String label;

  protected MappedQueryForFieldEq(
      Repository<T, ID> repository,
      TableInfo<T, ID> tableInfo,
      String statement,
      FieldType[] argFieldTypes,
      FieldType[] resultsFieldTypes,
      String label) {
    super(repository, tableInfo, statement, argFieldTypes, resultsFieldTypes);
    this.label = label;
  }

  public static <T, ID> MappedQueryForFieldEq<T, ID> build(
      Repository<T, ID> repository, TableInfo<T, ID> tableInfo, FieldType idFieldType) throws SQLException {
    if (idFieldType == null) {
      idFieldType = tableInfo.getIdField();
      if (idFieldType == null) {
        throw new SQLException(
            "Cannot query-for-id with "
                + tableInfo.getDataClass()
                + " because it doesn't have an id field");
      }
    }
    DatabaseType databaseType = repository.getConnectionSource().getDatabaseType();
    String statement = buildStatement(databaseType, tableInfo, idFieldType);
    return new MappedQueryForFieldEq<T, ID>(
        repository,
        tableInfo,
        statement,
        new FieldType[] {idFieldType},
        tableInfo.getFieldTypes(),
        "query-for-id");
  }

  protected static <T, ID> String buildStatement(
      DatabaseType databaseType, TableInfo<T, ID> tableInfo, FieldType idFieldType) {
    // build the select statement by hand
    StringBuilder sb = new StringBuilder(64);
    appendTableName(databaseType, sb, "SELECT * FROM ", tableInfo.getTableName());
    appendWhereFieldEq(databaseType, idFieldType, sb, null);
    return sb.toString();
  }

  /** Query for an object in the database which matches the id argument. */
  public T execute(DatabaseConnection databaseConnection, ID id, ObjectCache objectCache)
      throws SQLException {
    if (objectCache != null) {
      T result = objectCache.get(clazz, id);
      if (result != null) {
        return result;
      }
    }
    Object[] args = new Object[] {convertIdToFieldObject(id)};
    // @SuppressWarnings("unchecked")
    Object result =
        databaseConnection.queryForOne(statement, args, argFieldTypes, this, objectCache);
    if (result == null) {
      logger.debug("{} using '{}' and {} args, got no results", label, statement, args.length);
    } else if (result == DatabaseConnection.MORE_THAN_ONE) {
      logger.error("{} using '{}' and {} args, got >1 results", label, statement, args.length);
      logArgs(args);
      throw new SQLException(label + " got more than 1 result: " + statement);
    } else {
      logger.debug("{} using '{}' and {} args, got 1 result", label, statement, args.length);
    }
    logArgs(args);
    @SuppressWarnings("unchecked")
    T castResult = (T) result;
    return castResult;
  }

  private void logArgs(Object[] args) {
    if (args.length > 0) {
      // need to do the (Object) cast to force args to be a single object and not an array
      logger.trace("{} arguments: {}", label, (Object) args);
    }
  }
}
