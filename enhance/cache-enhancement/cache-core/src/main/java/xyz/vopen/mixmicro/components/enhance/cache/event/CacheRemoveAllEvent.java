package xyz.vopen.mixmicro.components.enhance.cache.event;

import xyz.vopen.mixmicro.components.enhance.cache.Cache;
import xyz.vopen.mixmicro.components.enhance.cache.CacheResult;

import java.util.Set;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheRemoveAllEvent extends CacheEvent {
  private final long millis;
  private final Set keys;
  private final CacheResult result;

  public CacheRemoveAllEvent(Cache cache, long millis, Set keys, CacheResult result) {
    super(cache);
    this.millis = millis;
    this.keys = keys;
    this.result = result;
  }

  public long getMillis() {
    return millis;
  }

  public Set getKeys() {
    return keys;
  }

  public CacheResult getResult() {
    return result;
  }
}
