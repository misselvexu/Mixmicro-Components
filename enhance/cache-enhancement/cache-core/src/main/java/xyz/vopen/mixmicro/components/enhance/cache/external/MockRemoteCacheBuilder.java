package xyz.vopen.mixmicro.components.enhance.cache.external;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class MockRemoteCacheBuilder<T extends ExternalCacheBuilder<T>> extends ExternalCacheBuilder<T> {

    public static class MockRemoteCacheBuilderImpl extends MockRemoteCacheBuilder<MockRemoteCacheBuilderImpl> {
    }

    public static MockRemoteCacheBuilderImpl createMockRemoteCacheBuilder(){
        return new MockRemoteCacheBuilderImpl();
    }

    @Override
    public MockRemoteCacheConfig getConfig() {
        if (config == null) {
            config = new MockRemoteCacheConfig();
        }
        return (MockRemoteCacheConfig) config;
    }

    public MockRemoteCacheBuilder() {
        this.setKeyPrefix("DEFAULT_PREFIX");
        buildFunc((c) -> new MockRemoteCache((MockRemoteCacheConfig) c));
    }

    public T limit(int limit){
        getConfig().setLimit(limit);
        return self();
    }

    public void setLimit(int limit){
        getConfig().setLimit(limit);
    }
}
