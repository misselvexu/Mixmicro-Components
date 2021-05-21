package xyz.vopen.mixmicro.components.boot.redlimiter;

import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link RedLimiterReference}
 *
 * <p>Class RedlimiterReference Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2021/5/21
 */
public final class RedLimiterReference {

  /**
   * Rate Limiter Reference Cache
   *
   * <p>
   */
  private static final AtomicReference<RedLimiter> CACHE = new AtomicReference<>();

  /**
   * Fetch Current Reference .
   *
   * @return reference instance of {@link RedLimiter}
   */
  public static RedLimiter limiter() {
    return CACHE.get();
  }
}
