package xyz.vopen.mixmicro.components.boot.logging.tracing;

import lombok.Getter;

/**
 * Mixmicro Boot Logging Cache Away
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Getter
public enum LoggingCacheAway {
  /**
   * cache request log to memory
   */
  memory,
  /**
   * append request log to file
   */
  file
}
