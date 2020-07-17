package xyz.vopen.mixmicro.components.health;

/**
 * The health check procedure interface.
 * Invoked by consumers to verify the healthiness of a computing node.
 * Unhealthy nodes are expected to be terminated.
 *
 * @author Heiko Braun
 * @since 13.06.17
 */
@FunctionalInterface
public interface HealthCheck {

  /**
   * Invokes the health check procedure provided by the implementation of this interface.
   *
   * @return {@link HealthCheckResponse} object containing information about the health check result
   */
  HealthCheckResponse call();
}
