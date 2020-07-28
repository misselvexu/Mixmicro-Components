package xyz.vopen.mixmicro.components.boot.logging.tracing.admin.ui;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static xyz.vopen.mixmicro.components.boot.logging.tracing.admin.ui.MixmicroLoggingTracingAdminUiProperties.MIXMICRO_BOOT_LOGGING_ADMIN_UI_PREFIX;

/**
 * Mixmicro Boot Logging Admin Ui Properties
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConfigurationProperties(prefix = MIXMICRO_BOOT_LOGGING_ADMIN_UI_PREFIX)
@Data
public class MixmicroLoggingTracingAdminUiProperties {
  /**
   * Mixmicro Boot logging properties config prefix
   */
  public static final String MIXMICRO_BOOT_LOGGING_ADMIN_UI_PREFIX = "mixmicro.logging.tracing.admin.ui";
  /**
   * Mixmicro Boot Logging Admin Ui Resource Locations
   */
  private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {"classpath:/META-INF/mixmicro-logging-tracing-admin-ui/"};
  /**
   * Locations of Mixmicro Boot Logging Admin ui resources.
   */
  private String[] resourceLocations = CLASSPATH_RESOURCE_LOCATIONS;
  /**
   * Locations of Mixmicro Boot Logging Admin ui template.
   */
  private String templateLocation = CLASSPATH_RESOURCE_LOCATIONS[0];
  /**
   * Wether the thymeleaf templates should be cached.
   */
  private boolean cacheTemplates = true;
  /**
   * Page Title
   */
  private String title = "Mixmicro Logging Admin";
  /**
   * Mixmicro Boot Logo
   */
  private String brand = "<img src=\"assets/img/icon-white.png\">";
}
