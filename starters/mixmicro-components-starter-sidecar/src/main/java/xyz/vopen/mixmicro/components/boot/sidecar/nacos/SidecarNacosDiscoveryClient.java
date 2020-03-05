package xyz.vopen.mixmicro.components.boot.sidecar.nacos;

import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.boot.sidecar.SidecarDiscoveryClient;

public class SidecarNacosDiscoveryClient implements SidecarDiscoveryClient {

  private static final Logger log = LoggerFactory.getLogger(SidecarNacosDiscoveryClient.class);

  private final SidecarNacosDiscoveryProperties sidecarNacosDiscoveryProperties;

  public SidecarNacosDiscoveryClient(
      SidecarNacosDiscoveryProperties sidecarNacosDiscoveryProperties) {
    this.sidecarNacosDiscoveryProperties = sidecarNacosDiscoveryProperties;
  }

  @Override
  public void registerInstance(String applicationName, String ip, Integer port) {
    try {
      this.sidecarNacosDiscoveryProperties
          .namingServiceInstance()
          .registerInstance(applicationName, ip, port);
    } catch (NacosException e) {
      log.warn("nacos exception happens", e);
    }
  }

  @Override
  public void deregisterInstance(String applicationName, String ip, Integer port) {
    try {
      this.sidecarNacosDiscoveryProperties
          .namingServiceInstance()
          .deregisterInstance(applicationName, ip, port);
    } catch (NacosException e) {
      log.warn("nacos exception happens", e);
    }
  }
}
