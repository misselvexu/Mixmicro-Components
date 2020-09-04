package xyz.vopen.mixmicro.components.mongo.client.query;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;

/** A default implementation of {@link QueryFactory}. */
public class DefaultQueryFactory extends AbstractQueryFactory {

  @Override
  public <T> Query<T> createQuery(
      final MongoRepository mongoRepository,
      final DBCollection collection,
      final Class<T> type,
      final DBObject query) {

    final QueryImpl<T> item = new QueryImpl<T>(type, collection, mongoRepository);

    if (query != null) {
      item.setQueryObject(query);
    }

    return item;
  }
}
