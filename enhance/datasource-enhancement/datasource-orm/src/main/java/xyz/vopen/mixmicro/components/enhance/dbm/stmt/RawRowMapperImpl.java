package xyz.vopen.mixmicro.components.enhance.dbm.stmt;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;
import xyz.vopen.mixmicro.components.enhance.dbm.repository.RawRowMapper;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.table.TableInfo;

import java.sql.SQLException;

/**
 * Default row mapper when you are using the {@link Repository#queryRaw(String, RawRowMapper, String...)}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class RawRowMapperImpl<T, ID> implements RawRowMapper<T> {

  private final Repository<T, ID> repository;
  private final TableInfo<T, ID> tableInfo;

  public RawRowMapperImpl(Repository<T, ID> repository) {
    this.repository = repository;
    this.tableInfo = repository.getTableInfo();
  }

  @Override
  public T mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
    // create our object
    T rowObj = repository.createObjectInstance();
    for (int i = 0; i < columnNames.length; i++) {
      // sanity check, prolly will never happen but let's be careful out there
      if (i >= resultColumns.length) {
        continue;
      }
      // run through and convert each field
      FieldType fieldType = tableInfo.getFieldTypeByColumnName(columnNames[i]);
      Object fieldObj = fieldType.convertStringToJavaField(resultColumns[i], i);
      // assign it to the row object
      fieldType.assignField(repository.getConnectionSource(), rowObj, fieldObj, false, null);
    }
    return rowObj;
  }
}
