package xyz.vopen.mixmicro.components.mongo.client;

import com.mongodb.DBObject;
import xyz.vopen.mixmicro.components.mongo.client.annotations.PostLoad;
import xyz.vopen.mixmicro.components.mongo.client.annotations.PostPersist;
import xyz.vopen.mixmicro.components.mongo.client.annotations.PreLoad;
import xyz.vopen.mixmicro.components.mongo.client.annotations.PreSave;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;

/** Interface for intercepting @Entity lifecycle events */
public interface EntityInterceptor {
  /**
   * @param ent the entity being processed
   * @param dbObj the DBObject form of the entity
   * @param mapper the Mapper being used
   * @see PostLoad
   */
  void postLoad(Object ent, DBObject dbObj, Mapper mapper);

  /**
   * @param ent the entity being processed
   * @param dbObj the DBObject form of the entity
   * @param mapper the Mapper being used
   * @see PostPersist
   */
  void postPersist(Object ent, DBObject dbObj, Mapper mapper);

  /**
   * @param ent the entity being processed
   * @param dbObj the DBObject form of the entity
   * @param mapper the Mapper being used
   * @see PreLoad
   */
  void preLoad(Object ent, DBObject dbObj, Mapper mapper);

  /**
   * @param ent the entity being processed
   * @param dbObj the DBObject form of the entity
   * @param mapper the Mapper being used
   * @see PostPersist
   */
  void prePersist(Object ent, DBObject dbObj, Mapper mapper);

  /**
   * @param ent the entity being processed
   * @param dbObj the DBObject form of the entity
   * @param mapper the Mapper being used
   * @see PreSave
   * @deprecated removed in 2.0
   */
  void preSave(Object ent, DBObject dbObj, Mapper mapper);
}
