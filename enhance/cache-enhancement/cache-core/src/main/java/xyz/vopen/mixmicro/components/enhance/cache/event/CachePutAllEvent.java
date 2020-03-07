package xyz.vopen.mixmicro.components.enhance.cache.event;

import xyz.vopen.mixmicro.components.enhance.cache.Cache;
import xyz.vopen.mixmicro.components.enhance.cache.CacheResult;

import java.util.Map;

/**
 * Created on 2018/2/22.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CachePutAllEvent extends CacheEvent {
  private final long millis;
  /** key, value map. */
  private final Map map;

  private final CacheResult result;

  public CachePutAllEvent(Cache cache, long millis, Map map, CacheResult result) {
    super(cache);
    this.millis = millis;
    this.map = map;
    this.result = result;
  }

  public long getMillis() {
    return millis;
  }

  public Map getMap() {
    return map;
  }

  public CacheResult getResult() {
    return result;
  }
}
