package xyz.vopen.mixmicro.components.mongo.client;

import com.mongodb.DBObject;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class AbstractEntityInterceptor implements EntityInterceptor {

  @Override
  public void postLoad(final Object ent, final DBObject dbObj, final Mapper mapper) {}

  @Override
  public void postPersist(final Object ent, final DBObject dbObj, final Mapper mapper) {}

  @Override
  public void preLoad(final Object ent, final DBObject dbObj, final Mapper mapper) {}

  @Override
  public void prePersist(final Object ent, final DBObject dbObj, final Mapper mapper) {}

  @Override
  public void preSave(final Object ent, final DBObject dbObj, final Mapper mapper) {}
}
