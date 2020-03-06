package xyz.vopen.mixmicro.components.enhance.cache.embedded;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CaffeineCacheBuilder<T extends EmbeddedCacheBuilder<T>>
    extends EmbeddedCacheBuilder<T> {
  protected CaffeineCacheBuilder() {
    buildFunc((c) -> new CaffeineCache((EmbeddedCacheConfig) c));
  }

  public static CaffeineCacheBuilderImpl createCaffeineCacheBuilder() {
    return new CaffeineCacheBuilderImpl();
  }

  public static class CaffeineCacheBuilderImpl
      extends CaffeineCacheBuilder<CaffeineCacheBuilderImpl> {}
}
