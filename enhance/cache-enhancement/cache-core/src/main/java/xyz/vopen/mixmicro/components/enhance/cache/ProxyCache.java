package xyz.vopen.mixmicro.components.enhance.cache;

/**
 * Created on 2018/12/13.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public interface ProxyCache<K, V> extends Cache<K, V> {
  Cache<K, V> getTargetCache();

  @Override
  default <T> T unwrap(Class<T> clazz) {
    return getTargetCache().unwrap(clazz);
  }
}
