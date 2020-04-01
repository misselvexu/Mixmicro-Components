package xyz.vopen.mixmicro.components.enhance.dbm.stmt.mapped;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;
import xyz.vopen.mixmicro.components.enhance.dbm.repository.ObjectCache;
import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.SqlExceptionUtil;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseConnection;
import xyz.vopen.mixmicro.components.enhance.dbm.table.TableInfo;

import java.sql.SQLException;

/**
 * A mapped statement for deleting an object.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MappedDelete<T, ID> extends BaseMappedStatement<T, ID> {

  private MappedDelete(
      Repository<T, ID> repository, TableInfo<T, ID> tableInfo, String statement, FieldType[] argFieldTypes) {
    super(repository, tableInfo, statement, argFieldTypes);
  }

  public static <T, ID> MappedDelete<T, ID> build(Repository<T, ID> repository, TableInfo<T, ID> tableInfo)
      throws SQLException {
    FieldType idField = tableInfo.getIdField();
    if (idField == null) {
      throw new SQLException(
          "Cannot delete from "
              + tableInfo.getDataClass()
              + " because it doesn't have an id field");
    }
    StringBuilder sb = new StringBuilder(64);
    DatabaseType databaseType = repository.getConnectionSource().getDatabaseType();
    appendTableName(databaseType, sb, "DELETE FROM ", tableInfo.getTableName());
    appendWhereFieldEq(databaseType, idField, sb, null);
    return new MappedDelete<T, ID>(repository, tableInfo, sb.toString(), new FieldType[] {idField});
  }

  /** Delete the object from the database. */
  public int delete(DatabaseConnection databaseConnection, T data, ObjectCache objectCache)
      throws SQLException {
    try {
      Object[] args = getFieldObjects(data);
      int rowC = databaseConnection.delete(statement, args, argFieldTypes);
      logger.debug(
          "delete data with statement '{}' and {} args, changed {} rows",
          statement,
          args.length,
          rowC);
      if (args.length > 0) {
        // need to do the (Object) cast to force args to be a single object
        logger.trace("delete arguments: {}", (Object) args);
      }
      if (rowC > 0 && objectCache != null) {
        Object id = idField.extractJavaFieldToSqlArgValue(data);
        objectCache.remove(clazz, id);
      }
      return rowC;
    } catch (SQLException e) {
      throw SqlExceptionUtil.create(
          "Unable to run delete stmt on object " + data + ": " + statement, e);
    }
  }

  /** Delete the object from the database. */
  public int deleteById(DatabaseConnection databaseConnection, ID id, ObjectCache objectCache)
      throws SQLException {
    try {
      Object[] args = new Object[] {convertIdToFieldObject(id)};
      int rowC = databaseConnection.delete(statement, args, argFieldTypes);
      logger.debug(
          "delete data with statement '{}' and {} args, changed {} rows",
          statement,
          args.length,
          rowC);
      if (args.length > 0) {
        // need to do the (Object) cast to force args to be a single object
        logger.trace("delete arguments: {}", (Object) args);
      }
      if (rowC > 0 && objectCache != null) {
        objectCache.remove(clazz, id);
      }
      return rowC;
    } catch (SQLException e) {
      throw SqlExceptionUtil.create(
          "Unable to run deleteById stmt on id " + id + ": " + statement, e);
    }
  }
}
