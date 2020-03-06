package xyz.vopen.mixmicro.components.enhance.cache.embedded;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class LinkedHashMapCacheBuilder<T extends EmbeddedCacheBuilder<T>> extends EmbeddedCacheBuilder<T> {
    public static class LinkedHashMapCacheBuilderImpl extends LinkedHashMapCacheBuilder<LinkedHashMapCacheBuilderImpl> {
    }

    public static LinkedHashMapCacheBuilderImpl createLinkedHashMapCacheBuilder() {
        return new LinkedHashMapCacheBuilderImpl();
    }

    protected LinkedHashMapCacheBuilder() {
        buildFunc((c) -> new LinkedHashMapCache((EmbeddedCacheConfig) c));
    }
}
