package xyz.vopen.mixmicro.components.boot.logging.admin;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static xyz.vopen.mixmicro.components.boot.logging.admin.MixmicroLoggingTracingAdminProperties.MIXMICRO_BOOT_LOGGING_ADMIN_PREFIX;

/**
 * Mixmicro Boot Logging Admin Properties
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConfigurationProperties(prefix = MIXMICRO_BOOT_LOGGING_ADMIN_PREFIX)
@Data
public class MixmicroLoggingTracingAdminProperties {
  /**
   * Mixmicro Boot logging properties config prefix
   */
  public static final String MIXMICRO_BOOT_LOGGING_ADMIN_PREFIX = "mixmicro.logging.tracing.admin";
  /**
   * Whether to print the logs reported on the console
   */
  private boolean showConsoleReportLog = false;
  /**
   * Format console log JSON
   */
  private boolean formatConsoleLogJson = false;
}
