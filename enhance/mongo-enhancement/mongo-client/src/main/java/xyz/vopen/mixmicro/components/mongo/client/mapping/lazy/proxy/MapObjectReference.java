package xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.proxy;

import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;
import xyz.vopen.mixmicro.components.mongo.client.Key;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class MapObjectReference extends AbstractReference implements ProxiedEntityReferenceMap {

  private static final long serialVersionUID = 1L;
  private final HashMap<Object, Key<?>> keyMap;

  /**
   * Creates a MapObjectReference
   *
   * @param mongoRepository the MongoRepository to use when fetching this reference
   * @param mapToProxy the map to proxy
   * @param referenceObjClass the referenced objects' Class
   * @param ignoreMissing ignore missing referenced documents
   */
  public MapObjectReference(
      final MongoRepository mongoRepository,
      final Map mapToProxy,
      final Class referenceObjClass,
      final boolean ignoreMissing) {

    super(mongoRepository, referenceObjClass, ignoreMissing);
    object = mapToProxy;
    keyMap = new LinkedHashMap<Object, Key<?>>();
  }

  // CHECKSTYLE:OFF
  @Override
  public Map<Object, Key<?>> __getReferenceMap() {
    return keyMap;
  }
  // CHECKSTYLE:ON

  // CHECKSTYLE:OFF
  @Override
  public void __put(final Object key, final Key k) {
    keyMap.put(key, k);
  }

  @Override
  protected void beforeWriteObject() {
    if (__isFetched()) {
      syncKeys();
      ((Map) object).clear();
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  protected Object fetch() {
    final Map m = (Map) object;
    m.clear();
    // TODO us: change to getting them all at once and yell according to
    // ignoreMissing in order to a) increase performance and b) resolve
    // equals keys to the same instance
    // that should really be done in mongo repository.
    for (final Map.Entry<?, Key<?>> e : keyMap.entrySet()) {
      final Key<?> entityKey = e.getValue();
      m.put(e.getKey(), fetch(entityKey));
    }
    return m;
  }

  @SuppressWarnings("unchecked")
  private void syncKeys() {
    final MongoRepository ds = getMongoRepository();

    keyMap.clear();
    final Map<Object, Object> map = (Map) object;
    for (final Map.Entry<Object, Object> e : map.entrySet()) {
      keyMap.put(e.getKey(), ds.getKey(e.getValue()));
    }
  }
}
