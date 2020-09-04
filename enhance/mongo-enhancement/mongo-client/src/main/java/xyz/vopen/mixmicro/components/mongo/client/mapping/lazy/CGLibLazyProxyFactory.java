package xyz.vopen.mixmicro.components.mongo.client.mapping.lazy;

import com.thoughtworks.proxy.factory.CglibProxyFactory;
import com.thoughtworks.proxy.toys.delegate.DelegationMode;
import com.thoughtworks.proxy.toys.dispatch.Dispatching;
import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;
import xyz.vopen.mixmicro.components.mongo.client.Key;
import xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.proxy.ProxiedEntityReference;
import xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.proxy.ProxiedEntityReferenceList;
import xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.proxy.ProxiedEntityReferenceMap;
import xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.proxy.CollectionObjectReference;
import xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.proxy.EntityObjectReference;
import xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.proxy.MapObjectReference;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * i have to admit, there are plenty of open questions for me on that Key-business...
 *
 * @author uwe schaefer
 */
@SuppressWarnings("unchecked")
public class CGLibLazyProxyFactory implements LazyProxyFactory {
  private final CglibProxyFactory factory = new CglibProxyFactory();

  @Override
  public <T extends Collection> T createListProxy(
      final MongoRepository mongoRepository,
      final T listToProxy,
      final Class referenceObjClass,
      final boolean ignoreMissing) {
    final Class<? extends Collection> targetClass = listToProxy.getClass();
    final CollectionObjectReference objectReference =
        new CollectionObjectReference(listToProxy, referenceObjClass, ignoreMissing, mongoRepository);

    final T backend =
        (T)
            new NonFinalizingHotSwappingInvoker(
                    new Class[] {targetClass, Serializable.class},
                    factory,
                    objectReference,
                    DelegationMode.SIGNATURE)
                .proxy();

    return (T)
        Dispatching.proxy(
                targetClass,
                new Class[] {ProxiedEntityReferenceList.class, targetClass, Serializable.class})
            .with(objectReference, backend)
            .build(factory);
  }

  @Override
  public <T extends Map> T createMapProxy(
      final MongoRepository mongoRepository,
      final T mapToProxy,
      final Class referenceObjClass,
      final boolean ignoreMissing) {
    final Class<? extends Map> targetClass = mapToProxy.getClass();
    final MapObjectReference objectReference =
        new MapObjectReference(mongoRepository, mapToProxy, referenceObjClass, ignoreMissing);

    final T backend =
        (T)
            new NonFinalizingHotSwappingInvoker(
                    new Class[] {targetClass, Serializable.class},
                    factory,
                    objectReference,
                    DelegationMode.SIGNATURE)
                .proxy();

    return (T)
        Dispatching.proxy(
                targetClass,
                new Class[] {ProxiedEntityReferenceMap.class, targetClass, Serializable.class})
            .with(objectReference, backend)
            .build(factory);
  }

  @Override
  public <T> T createProxy(
      final MongoRepository mongoRepository,
      final Class<T> targetClass,
      final Key<T> key,
      final boolean ignoreMissing) {

    final EntityObjectReference objectReference =
        new EntityObjectReference(mongoRepository, targetClass, key, ignoreMissing);

    final T backend =
        (T)
            new NonFinalizingHotSwappingInvoker(
                    new Class[] {targetClass, Serializable.class},
                    factory,
                    objectReference,
                    DelegationMode.SIGNATURE)
                .proxy();

    return (T)
        Dispatching.proxy(
                targetClass, new Class[] {ProxiedEntityReference.class, Serializable.class})
            .with(objectReference, backend)
            .build(factory);
  }
}
