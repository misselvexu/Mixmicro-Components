package xyz.vopen.mixmicro.components.boot.sidecar;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.*;
import java.util.Enumeration;

import static xyz.vopen.mixmicro.components.boot.sidecar.SidecarProperties.SIDECAR_CONFIG_PREFIX;

@Validated
@ConfigurationProperties(prefix = SIDECAR_CONFIG_PREFIX)
public class SidecarProperties implements InitializingBean {

  private static final Logger log = LoggerFactory.getLogger(SidecarProperties.class);

  public static final String SIDECAR_CONFIG_PREFIX = "mixmicro.sidecar";

  /**
   * polyglot service's ip.
   */
  private String ip;

  /**
   * polyglot service's port.
   */
  @NotNull
  @Max(65535)
  @Min(1)
  private Integer port;

  /**
   * polyglot service's health check url. this endpoint must return json and the format must follow
   * spring boot actuator's health endpoint. eg. {"status": "UP"}.
   */
  private URI healthCheckUrl;

  /**
   * interval of health check.
   */
  private long healthCheckInterval = 30000L;

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public URI getHealthCheckUrl() {
    return healthCheckUrl;
  }

  public void setHealthCheckUrl(URI healthCheckUrl) {
    this.healthCheckUrl = healthCheckUrl;
  }

  public long getHealthCheckInterval() {
    return healthCheckInterval;
  }

  public void setHealthCheckInterval(long healthCheckInterval) {
    this.healthCheckInterval = healthCheckInterval;
  }

  @Override
  public void afterPropertiesSet() throws Exception {

    if (StringUtils.isBlank(ip)) {
      this.ip = findFirstNonLoopbackAddress().getHostAddress();
    }

  }

  public InetAddress findFirstNonLoopbackAddress() {
    InetAddress result = null;
    try {
      int lowest = Integer.MAX_VALUE;
      for (Enumeration<NetworkInterface> nics = NetworkInterface
          .getNetworkInterfaces(); nics.hasMoreElements(); ) {
        NetworkInterface ifc = nics.nextElement();
        if (ifc.isUp()) {
          if (ifc.getIndex() < lowest || result == null) {
            lowest = ifc.getIndex();
          } else {
            continue;
          }

          // @formatter:off
            for (Enumeration<InetAddress> addrs = ifc
                .getInetAddresses(); addrs.hasMoreElements();) {
              InetAddress address = addrs.nextElement();
              if (address instanceof Inet4Address
                  && !address.isLoopbackAddress()) {
                result = address;
              }
            }
          // @formatter:on
        }
      }
    } catch (IOException ex) {
      log.error("Cannot get first non-loopback address", ex);
    }

    if (result != null) {
      return result;
    }

    try {
      return InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      log.warn("Unable to retrieve localhost");
    }

    return null;
  }
}
