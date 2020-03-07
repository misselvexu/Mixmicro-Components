package xyz.vopen.mixmicro.components.enhance.cache.embedded;

import xyz.vopen.mixmicro.components.enhance.cache.CacheConfig;
import xyz.vopen.mixmicro.components.enhance.cache.annotation.CacheConsts;

/**
 * Created on 16/9/7.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class EmbeddedCacheConfig<K, V> extends CacheConfig<K, V> {
  private int limit = CacheConsts.DEFAULT_LOCAL_LIMIT;

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }
}
