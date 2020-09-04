package xyz.vopen.mixmicro.components.mongo.client.mapping.lazy;

import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;
import xyz.vopen.mixmicro.components.mongo.client.Key;

import java.util.Collection;
import java.util.Map;

/** @author uwe schaefer */
public interface LazyProxyFactory {
  /**
   * Creates a proxy for a List.
   *
   * @param <T> the type of the entities
   * @param mongoRepository the MongoRepository to use when fetching this reference
   * @param listToProxy the list to proxy
   * @param referenceObjClass the type of the referenced objects
   * @param ignoreMissing ignore references that don't exist in the database
   * @return the proxy
   */
  <T extends Collection> T createListProxy(
      MongoRepository mongoRepository, T listToProxy, Class referenceObjClass, boolean ignoreMissing);

  /**
   * Creates a proxy for a Map.
   *
   * @param <T> the type of the entities
   * @param mongoRepository the MongoRepository to use when fetching this reference
   * @param mapToProxy the map to proxy
   * @param referenceObjClass the type of the referenced objects
   * @param ignoreMissing ignore references that don't exist in the database
   * @return the proxy
   */
  <T extends Map> T createMapProxy(
      MongoRepository mongoRepository, T mapToProxy, Class referenceObjClass, boolean ignoreMissing);

  /**
   * Creates a proxy for a Class.
   *
   * @param <T> the type of the entity
   * @param mongoRepository the MongoRepository to use when fetching this reference
   * @param targetClass the referenced object's Class
   * @param key the Key of the reference
   * @param ignoreMissing ignore references that don't exist in the database
   * @return the proxy
   */
  <T> T createProxy(MongoRepository mongoRepository, Class<T> targetClass, Key<T> key, boolean ignoreMissing);
}
