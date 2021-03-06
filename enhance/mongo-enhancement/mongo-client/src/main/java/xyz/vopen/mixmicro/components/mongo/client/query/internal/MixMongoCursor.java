package xyz.vopen.mixmicro.components.mongo.client.query.internal;

import com.mongodb.Cursor;
import com.mongodb.DBObject;
import com.mongodb.ServerAddress;
import com.mongodb.ServerCursor;
import com.mongodb.client.MongoCursor;
import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.cache.EntityCache;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/** @param <T> the original type being iterated */
public class MixMongoCursor<T> implements MongoCursor<T> {
  private final Cursor wrapped;
  private final Mapper mapper;
  private final Class<T> clazz;
  private final EntityCache cache;
  private final MongoRepository mongoRepository;

  /**
   * Creates a MixMongoCursor
   *
   * @param mongoRepository the MongoRepository to use when fetching this reference
   * @param cursor the Iterator to use
   * @param mapper the Mapper to use
   * @param clazz the original type being iterated
   * @param cache the EntityCache
   */
  public MixMongoCursor(
      final MongoRepository mongoRepository,
      final Cursor cursor,
      final Mapper mapper,
      final Class<T> clazz,
      final EntityCache cache) {
    wrapped = cursor;
    if (wrapped == null) {
      throw new IllegalArgumentException("The wrapped cursor can not be null");
    }
    this.mapper = mapper;
    this.clazz = clazz;
    this.cache = cache;
    this.mongoRepository = mongoRepository;
  }

  /**
   * Converts this cursor to a List. Care should be taken on large datasets as OutOfMemoryErrors are
   * a risk.
   *
   * @return the list of Entities
   */
  public List<T> toList() {
    final List<T> results = new ArrayList<T>();
    try {
      while (wrapped.hasNext()) {
        results.add(next());
      }
    } finally {
      wrapped.close();
    }
    return results;
  }

  /** Closes the underlying cursor. */
  public void close() {
    if (wrapped != null) {
      wrapped.close();
    }
  }

  @Override
  public boolean hasNext() {
    if (wrapped == null) {
      return false;
    }
    return wrapped.hasNext();
  }

  @Override
  public T next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return mapper.fromDBObject(mongoRepository, clazz, wrapped.next(), cache);
  }

  @Override
  public T tryNext() {
    if (hasNext()) {
      return next();
    } else {
      return null;
    }
  }

  @Override
  public ServerCursor getServerCursor() {
    return new ServerCursor(wrapped.getCursorId(), wrapped.getServerAddress());
  }

  @Override
  public ServerAddress getServerAddress() {
    return wrapped.getServerAddress();
  }

  @Override
  public void remove() {
    wrapped.remove();
  }

  protected DBObject getNext() {
    return wrapped.next();
  }
}
