package xyz.vopen.mixmicro.components.enhance.cache.redis.springdata;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import xyz.vopen.mixmicro.components.enhance.cache.external.ExternalCacheBuilder;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class RedisSpringDataCacheBuilder<T extends ExternalCacheBuilder<T>>
    extends ExternalCacheBuilder<T> {
  protected RedisSpringDataCacheBuilder() {
    buildFunc(config -> new RedisSpringDataCache((RedisSpringDataCacheConfig) config));
  }

  public static RedisSpringDataCacheBuilderImpl createBuilder() {
    return new RedisSpringDataCacheBuilderImpl();
  }

  @Override
  public RedisSpringDataCacheConfig getConfig() {
    if (config == null) {
      config = new RedisSpringDataCacheConfig();
    }
    return (RedisSpringDataCacheConfig) config;
  }

  public T connectionFactory(RedisConnectionFactory connectionFactory) {
    getConfig().setConnectionFactory(connectionFactory);
    return self();
  }

  public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
    getConfig().setConnectionFactory(connectionFactory);
  }

  public static class RedisSpringDataCacheBuilderImpl
      extends RedisSpringDataCacheBuilder<RedisSpringDataCacheBuilderImpl> {}
}
