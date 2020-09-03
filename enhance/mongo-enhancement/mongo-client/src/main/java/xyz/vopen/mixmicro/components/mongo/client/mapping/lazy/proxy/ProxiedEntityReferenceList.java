package xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.proxy;

import xyz.vopen.mixmicro.components.mongo.client.Key;

import java.util.Collection;
import java.util.List;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public interface ProxiedEntityReferenceList extends ProxiedReference {
  // CHECKSTYLE:OFF
  void __add(Key<?> key);

  void __addAll(Collection<? extends Key<?>> keys);

  List<Key<?>> __getKeysAsList();
  // CHECKSTYLE:ON
}
