package xyz.vopen.mixmicro.components.mongo.client.mapping.experimental;

import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;

import java.util.Collection;
import java.util.List;

/**
 * @param <T>
 *
 */
class ListReference<T> extends CollectionReference<List<T>> {
  private List<T> values;

  /**  */
  ListReference(final MongoRepository mongoRepository, final MappedClass mappedClass, final List ids) {
    super(mongoRepository, mappedClass, ids);
  }

  ListReference(final List<T> values) {
    this.values = values;
  }

  @Override
  Collection<?> getValues() {
    return values;
  }

  @Override
  public List<T> get() {
    if (values == null && getIds() != null) {
      values = (List<T>) find();
    }
    return values;
  }
}
