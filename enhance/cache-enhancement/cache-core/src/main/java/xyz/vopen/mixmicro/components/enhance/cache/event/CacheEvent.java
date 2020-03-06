package xyz.vopen.mixmicro.components.enhance.cache.event;

import xyz.vopen.mixmicro.components.enhance.cache.Cache;

/**
 * The CacheEvent is used in single JVM while CacheMessage used for distributed message.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheEvent {

  protected Cache cache;

  public CacheEvent(Cache cache) {
    this.cache = cache;
  }

  public Cache getCache() {
    return cache;
  }
}
