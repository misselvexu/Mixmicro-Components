package xyz.vopen.mixmicro.components.enhance.cache.support;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public interface CacheMessagePublisher {
  void publish(String area, String cacheName, CacheMessage cacheMessage);
}
