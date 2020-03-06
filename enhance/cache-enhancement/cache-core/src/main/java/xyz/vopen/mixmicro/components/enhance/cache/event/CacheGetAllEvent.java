package xyz.vopen.mixmicro.components.enhance.cache.event;

import xyz.vopen.mixmicro.components.enhance.cache.Cache;
import xyz.vopen.mixmicro.components.enhance.cache.MultiGetResult;

import java.util.Set;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheGetAllEvent extends CacheEvent {
    private final long millis;
    private final Set keys;
    private final MultiGetResult result;

    public CacheGetAllEvent(Cache cache, long millis, Set keys, MultiGetResult result) {
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

    public MultiGetResult getResult() {
        return result;
    }
}
