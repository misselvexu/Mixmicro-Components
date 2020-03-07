package xyz.vopen.mixmicro.components.enhance.cache.annotation.support;

import xyz.vopen.mixmicro.components.enhance.cache.annotation.CacheConsts;
import xyz.vopen.mixmicro.components.enhance.cache.annotation.method.CacheInvokeConfig;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class ConfigMap {
  private ConcurrentHashMap<String, CacheInvokeConfig> methodInfoMap = new ConcurrentHashMap<>();
  private ConcurrentHashMap<String, CacheInvokeConfig> cacheNameMap = new ConcurrentHashMap<>();

  public void putByMethodInfo(String key, CacheInvokeConfig config) {
    methodInfoMap.put(key, config);
    CachedAnnoConfig cac = config.getCachedAnnoConfig();
    if (cac != null && !CacheConsts.isUndefined(cac.getName())) {
      cacheNameMap.put(cac.getArea() + "_" + cac.getName(), config);
    }
  }

  public CacheInvokeConfig getByMethodInfo(String key) {
    return methodInfoMap.get(key);
  }

  public CacheInvokeConfig getByCacheName(String area, String cacheName) {
    return cacheNameMap.get(area + "_" + cacheName);
  }
}
