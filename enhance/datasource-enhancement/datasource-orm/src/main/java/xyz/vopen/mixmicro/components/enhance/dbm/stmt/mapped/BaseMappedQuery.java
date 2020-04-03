package xyz.vopen.mixmicro.components.enhance.dbm.stmt.mapped;

import xyz.vopen.mixmicro.components.enhance.dbm.repository.BaseForeignCollection;
import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;
import xyz.vopen.mixmicro.components.enhance.dbm.repository.ObjectCache;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.GenericRowMapper;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;
import xyz.vopen.mixmicro.components.enhance.dbm.table.TableInfo;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract mapped statement for queries which handle the creating of a new object and the row
 * mapping functionality.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public abstract class BaseMappedQuery<T, ID> extends BaseMappedStatement<T, ID>
    implements GenericRowMapper<T> {

  protected final FieldType[] resultsFieldTypes;
  // cache of column names to results position
  private Map<String, Integer> columnPositions = null;
  private Object parent = null;
  private Object parentId = null;

  protected BaseMappedQuery(
      Repository<T, ID> repository,
      TableInfo<T, ID> tableInfo,
      String statement,
      FieldType[] argFieldTypes,
      FieldType[] resultsFieldTypes) {
    super(repository, tableInfo, statement, argFieldTypes);
    this.resultsFieldTypes = resultsFieldTypes;
  }

  @Override
  public T mapRow(DatabaseResults results) throws SQLException {
    Map<String, Integer> colPosMap;
    if (columnPositions == null) {
      colPosMap = new HashMap<String, Integer>();
    } else {
      colPosMap = columnPositions;
    }

    ObjectCache objectCache = results.getObjectCacheForRetrieve();
    if (objectCache != null) {
      Object id = idField.resultToJava(results, colPosMap);
      T cachedInstance = objectCache.get(clazz, id);
      if (cachedInstance != null) {
        // if we have a cached instance for this id then return it
        return cachedInstance;
      }
    }

    // create our instance
    T instance = repository.createObjectInstance();
    // populate its fields
    Object id = null;
    boolean foreignCollections = false;
    for (FieldType fieldType : resultsFieldTypes) {
      if (fieldType.isForeignCollection()) {
        foreignCollections = true;
      } else {
        Object val = fieldType.resultToJava(results, colPosMap);
        /*
         * This is pretty subtle. We introduced multiple foreign fields to the same type which use the {@link
         * ForeignCollectionField} foreignColumnName field. The bug that was created was that all the fields
         * were then set with the parent class. Only the fields that have a matching id value should be set to
         * the parent. We had to add the val.equals logic.
         */
        if (val != null
            && parent != null
            && fieldType.getField().getType() == parent.getClass()
            && val.equals(parentId)) {
          fieldType.assignField(connectionSource, instance, parent, true, objectCache);
        } else {
          fieldType.assignField(connectionSource, instance, val, false, objectCache);
        }
        if (fieldType.isId()) {
          id = val;
        }
      }
    }
    if (foreignCollections) {
      // go back and initialize any foreign collections
      for (FieldType fieldType : resultsFieldTypes) {
        if (fieldType.isForeignCollection()) {
          BaseForeignCollection<?, ?> collection = fieldType.buildForeignCollection(instance, id);
          if (collection != null) {
            fieldType.assignField(connectionSource, instance, collection, false, objectCache);
          }
        }
      }
    }
    // if we have a cache and we have an id then add it to the cache
    objectCache = results.getObjectCacheForStore();
    if (objectCache != null && id != null) {
      objectCache.put(clazz, id, instance);
    }
    if (columnPositions == null) {
      columnPositions = colPosMap;
    }
    return instance;
  }

  /**
   * If we have a foreign collection object then this sets the value on the foreign object in the
   * class.
   */
  public void setParentInformation(Object parent, Object parentId) {
    this.parent = parent;
    this.parentId = parentId;
  }
}
