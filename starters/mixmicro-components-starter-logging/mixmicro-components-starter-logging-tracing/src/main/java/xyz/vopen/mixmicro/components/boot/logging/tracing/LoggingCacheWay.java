package xyz.vopen.mixmicro.components.boot.logging.tracing;

import lombok.Getter;

/**
 * Mixmicro Boot Logging Cache Away
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Getter
public enum LoggingCacheWay {
  /** cache request log to memory */
  MEMORY,
  /** append request log to file */
  FILE
}
