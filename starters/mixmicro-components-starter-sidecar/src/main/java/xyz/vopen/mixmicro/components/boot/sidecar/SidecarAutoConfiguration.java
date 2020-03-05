package xyz.vopen.mixmicro.components.boot.sidecar;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.client.RestTemplate;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SidecarProperties.class)
public class SidecarAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public SidecarHealthIndicator sidecarHealthIndicator(
      SidecarProperties sidecarProperties, RestTemplate restTemplate) {
    return new SidecarHealthIndicator(sidecarProperties, restTemplate);
  }

  @Bean
  public SidecarHealthChecker sidecarHealthChecker(
      SidecarDiscoveryClient sidecarDiscoveryClient,
      SidecarHealthIndicator sidecarHealthIndicator,
      SidecarProperties sidecarProperties,
      ConfigurableEnvironment environment) {
    SidecarHealthChecker cleaner =
        new SidecarHealthChecker(
            sidecarDiscoveryClient, sidecarHealthIndicator, sidecarProperties, environment);
    cleaner.check();
    return cleaner;
  }
}
