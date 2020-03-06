package xyz.vopen.mixmicro.components.enhance.cache.anno.support;

import xyz.vopen.mixmicro.components.enhance.cache.Cache;
import xyz.vopen.mixmicro.components.enhance.cache.anno.CacheConsts;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public interface CacheManager {
  static CacheManager defaultManager() {
    return SimpleCacheManager.defaultManager;
  }

  Cache getCache(String area, String cacheName);

  default Cache getCache(String cacheName) {
    return getCache(CacheConsts.DEFAULT_AREA, cacheName);
  }
}
