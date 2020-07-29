package xyz.vopen.mixmicro.components.boot.logging.tracing;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;
import xyz.vopen.framework.logging.client.admin.discovery.LoggingAdminDiscovery;
import xyz.vopen.framework.logging.client.admin.discovery.support.LoggingRegistryCenterAdminDiscovery;

/**
 * Mixmicro Boot Logging Admin Discovery Configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@ConditionalOnClass(LoadBalancerClient.class)
@EnableConfigurationProperties(MixmicroLoggingTracingProperties.class)
@ConditionalOnProperty(prefix = MixmicroLoggingTracingProperties.MIXMICRO_BOOT_LOGGING_PREFIX, name = "discovery.service-id")
public class MixmicroLoggingTracingAdminDiscoveryAutoConfiguration {
  /**
   * Mixmicro Boot Logging Properties
   */
  private MixmicroLoggingTracingProperties mixmicroLoggingTracingProperties;

  public MixmicroLoggingTracingAdminDiscoveryAutoConfiguration(
      MixmicroLoggingTracingProperties mixmicroLoggingTracingProperties) {
    this.mixmicroLoggingTracingProperties = mixmicroLoggingTracingProperties;
  }

  /**
   * Mixmicro Boot Logging Admin Registry Center Discovery setting basic auth username if not empty {@link
   * LoggingRegistryCenterAdminDiscovery#setUsername(String)} setting basic auth password if not
   * empty {@link LoggingRegistryCenterAdminDiscovery#setPassword(String)}
   *
   * @param loadBalancerClient LoadBalance Client
   * @return LoggingRegistryCenterAdminDiscovery
   */
  @Bean
  @ConditionalOnMissingBean
  public LoggingAdminDiscovery loggingRegistryCenterAdminDiscovery(
      LoadBalancerClient loadBalancerClient) {
    LoggingRegistryCenterAdminDiscovery registryCenterAdminDiscovery =
        new LoggingRegistryCenterAdminDiscovery(
            mixmicroLoggingTracingProperties.getDiscovery().getServiceId(), loadBalancerClient);
    String basicAuthUserName = mixmicroLoggingTracingProperties.getDiscovery().getUsername();
    if (!ObjectUtils.isEmpty(basicAuthUserName)) {
      registryCenterAdminDiscovery.setUsername(basicAuthUserName);
    }
    String basicAuthPassword = mixmicroLoggingTracingProperties.getDiscovery().getPassword();
    if (!ObjectUtils.isEmpty(basicAuthPassword)) {
      registryCenterAdminDiscovery.setPassword(basicAuthPassword);
    }
    return registryCenterAdminDiscovery;
  }
}
