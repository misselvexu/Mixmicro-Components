package xyz.vopen.mixmicro.components.enhance.cache.anno.method;

import xyz.vopen.mixmicro.components.enhance.cache.anno.support.CacheInvalidateAnnoConfig;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.CacheUpdateAnnoConfig;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.CachedAnnoConfig;

import java.util.List;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheInvokeConfig {
    private CachedAnnoConfig cachedAnnoConfig;
    private List<CacheInvalidateAnnoConfig> invalidateAnnoConfigs;
    private CacheUpdateAnnoConfig updateAnnoConfig;
    private boolean enableCacheContext;

    private static final CacheInvokeConfig noCacheInvokeConfigInstance = new CacheInvokeConfig();

    public static CacheInvokeConfig getNoCacheInvokeConfigInstance() {
        return noCacheInvokeConfigInstance;
    }

    public CachedAnnoConfig getCachedAnnoConfig() {
        return cachedAnnoConfig;
    }

    public void setCachedAnnoConfig(CachedAnnoConfig cachedAnnoConfig) {
        this.cachedAnnoConfig = cachedAnnoConfig;
    }

    public boolean isEnableCacheContext() {
        return enableCacheContext;
    }

    public void setEnableCacheContext(boolean enableCacheContext) {
        this.enableCacheContext = enableCacheContext;
    }

    public List<CacheInvalidateAnnoConfig> getInvalidateAnnoConfigs() {
        return invalidateAnnoConfigs;
    }

    public void setInvalidateAnnoConfigs(List<CacheInvalidateAnnoConfig> invalidateAnnoConfigs) {
        this.invalidateAnnoConfigs = invalidateAnnoConfigs;
    }

    public CacheUpdateAnnoConfig getUpdateAnnoConfig() {
        return updateAnnoConfig;
    }

    public void setUpdateAnnoConfig(CacheUpdateAnnoConfig updateAnnoConfig) {
        this.updateAnnoConfig = updateAnnoConfig;
    }
}
