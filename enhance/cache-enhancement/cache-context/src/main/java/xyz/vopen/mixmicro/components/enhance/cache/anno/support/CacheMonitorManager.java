package xyz.vopen.mixmicro.components.enhance.cache.anno.support;

import xyz.vopen.mixmicro.components.enhance.cache.Cache;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public interface CacheMonitorManager {
    void addMonitors(String area, String cacheName, Cache cache);
}
