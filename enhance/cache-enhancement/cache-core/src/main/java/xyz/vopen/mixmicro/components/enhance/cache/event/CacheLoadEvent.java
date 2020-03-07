package xyz.vopen.mixmicro.components.enhance.cache.event;

import xyz.vopen.mixmicro.components.enhance.cache.Cache;

/**
 * Created on 2018/2/22.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheLoadEvent extends CacheEvent {
  private final long millis;
  private final Object key;
  private final Object loadedValue;
  private final boolean success;

  public CacheLoadEvent(Cache cache, long millis, Object key, Object loadedValue, boolean success) {
    super(cache);
    this.millis = millis;
    this.key = key;
    this.loadedValue = loadedValue;
    this.success = success;
  }

  public long getMillis() {
    return millis;
  }

  public Object getKey() {
    return key;
  }

  public Object getLoadedValue() {
    return loadedValue;
  }

  public boolean isSuccess() {
    return success;
  }
}
