package xyz.vopen.mixmicro.components.enhance.cache.event;

import xyz.vopen.mixmicro.components.enhance.cache.Cache;

import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheLoadAllEvent extends CacheEvent {

  private long millis;
  private Set keys;
  private Map loadedValue;
  private boolean success;

  public CacheLoadAllEvent(Cache cache, long millis, Set keys, Map loadedValue, boolean success) {
    super(cache);
    this.millis = millis;
    this.keys = keys;
    this.loadedValue = loadedValue;
    this.success = success;
  }

  public long getMillis() {
    return millis;
  }

  public Set getKeys() {
    return keys;
  }

  public Map getLoadedValue() {
    return loadedValue;
  }

  public boolean isSuccess() {
    return success;
  }
}
