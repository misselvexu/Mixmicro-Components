package xyz.vopen.mixmicro.components.mongo.client.mapping;

import com.mongodb.DBObject;
import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;
import xyz.vopen.mixmicro.components.mongo.client.mapping.cache.EntityCache;

import java.util.Map;

/**
 *
 * @deprecated
 */
class ValueMapper implements CustomMapper {
  @Override
  public void fromDBObject(
      final MongoRepository mongoRepository,
      final DBObject dbObject,
      final MappedField mf,
      final Object entity,
      final EntityCache cache,
      final Mapper mapper) {
    mapper.getConverters().fromDBObject(dbObject, mf, entity);
  }

  @Override
  public void toDBObject(
      final Object entity,
      final MappedField mf,
      final DBObject dbObject,
      final Map<Object, DBObject> involvedObjects,
      final Mapper mapper) {
    try {
      mapper.getConverters().toDBObject(entity, mf, dbObject, mapper.getOptions());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
