package xyz.vopen.mixmicro.components.enhance.cache.embedded;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CaffeineCacheBuilder<T extends EmbeddedCacheBuilder<T>> extends EmbeddedCacheBuilder<T> {
    public static class CaffeineCacheBuilderImpl extends CaffeineCacheBuilder<CaffeineCacheBuilderImpl> {
    }

    public static CaffeineCacheBuilderImpl createCaffeineCacheBuilder() {
        return new CaffeineCacheBuilderImpl();
    }

    protected CaffeineCacheBuilder() {
        buildFunc((c) -> new CaffeineCache((EmbeddedCacheConfig) c));
    }
}
