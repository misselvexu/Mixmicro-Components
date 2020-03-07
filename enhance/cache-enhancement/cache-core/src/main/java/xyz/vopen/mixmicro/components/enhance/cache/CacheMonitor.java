package xyz.vopen.mixmicro.components.enhance.cache;

import xyz.vopen.mixmicro.components.enhance.cache.event.CacheEvent;

/**
 * Created on 2018/10/25.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
@FunctionalInterface
public interface CacheMonitor {

  void afterOperation(CacheEvent event);
}
