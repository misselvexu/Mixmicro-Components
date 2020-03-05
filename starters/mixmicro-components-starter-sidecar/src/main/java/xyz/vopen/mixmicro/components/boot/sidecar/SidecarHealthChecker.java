package xyz.vopen.mixmicro.components.boot.sidecar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.core.env.ConfigurableEnvironment;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.TimeUnit;

public class SidecarHealthChecker {

  private static final Logger log = LoggerFactory.getLogger(SidecarHealthChecker.class);

  private final SidecarDiscoveryClient sidecarDiscoveryClient;

  private final HealthIndicator healthIndicator;

  private final SidecarProperties sidecarProperties;

  private final ConfigurableEnvironment environment;

  public SidecarHealthChecker(
      SidecarDiscoveryClient sidecarDiscoveryClient,
      HealthIndicator healthIndicator,
      SidecarProperties sidecarProperties,
      ConfigurableEnvironment environment) {
    this.sidecarDiscoveryClient = sidecarDiscoveryClient;
    this.healthIndicator = healthIndicator;
    this.sidecarProperties = sidecarProperties;
    this.environment = environment;
  }

  public void check() {
    Schedulers.single()
        .schedulePeriodically(
            () -> {
              String ip = sidecarProperties.getIp();
              Integer port = sidecarProperties.getPort();

              Status status = healthIndicator.health().getStatus();
              String applicationName = environment.getProperty("spring.application.name");

              if (status.equals(Status.UP)) {
                this.sidecarDiscoveryClient.registerInstance(applicationName, ip, port);
                log.debug(
                    "Health check success. register this instance. applicationName = {}, ip = {}, port = {}, status = {}",
                    applicationName,
                    ip,
                    port,
                    status);
              } else {
                log.warn(
                    "Health check failed. unregister this instance. applicationName = {}, ip = {}, port = {}, status = {}",
                    applicationName,
                    ip,
                    port,
                    status);
                this.sidecarDiscoveryClient.deregisterInstance(applicationName, ip, port);
              }
            },
            0,
            sidecarProperties.getHealthCheckInterval(),
            TimeUnit.MILLISECONDS);
  }
}
