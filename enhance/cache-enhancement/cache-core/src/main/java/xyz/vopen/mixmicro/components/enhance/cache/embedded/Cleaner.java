package xyz.vopen.mixmicro.components.enhance.cache.embedded;

import xyz.vopen.mixmicro.components.enhance.cache.support.MixCacheExecutor;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
class Cleaner {

    static LinkedList<WeakReference<LinkedHashMapCache>> linkedHashMapCaches = new LinkedList<>();

    static {
        ScheduledExecutorService executorService = MixCacheExecutor.defaultExecutor();
        executorService.scheduleWithFixedDelay(() -> run(), 60, 60, TimeUnit.SECONDS);
    }

    static void add(LinkedHashMapCache cache) {
        synchronized (linkedHashMapCaches) {
            linkedHashMapCaches.add(new WeakReference<>(cache));
        }
    }

    static void run() {
        synchronized (linkedHashMapCaches) {
            Iterator<WeakReference<LinkedHashMapCache>> it = linkedHashMapCaches.iterator();
            while (it.hasNext()) {
                WeakReference<LinkedHashMapCache> ref = it.next();
                LinkedHashMapCache c = ref.get();
                if (c == null) {
                    it.remove();
                } else {
                    c.cleanExpiredEntry();
                }
            }
        }
    }

}
