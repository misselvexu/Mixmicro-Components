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
 * Mapped statement for updating an object's ID field.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class MappedUpdateId<T, ID> extends BaseMappedStatement<T, ID> {

  private MappedUpdateId(
      Repository<T, ID> repository, TableInfo<T, ID> tableInfo, String statement, FieldType[] argFieldTypes) {
    super(repository, tableInfo, statement, argFieldTypes);
  }

  public static <T, ID> MappedUpdateId<T, ID> build(Repository<T, ID> repository, TableInfo<T, ID> tableInfo)
      throws SQLException {
    FieldType idField = tableInfo.getIdField();
    if (idField == null) {
      throw new SQLException(
          "Cannot update-id in "
              + tableInfo.getDataClass()
              + " because it doesn't have an id field");
    }
    StringBuilder sb = new StringBuilder(64);
    DatabaseType databaseType = repository.getConnectionSource().getDatabaseType();
    appendTableName(databaseType, sb, "UPDATE ", tableInfo.getTableName());
    sb.append("SET ");
    appendFieldColumnName(databaseType, sb, idField, null);
    sb.append("= ? ");
    appendWhereFieldEq(databaseType, idField, sb, null);
    return new MappedUpdateId<T, ID>(
        repository, tableInfo, sb.toString(), new FieldType[] {idField, idField});
  }

  /** Update the id field of the object in the database. */
  public int execute(
      DatabaseConnection databaseConnection, T data, ID newId, ObjectCache objectCache)
      throws SQLException {
    try {
      // the arguments are the new-id and old-id
      Object[] args = new Object[] {convertIdToFieldObject(newId), extractIdToFieldObject(data)};
      int rowC = databaseConnection.update(statement, args, argFieldTypes);
      if (rowC > 0) {
        if (objectCache != null) {
          Object oldId = idField.extractJavaFieldValue(data);
          T obj = objectCache.updateId(clazz, oldId, newId);
          if (obj != null && obj != data) {
            // if our cached value is not the data that will be updated then we need to update it
            // specially
            idField.assignField(connectionSource, obj, newId, false, objectCache);
          }
        }
        // adjust the object to assign the new id
        idField.assignField(connectionSource, data, newId, false, objectCache);
      }
      logger.debug(
          "updating-id with statement '{}' and {} args, changed {} rows",
          statement,
          args.length,
          rowC);
      if (args.length > 0) {
        // need to do the cast otherwise we only print the first object in args
        logger.trace("updating-id arguments: {}", (Object) args);
      }
      return rowC;
    } catch (SQLException e) {
      throw SqlExceptionUtil.create(
          "Unable to run update-id stmt on object " + data + ": " + statement, e);
    }
  }

  /** Return a field-object for the id extracted from the data. */
  private Object extractIdToFieldObject(T data) throws SQLException {
    return idField.extractJavaFieldToSqlArgValue(data);
  }
}
