package xyz.vopen.mixmicro.components.enhance.cache.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
@ConfigurationProperties(prefix = "mixcache")
public class MixCacheProperties {

    private String[] hiddenPackages;
    private int statIntervalMinutes;
    private boolean areaInCacheName = true;
    private boolean penetrationProtect = false;
    private boolean enableMethodCache = true;

    public MixCacheProperties(){
    }

    public String[] getHiddenPackages() {
        // keep same with GlobalCacheConfig
        return hiddenPackages;
    }

    public void setHiddenPackages(String[] hiddenPackages) {
        // keep same with GlobalCacheConfig
        this.hiddenPackages = hiddenPackages;
    }

    public void setHidePackages(String[] hidePackages) {
        // keep same with GlobalCacheConfig
        this.hiddenPackages = hidePackages;
    }

    public int getStatIntervalMinutes() {
        return statIntervalMinutes;
    }

    public void setStatIntervalMinutes(int statIntervalMinutes) {
        this.statIntervalMinutes = statIntervalMinutes;
    }

    public boolean isAreaInCacheName() {
        return areaInCacheName;
    }

    public void setAreaInCacheName(boolean areaInCacheName) {
        this.areaInCacheName = areaInCacheName;
    }

    public boolean isPenetrationProtect() {
        return penetrationProtect;
    }

    public void setPenetrationProtect(boolean penetrationProtect) {
        this.penetrationProtect = penetrationProtect;
    }

    public boolean isEnableMethodCache() {
        return enableMethodCache;
    }

    public void setEnableMethodCache(boolean enableMethodCache) {
        this.enableMethodCache = enableMethodCache;
    }
}
