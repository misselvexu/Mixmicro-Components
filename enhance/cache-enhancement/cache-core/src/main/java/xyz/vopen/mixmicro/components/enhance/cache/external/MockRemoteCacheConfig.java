package xyz.vopen.mixmicro.components.enhance.cache.external;

import xyz.vopen.mixmicro.components.enhance.cache.annotation.CacheConsts;

public class MockRemoteCacheConfig<K, V> extends ExternalCacheConfig<K, V> {
  private int limit = CacheConsts.DEFAULT_LOCAL_LIMIT;

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }
}
