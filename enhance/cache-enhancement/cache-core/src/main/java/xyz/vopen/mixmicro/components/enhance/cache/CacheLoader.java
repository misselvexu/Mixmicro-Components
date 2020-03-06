package xyz.vopen.mixmicro.components.enhance.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
@FunctionalInterface
public interface CacheLoader<K, V> extends Function<K, V> {
  V load(K key) throws Throwable;

  default Map<K, V> loadAll(Set<K> keys) throws Throwable {
    Map<K, V> map = new HashMap<>();
    for (K k : keys) {
      V value = load(k);
      if (value != null) {
        map.put(k, value);
      }
    }
    return map;
  }

  @Override
  default V apply(K key) {
    try {
      return load(key);
    } catch (Throwable e) {
      throw new CacheInvokeException(e.getMessage(), e);
    }
  }

  default boolean vetoCacheUpdate() {
    return false;
  }
}
