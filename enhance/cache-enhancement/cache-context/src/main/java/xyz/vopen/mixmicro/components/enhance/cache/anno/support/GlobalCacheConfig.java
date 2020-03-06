package xyz.vopen.mixmicro.components.enhance.cache.anno.support;

import xyz.vopen.mixmicro.components.enhance.cache.CacheBuilder;

import java.util.Map;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class GlobalCacheConfig {

  protected int statIntervalMinutes;
  private String[] hiddenPackages;
  private boolean areaInCacheName = true;
  private boolean penetrationProtect = false;
  private boolean enableMethodCache = true;

  private Map<String, CacheBuilder> localCacheBuilders;
  private Map<String, CacheBuilder> remoteCacheBuilders;

  public GlobalCacheConfig() {}

  public String[] getHiddenPackages() {
    return hiddenPackages;
  }

  public void setHiddenPackages(String[] hiddenPackages) {
    this.hiddenPackages = hiddenPackages;
  }

  public Map<String, CacheBuilder> getLocalCacheBuilders() {
    return localCacheBuilders;
  }

  public void setLocalCacheBuilders(Map<String, CacheBuilder> localCacheBuilders) {
    this.localCacheBuilders = localCacheBuilders;
  }

  public Map<String, CacheBuilder> getRemoteCacheBuilders() {
    return remoteCacheBuilders;
  }

  public void setRemoteCacheBuilders(Map<String, CacheBuilder> remoteCacheBuilders) {
    this.remoteCacheBuilders = remoteCacheBuilders;
  }

  public int getStatIntervalMinutes() {
    return statIntervalMinutes;
  }

  public void setStatIntervalMinutes(int statIntervalMinutes) {
    this.statIntervalMinutes = statIntervalMinutes;
  }

  public boolean isAreaInCacheName() {
    return areaInCacheName;
  }

  public void setAreaInCacheName(boolean areaInCacheName) {
    this.areaInCacheName = areaInCacheName;
  }

  public boolean isPenetrationProtect() {
    return penetrationProtect;
  }

  public void setPenetrationProtect(boolean penetrationProtect) {
    this.penetrationProtect = penetrationProtect;
  }

  public boolean isEnableMethodCache() {
    return enableMethodCache;
  }

  public void setEnableMethodCache(boolean enableMethodCache) {
    this.enableMethodCache = enableMethodCache;
  }
}
