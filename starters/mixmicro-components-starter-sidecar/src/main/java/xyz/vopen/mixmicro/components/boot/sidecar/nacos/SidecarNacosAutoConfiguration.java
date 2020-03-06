package xyz.vopen.mixmicro.components.boot.sidecar.nacos;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.discovery.NacosDiscoveryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.mixmicro.components.boot.sidecar.SidecarAutoConfiguration;
import xyz.vopen.mixmicro.components.boot.sidecar.SidecarDiscoveryClient;
import xyz.vopen.mixmicro.components.boot.sidecar.SidecarProperties;

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({NacosDiscoveryAutoConfiguration.class, SidecarAutoConfiguration.class})
@ConditionalOnClass(NacosDiscoveryProperties.class)
public class SidecarNacosAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public SidecarNacosDiscoveryProperties sidecarNacosDiscoveryProperties(
      SidecarProperties sidecarProperties) {
    return new SidecarNacosDiscoveryProperties(sidecarProperties);
  }

  @Bean
  @ConditionalOnMissingBean
  public SidecarDiscoveryClient sidecarDiscoveryClient(
      SidecarNacosDiscoveryProperties sidecarNacosDiscoveryProperties) {
    return new SidecarNacosDiscoveryClient(sidecarNacosDiscoveryProperties);
  }
}
