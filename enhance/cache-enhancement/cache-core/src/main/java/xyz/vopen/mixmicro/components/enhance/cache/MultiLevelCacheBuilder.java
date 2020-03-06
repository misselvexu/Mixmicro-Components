package xyz.vopen.mixmicro.components.enhance.cache;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class MultiLevelCacheBuilder<T extends MultiLevelCacheBuilder<T>>
    extends AbstractCacheBuilder<T> {
  protected MultiLevelCacheBuilder() {
    buildFunc(config -> new MultiLevelCache((MultiLevelCacheConfig) config));
  }

  public static MultiLevelCacheBuilderImpl createMultiLevelCacheBuilder() {
    return new MultiLevelCacheBuilderImpl();
  }

  @Override
  public MultiLevelCacheConfig getConfig() {
    if (config == null) {
      config = new MultiLevelCacheConfig();
    }
    return (MultiLevelCacheConfig) config;
  }

  public T addCache(Cache... caches) {
    for (Cache c : caches) {
      getConfig().getCaches().add(c);
    }
    return self();
  }

  public void setCaches(List<Cache> caches) {
    getConfig().setCaches(caches);
  }

  public T useExpireOfSubCache(boolean useExpireOfSubCache) {
    getConfig().setUseExpireOfSubCache(useExpireOfSubCache);
    return self();
  }

  public void setUseExpireOfSubCache(boolean useExpireOfSubCache) {
    getConfig().setUseExpireOfSubCache(useExpireOfSubCache);
  }

  @Override
  public T keyConvertor(Function<Object, Object> keyConvertor) {
    throw new UnsupportedOperationException("MultiLevelCache do not need a key convertor");
  }

  @Override
  public void setKeyConvertor(Function<Object, Object> keyConvertor) {
    throw new UnsupportedOperationException("MultiLevelCache do not need a key convertor");
  }

  @Override
  public T expireAfterAccess(long defaultExpire, TimeUnit timeUnit) {
    throw new UnsupportedOperationException("MultiLevelCache do not support expireAfterAccess");
  }

  @Override
  public void setExpireAfterAccessInMillis(long expireAfterAccessInMillis) {
    throw new UnsupportedOperationException("MultiLevelCache do not support expireAfterAccess");
  }

  public static class MultiLevelCacheBuilderImpl
      extends MultiLevelCacheBuilder<MultiLevelCacheBuilderImpl> {}
}
