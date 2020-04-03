package xyz.vopen.mixmicro.components.enhance.dbm.stmt.mapped;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;
import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.logger.Logger;
import xyz.vopen.mixmicro.components.enhance.dbm.logger.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.dbm.support.ConnectionSource;
import xyz.vopen.mixmicro.components.enhance.dbm.table.TableInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * Abstract mapped statement which has common statements used by the subclasses.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public abstract class BaseMappedStatement<T, ID> {

  protected static Logger logger = LoggerFactory.getLogger(BaseMappedStatement.class);

  protected final Repository<T, ID> repository;
  protected final ConnectionSource connectionSource;
  protected final TableInfo<T, ID> tableInfo;
  protected final Class<T> clazz;
  protected final FieldType idField;
  protected final String statement;
  protected final FieldType[] argFieldTypes;

  protected BaseMappedStatement(
      Repository<T, ID> repository, TableInfo<T, ID> tableInfo, String statement, FieldType[] argFieldTypes) {
    this.repository = repository;
    this.connectionSource = repository.getConnectionSource();
    this.tableInfo = tableInfo;
    this.clazz = tableInfo.getDataClass();
    this.idField = tableInfo.getIdField();
    this.statement = statement;
    this.argFieldTypes = argFieldTypes;
  }

  static void appendWhereFieldEq(
      DatabaseType databaseType,
      FieldType fieldType,
      StringBuilder sb,
      List<FieldType> fieldTypeList) {
    sb.append("WHERE ");
    appendFieldColumnName(databaseType, sb, fieldType, fieldTypeList);
    sb.append("= ?");
  }

  static void appendTableName(
      DatabaseType databaseType, StringBuilder sb, String prefix, String tableName) {
    if (prefix != null) {
      sb.append(prefix);
    }
    databaseType.appendEscapedEntityName(sb, tableName);
    sb.append(' ');
  }

  static void appendFieldColumnName(
      DatabaseType databaseType,
      StringBuilder sb,
      FieldType fieldType,
      List<FieldType> fieldTypeList) {
    databaseType.appendEscapedEntityName(sb, fieldType.getColumnName());
    if (fieldTypeList != null) {
      fieldTypeList.add(fieldType);
    }
    sb.append(' ');
  }

  /** Return the array of field objects pulled from the data object. */
  protected Object[] getFieldObjects(Object data) throws SQLException {
    Object[] objects = new Object[argFieldTypes.length];
    for (int i = 0; i < argFieldTypes.length; i++) {
      FieldType fieldType = argFieldTypes[i];
      if (fieldType.isAllowGeneratedIdInsert()) {
        objects[i] = fieldType.getFieldValueIfNotDefault(data);
      } else {
        objects[i] = fieldType.extractJavaFieldToSqlArgValue(data);
      }
      if (objects[i] == null) {
        // NOTE: the default value could be null as well
        objects[i] = fieldType.getDefaultValue();
      }
    }
    return objects;
  }

  /** Return a field object converted from an id. */
  protected Object convertIdToFieldObject(ID id) throws SQLException {
    return idField.convertJavaFieldToSqlArgValue(id);
  }

  @Override
  public String toString() {
    return "MappedStatement: " + statement;
  }
}
