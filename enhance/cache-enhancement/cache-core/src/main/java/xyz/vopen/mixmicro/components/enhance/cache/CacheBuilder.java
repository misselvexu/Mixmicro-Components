package xyz.vopen.mixmicro.components.enhance.cache;

/**
 * Created on 2018/11/17.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public interface CacheBuilder {
  <K, V> Cache<K, V> buildCache();
}
