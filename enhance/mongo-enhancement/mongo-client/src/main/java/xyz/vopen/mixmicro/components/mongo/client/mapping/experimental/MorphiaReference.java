package xyz.vopen.mixmicro.components.mongo.client.mapping.experimental;

import com.mongodb.DBRef;
import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedField;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper type for references to entities in other collections
 *
 * @param <T>
 * @since 1.5
 */
@SuppressWarnings("unchecked")
public abstract class MorphiaReference<T> {
  private MongoRepository mongoRepository;
  private MappedClass mappedClass;

  MorphiaReference() {}

  MorphiaReference(final MongoRepository mongoRepository, final MappedClass mappedClass) {
    this.mongoRepository = mongoRepository;
    this.mappedClass = mappedClass;
  }

  static Object wrapId(final Mapper mapper, final MappedField field, final Object entity) {
    Object id = mapper.getId(entity);
    Object encoded = mapper.toMongoObject(field, mapper.getMappedClass(entity), id);
    if (!entity.getClass().equals(field.getType())) {
      encoded = new DBRef(mapper.getCollectionName(entity), encoded);
    }

    return encoded;
  }

  /** @return returns the referenced entity if it exists. May return null. */
  public abstract T get();

  /**
   * @return true if this reference has already been resolved
   *
   */
  public abstract boolean isResolved();

  /**
   * @param mapper the mapper
   * @param value the value
   * @param optionalExtraInfo the MappedField
   * @return the encoded vale
   *
   */
  public abstract Object encode(Mapper mapper, Object value, MappedField optionalExtraInfo);

  /**
   * @return the mongo repository
   *
   */
  MongoRepository getMongoRepository() {
    return mongoRepository;
  }

  /**
   * @return the MappedClass of the referenced entity type
   *
   */
  MappedClass getMappedClass() {
    return mappedClass;
  }

  /**
   * Wraps an value in a MorphiaReference to storing on an entity
   *
   * @param value the value wrap
   * @param <V> the type of the value
   * @return the MorphiaReference wrapper
   */
  @SuppressWarnings("unchecked")
  public static <V> MorphiaReference<V> wrap(final V value) {
    if (value instanceof List) {
      return (MorphiaReference<V>) new ListReference<V>((List<V>) value);
    } else if (value instanceof Set) {
      return (MorphiaReference<V>) new SetReference<V>((Set<V>) value);
    } else if (value instanceof Map) {
      return (MorphiaReference<V>) new MapReference<V>((Map<String, V>) value);
    } else {
      return new SingleReference<V>(value);
    }
  }
}
