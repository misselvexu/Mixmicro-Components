package xyz.vopen.mixmicro.components.mongo.client.mapping.experimental;

import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.mongodb.client.MongoCursor;
import xyz.vopen.mixmicro.components.mongo.client.AdvancedMongoRepository;
import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @param <T>
 *
 */
public class MapReference<T> extends MixMongoReference<Map<String, T>> {
  private Map<String, Object> ids;
  private Map<String, T> values;
  private Map<String, List<Object>> collections = new HashMap<String, List<Object>>();

  /**  */
  MapReference(
      final MongoRepository mongoRepository, final MappedClass mappedClass, final Map<String, Object> ids) {
    super(mongoRepository, mappedClass);
    Map<String, Object> unwrapped = ids;
    if (ids != null) {
      for (final Entry<String, Object> entry : ids.entrySet()) {
        CollectionReference.collate(mongoRepository, collections, entry.getValue());
      }
    }

    this.ids = unwrapped;
  }

  MapReference(final Map<String, T> values) {
    this.values = values;
  }

  /** {@inheritDoc} */
  public Map<String, T> get() {
    if (values == null && ids != null) {
      values = new LinkedHashMap<String, T>();
      mergeReads();
    }
    return values;
  }

  private void mergeReads() {
    for (final Entry<String, List<Object>> entry : collections.entrySet()) {
      readFromSingleCollection(entry.getKey(), entry.getValue());
    }
  }

  @SuppressWarnings("unchecked")
  private void readFromSingleCollection(final String collection, final List<Object> collectionIds) {

    final Class<?> collectionType = getMappedClass().getClazz();
    final MongoCursor<T> cursor =
        (MongoCursor<T>)
            ((AdvancedMongoRepository) getMongoRepository())
                .find(collection, collectionType)
                .filter("_id in ", collectionIds)
                .find();
    try {
      final Map<Object, T> idMap = new HashMap<Object, T>();
      while (cursor.hasNext()) {
        final T entity = cursor.next();
        idMap.put(getMongoRepository().getMapper().getId(entity), entity);
      }

      for (final Entry<String, Object> entry : ids.entrySet()) {
        final Object id = entry.getValue();
        final T value = idMap.get(id instanceof DBRef ? ((DBRef) id).getId() : id);
        if (value != null) {
          values.put(entry.getKey(), value);
        }
      }
    } finally {
      cursor.close();
    }
  }

  /** {@inheritDoc} */
  public boolean isResolved() {
    return values != null;
  }

  /** {@inheritDoc} */
  @Override
  public Object encode(final Mapper mapper, final Object value, final MappedField field) {
    if (isResolved()) {
      Map<String, Object> ids = new LinkedHashMap<String, Object>();
      for (final Entry<String, T> entry : get().entrySet()) {
        ids.put(entry.getKey(), wrapId(mapper, field, entry.getValue()));
      }
      return ids;
    } else {
      return null;
    }
  }

  /**
   * Decodes a document in to entities
   *
   * @param mongoRepository the mongo repository
   * @param mapper the mapper
   * @param mappedField the MappedField
   * @param dbObject the DBObject to decode
   * @return the entities
   */
  public static MapReference decode(
      final MongoRepository mongoRepository,
      final Mapper mapper,
      final MappedField mappedField,
      final DBObject dbObject) {
    final Class subType = mappedField.getTypeParameters().get(0).getSubClass();

    final Map<String, Object> ids = (Map<String, Object>) mappedField.getDbObjectValue(dbObject);
    MapReference reference = null;
    if (ids != null) {
      reference = new MapReference(mongoRepository, mapper.getMappedClass(subType), ids);
    }

    return reference;
  }
}
