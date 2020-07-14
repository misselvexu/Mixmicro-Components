package xyz.vopen.mixmicro.components.enhancement.health.endpoints;

import xyz.vopen.mixmicro.components.health.HealthCheck;
import xyz.vopen.mixmicro.components.health.HealthCheckResponse;
import xyz.vopen.mixmicro.components.health.Readiness;

import javax.enterprise.context.ApplicationScoped;

/**
 * {@link ApplicationHealthCheck}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 7/14/20
 */
@Readiness
@ApplicationScoped
public class ApplicationHealthCheck implements HealthCheck {

  /**
   * Invokes the health check procedure provided by the implementation of this interface.
   *
   * @return {@link HealthCheckResponse} object containing information about the health check result
   */
  @Override
  public HealthCheckResponse call() {
    // application status
    return HealthCheckResponse.up("application-status");
  }
}
