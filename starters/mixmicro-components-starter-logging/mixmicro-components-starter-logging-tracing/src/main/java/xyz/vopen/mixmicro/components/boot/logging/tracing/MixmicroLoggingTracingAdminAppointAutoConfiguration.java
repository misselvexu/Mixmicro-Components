package xyz.vopen.mixmicro.components.boot.logging.tracing;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.framework.logging.client.admin.discovery.LoggingAdminDiscovery;
import xyz.vopen.framework.logging.client.admin.discovery.lb.LoadBalanceStrategy;
import xyz.vopen.framework.logging.client.admin.discovery.lb.support.RandomWeightedStrategy;
import xyz.vopen.framework.logging.client.admin.discovery.lb.support.SmoothWeightedRoundRobinStrategy;
import xyz.vopen.framework.logging.client.admin.discovery.support.LoggingAppointAdminDiscovery;
import xyz.vopen.framework.logging.client.admin.discovery.support.LoggingRegistryCenterAdminDiscovery;

/**
 * Mixmicro Boot Logging Admin Appoint Configuration
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Configuration
@EnableConfigurationProperties(MixmicroLoggingTracingProperties.class)
@ConditionalOnMissingBean(LoggingRegistryCenterAdminDiscovery.class)
@ConditionalOnProperty(prefix = MixmicroLoggingTracingProperties.MIXMICRO_BOOT_LOGGING_PREFIX, name = "admin.server-address")
public class MixmicroLoggingTracingAdminAppointAutoConfiguration {
  /**
   * Mixmicro Boot Logging Properties
   */
  private MixmicroLoggingTracingProperties mixmicroLoggingTracingProperties;

  public MixmicroLoggingTracingAdminAppointAutoConfiguration(
      MixmicroLoggingTracingProperties mixmicroLoggingTracingProperties) {
    this.mixmicroLoggingTracingProperties = mixmicroLoggingTracingProperties;
  }

  /**
   * Mixmicro Boot Logging Admin Config Discovery Multiple Use "," Separation
   *
   * @return LoggingAdminDiscovery
   */
  @Bean
  @ConditionalOnMissingBean
  public LoggingAdminDiscovery loggingConfigAdminDiscovery() {
    String[] serverAddressArray = mixmicroLoggingTracingProperties.getAdmin().getServerAddress().split(",");
    LoggingAppointAdminDiscovery appointAdminDiscovery =
        new LoggingAppointAdminDiscovery(serverAddressArray);
    LoadBalanceStrategy loadBalanceStrategy = instantiationLoadBalanceStrategy();
    appointAdminDiscovery.setLoadBalanceStrategy(loadBalanceStrategy);
    return appointAdminDiscovery;
  }

  /**
   * Create {@link LoadBalanceStrategy} by {@link LoadBalanceStrategyWay} default is use {@link
   * RandomWeightedStrategy}
   *
   * @return {@link LoadBalanceStrategy} support class instance
   */
  private LoadBalanceStrategy instantiationLoadBalanceStrategy() {
    LoadBalanceStrategyWay strategyAway = mixmicroLoggingTracingProperties.getLoadBalanceStrategy();
    LoadBalanceStrategy strategy;
    switch (strategyAway) {
      case POLL_WEIGHT:
        strategy = new SmoothWeightedRoundRobinStrategy();
        break;
      case RANDOM_WEIGHT:
      default:
        strategy = new RandomWeightedStrategy();
        break;
    }
    return strategy;
  }
}
