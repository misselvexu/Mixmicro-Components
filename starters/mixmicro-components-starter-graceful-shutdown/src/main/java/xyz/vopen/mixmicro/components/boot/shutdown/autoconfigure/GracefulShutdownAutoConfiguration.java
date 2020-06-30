package xyz.vopen.mixmicro.components.boot.shutdown.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static xyz.vopen.mixmicro.components.boot.shutdown.autoconfigure.GracefulShutdownProperties.MIXMICRO_GRACEFUL_SHUTDOWN_PROPERTIES_PREFIX;

/**
 * {@link GracefulShutdownAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version v1.0 - 12/10/2018.
 */
@ConditionalOnProperty(
    havingValue = "true",
    prefix = MIXMICRO_GRACEFUL_SHUTDOWN_PROPERTIES_PREFIX,
    name = "enabled")
@ConditionalOnWebApplication
@Configuration
@EnableConfigurationProperties(GracefulShutdownProperties.class)
public class GracefulShutdownAutoConfiguration {
  @Bean
  @ConditionalOnMissingBean
  public GracefulShutdownHealthIndicator gracefulShutdownHealthIndicator(
      ApplicationContext ctx, GracefulShutdownProperties props) {

    return new GracefulShutdownHealthIndicator(ctx, props);
  }

  @Bean
  @ConditionalOnMissingBean
  public GracefulShutdownTomcatContainerCustomizer gracefulShutdownTomcatContainerCustomizer(
      GracefulShutdownTomcatConnectorCustomizer connectorCustomizer) {

    return new GracefulShutdownTomcatContainerCustomizer(connectorCustomizer);
  }

  @Bean
  @ConditionalOnMissingBean
  public GracefulShutdownTomcatConnectorCustomizer gracefulShutdownTomcatConnectorCustomizer(
      ApplicationContext ctx, GracefulShutdownProperties props) {

    return new GracefulShutdownTomcatConnectorCustomizer(ctx, props);
  }
}
