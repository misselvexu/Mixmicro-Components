package xyz.vopen.mixmicro.components.boot.logging.tracing;

import lombok.Getter;
import xyz.vopen.framework.logging.client.global.MixmicroLogging;
import xyz.vopen.framework.logging.client.global.support.MixmicroLoggingMemoryStorage;

/**
 * The global logging repository away
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @see MixmicroLogging
 */
@Getter
public enum MixmicroLoggingTracingStorageWay {
  /**
   * memory away
   *
   * @see MixmicroLoggingMemoryStorage
   */
  MEMORY
}
