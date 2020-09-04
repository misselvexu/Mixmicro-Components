package xyz.vopen.mixmicro.components.mongo.client.mapping.experimental;

import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MappedClass;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @param <T>
 *
 */
@SuppressWarnings("unchecked")
class SetReference<T> extends CollectionReference<Set<T>> {
  private Set<T> values;

  /**  */
  SetReference(final MongoRepository mongoRepository, final MappedClass mappedClass, final List ids) {
    super(mongoRepository, mappedClass, ids);
  }

  SetReference(final Set<T> values) {
    this.values = values;
  }

  @Override
  Set<T> getValues() {
    return values;
  }

  public Set<T> get() {
    if (values == null && getIds() != null) {
      values = new LinkedHashSet(find());
    }
    return values;
  }
}
