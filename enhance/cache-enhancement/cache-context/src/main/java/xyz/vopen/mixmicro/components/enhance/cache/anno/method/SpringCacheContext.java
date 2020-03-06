package xyz.vopen.mixmicro.components.enhance.cache.anno.method;

import xyz.vopen.mixmicro.components.enhance.cache.anno.support.CacheContext;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.ConfigMap;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.GlobalCacheConfig;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.SpringConfigProvider;
import org.springframework.context.ApplicationContext;

/**
 * Created on 2016/10/19.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class SpringCacheContext extends CacheContext {

    private ApplicationContext applicationContext;

    public SpringCacheContext(SpringConfigProvider configProvider, GlobalCacheConfig globalCacheConfig, ApplicationContext applicationContext) {
        super(configProvider, globalCacheConfig);
        this.applicationContext = applicationContext;
        init();
    }

    @Override
    protected CacheInvokeContext newCacheInvokeContext() {
        return new SpringCacheInvokeContext(applicationContext);
    }

    public void init() {
        if (applicationContext != null) {
            ConfigMap configMap = applicationContext.getBean(ConfigMap.class);
            cacheManager.setCacheCreator((area, cacheName) -> {
                CacheInvokeConfig cic = configMap.getByCacheName(area, cacheName);
                if (cic == null) {
                    throw new IllegalArgumentException("cache definition not found: area=" + area + ",cacheName=" + cacheName);
                }
                return __createOrGetCache(cic.getCachedAnnoConfig(), area, cacheName);
            });
        }
    }
}
