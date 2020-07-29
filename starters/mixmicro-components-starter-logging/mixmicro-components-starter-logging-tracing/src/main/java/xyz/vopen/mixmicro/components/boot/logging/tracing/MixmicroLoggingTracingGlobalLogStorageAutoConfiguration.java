package xyz.vopen.mixmicro.components.boot.logging.tracing;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.framework.logging.client.global.MixmicroLogging;
import xyz.vopen.framework.logging.client.global.support.MixmicroLoggingMemoryStorage;

/**
 * global log repository configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConditionalOnClass(MixmicroLogging.class)
public class MixmicroLoggingTracingGlobalLogStorageAutoConfiguration {
  /**
   * Instance global log memory mode repository
   *
   * @return {@link MixmicroLoggingMemoryStorage}
   */
  @Bean
  @ConditionalOnProperty(
      prefix = MixmicroLoggingTracingProperties.MIXMICRO_BOOT_LOGGING_PREFIX,
      name = "global-logging-repository-away",
      havingValue = "memory",
      matchIfMissing = true)
  public MixmicroLogging globalLogging() {
    return new MixmicroLoggingMemoryStorage();
  }
}
