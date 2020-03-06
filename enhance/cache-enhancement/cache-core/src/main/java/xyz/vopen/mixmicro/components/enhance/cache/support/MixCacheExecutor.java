package xyz.vopen.mixmicro.components.enhance.cache.support;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class MixCacheExecutor {
  protected static ScheduledExecutorService defaultExecutor;
  protected static ScheduledExecutorService heavyIOExecutor;

  private static int threadCount;

  static {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread() {
              @Override
              public void run() {
                if (defaultExecutor != null) {
                  defaultExecutor.shutdownNow();
                }
                if (heavyIOExecutor != null) {
                  heavyIOExecutor.shutdownNow();
                }
              }
            });
  }

  public static ScheduledExecutorService defaultExecutor() {
    if (defaultExecutor != null) {
      return defaultExecutor;
    }
    synchronized (MixCacheExecutor.class) {
      if (defaultExecutor == null) {
        ThreadFactory tf =
            r -> {
              Thread t = new Thread(r, "MixCacheDefaultExecutor");
              t.setDaemon(true);
              return t;
            };
        defaultExecutor =
            new ScheduledThreadPoolExecutor(1, tf, new ThreadPoolExecutor.DiscardPolicy());
      }
    }
    return defaultExecutor;
  }

  public static ScheduledExecutorService heavyIOExecutor() {
    if (heavyIOExecutor != null) {
      return heavyIOExecutor;
    }
    synchronized (MixCacheExecutor.class) {
      if (heavyIOExecutor == null) {
        ThreadFactory tf =
            r -> {
              Thread t = new Thread(r, "MixCacheHeavyIOExecutor" + threadCount++);
              t.setDaemon(true);
              return t;
            };
        heavyIOExecutor =
            new ScheduledThreadPoolExecutor(10, tf, new ThreadPoolExecutor.DiscardPolicy());
      }
    }
    return heavyIOExecutor;
  }

  public static void setDefaultExecutor(ScheduledExecutorService executor) {
    MixCacheExecutor.defaultExecutor = executor;
  }

  public static void setHeavyIOExecutor(ScheduledExecutorService heavyIOExecutor) {
    MixCacheExecutor.heavyIOExecutor = heavyIOExecutor;
  }
}
