package xyz.vopen.mixmicro.components.enhance.cache.event;

import xyz.vopen.mixmicro.components.enhance.cache.Cache;
import xyz.vopen.mixmicro.components.enhance.cache.CacheResult;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CachePutEvent extends CacheEvent {
    private long millis;
    private Object key;
    private Object value;
    private CacheResult result;

    public CachePutEvent(Cache cache, long millis, Object key, Object value, CacheResult result) {
        super(cache);
        this.millis = millis;
        this.key = key;
        this.value = value;
        this.result = result;
    }

    public long getMillis() {
        return millis;
    }

    public Object getKey() {
        return key;
    }

    public CacheResult getResult() {
        return result;
    }

    public Object getValue() {
        return value;
    }

}
