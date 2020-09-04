package xyz.vopen.mixmicro.components.mongo.client.query;

import com.mongodb.DBCollection;
import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;

/** An abstract implementation of {@link QueryFactory}. */
public abstract class AbstractQueryFactory implements QueryFactory {

  @Override
  public <T> Query<T> createQuery(
      final MongoRepository mongoRepository, final DBCollection collection, final Class<T> type) {
    return createQuery(mongoRepository, collection, type, null);
  }

  @Override
  public <T> Query<T> createQuery(final MongoRepository mongoRepository) {
    return new QueryImpl<T>(null, null, mongoRepository);
  }
}
