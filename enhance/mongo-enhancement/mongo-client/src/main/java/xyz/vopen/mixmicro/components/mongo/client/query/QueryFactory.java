package xyz.vopen.mixmicro.components.mongo.client.query;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;

/** A factory for {@link Query}ies. */
public interface QueryFactory {

  /**
   * Creates and returns a {@link Query} for the given arguments. Default implementations of this
   * method will simply delegate to {@link #createQuery(MongoRepository, DBCollection, Class, DBObject)}
   * with the last argument being {@code null}.
   *
   * @param mongoRepository the MongoRepository to use
   * @param collection the collection to query
   * @param type the type of the result
   * @param <T> the type of the result
   * @return the query
   * @see #createQuery(MongoRepository, DBCollection, Class, DBObject)
   */
  <T> Query<T> createQuery(MongoRepository mongoRepository, DBCollection collection, Class<T> type);

  /**
   * Creates and returns a {@link Query} for the given arguments. The last argument is optional and
   * may be {@code null}.
   *
   * @param mongoRepository the MongoRepository to use
   * @param collection the collection to query
   * @param type the type of the result
   * @param query the DBObject containing the query structure
   * @param <T> the type of the result
   * @return the query
   */
  <T> Query<T> createQuery(
      MongoRepository mongoRepository, DBCollection collection, Class<T> type, DBObject query);

  /**
   * Creates an unvalidated {@link Query} typically for use in aggregation pipelines.
   *
   * @param mongoRepository the MongoRepository to use
   * @param <T> the type of the result
   * @return the query
   */
  <T> Query<T> createQuery(MongoRepository mongoRepository);
}
