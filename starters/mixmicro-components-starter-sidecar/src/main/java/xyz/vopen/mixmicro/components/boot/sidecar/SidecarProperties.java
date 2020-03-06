package xyz.vopen.mixmicro.components.boot.sidecar;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.net.URI;

import static xyz.vopen.mixmicro.components.boot.sidecar.SidecarProperties.SIDECAR_CONFIG_PREFIX;

@Validated
@ConfigurationProperties(prefix = SIDECAR_CONFIG_PREFIX)
public class SidecarProperties {

  public static final String SIDECAR_CONFIG_PREFIX = "mixmicro.sidecar";

  /** polyglot service's ip. */
  private String ip;

  /** polyglot service's port. */
  @NotNull
  @Max(65535)
  @Min(1)
  private Integer port;

  /**
   * polyglot service's health check url. this endpoint must return json and the format must follow
   * spring boot actuator's health endpoint. eg. {"status": "UP"}.
   */
  private URI healthCheckUrl;

  /** interval of health check. */
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
}
