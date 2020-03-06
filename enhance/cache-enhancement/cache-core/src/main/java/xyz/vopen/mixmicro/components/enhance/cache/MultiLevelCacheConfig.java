package xyz.vopen.mixmicro.components.enhance.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class MultiLevelCacheConfig<K, V> extends CacheConfig<K, V> {
    private List<Cache<K, V>> caches = new ArrayList<>();
    private boolean useExpireOfSubCache;

    @Override
    public MultiLevelCacheConfig clone() {
        MultiLevelCacheConfig copy = (MultiLevelCacheConfig) super.clone();
        if (caches != null) {
            copy.caches = new ArrayList(this.caches);
        }
        return copy;
    }

    public List<Cache<K, V>> getCaches() {
        return caches;
    }

    public void setCaches(List<Cache<K, V>> caches) {
        this.caches = caches;
    }

    public boolean isUseExpireOfSubCache() {
        return useExpireOfSubCache;
    }

    public void setUseExpireOfSubCache(boolean useExpireOfSubCache) {
        this.useExpireOfSubCache = useExpireOfSubCache;
    }
}
