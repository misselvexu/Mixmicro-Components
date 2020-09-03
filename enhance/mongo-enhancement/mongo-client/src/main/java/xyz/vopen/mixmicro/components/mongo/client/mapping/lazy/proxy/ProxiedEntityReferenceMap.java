package xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.proxy;

import xyz.vopen.mixmicro.components.mongo.client.Key;

import java.util.Map;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public interface ProxiedEntityReferenceMap extends ProxiedReference {
  /** @return the reference map */
  // CHECKSTYLE:OFF
  Map<Object, Key<?>> __getReferenceMap();
  // CHECKSTYLE:ON

  // CHECKSTYLE:OFF
  void __put(Object key, Key<?> referenceKey);
  // CHECKSTYLE:ON
}
