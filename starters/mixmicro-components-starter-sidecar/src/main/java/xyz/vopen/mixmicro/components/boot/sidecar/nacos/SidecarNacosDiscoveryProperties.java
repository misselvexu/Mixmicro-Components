package xyz.vopen.mixmicro.components.boot.sidecar.nacos;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.apache.commons.lang3.StringUtils;
import xyz.vopen.mixmicro.components.boot.sidecar.SidecarProperties;

import java.net.SocketException;

public class SidecarNacosDiscoveryProperties extends NacosDiscoveryProperties {

  private final SidecarProperties sidecarProperties;

  public SidecarNacosDiscoveryProperties(SidecarProperties sidecarProperties) {
    this.sidecarProperties = sidecarProperties;
  }

  @Override
  public void init() throws SocketException {
    super.init();

    String ip = sidecarProperties.getIp();
    if (StringUtils.isNotBlank(ip)) {
      this.setIp(ip);
    }

    Integer port = sidecarProperties.getPort();
    this.setPort(port);
  }
}
