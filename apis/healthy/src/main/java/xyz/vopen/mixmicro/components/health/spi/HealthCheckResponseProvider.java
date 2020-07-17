package xyz.vopen.mixmicro.components.health.spi;

import xyz.vopen.mixmicro.components.health.HealthCheckResponse;
import xyz.vopen.mixmicro.components.health.HealthCheckResponseBuilder;

/**
 * <p>
 * Reserved for implementors as means to supply their own HealthCheckResponseBuilder. This provider is located using the default
 * service loader and instantiated from the {@link HealthCheckResponse}
 * </p>
 * Created by hbraun on 07.07.17.
 */
public interface HealthCheckResponseProvider {

  /**
   * Provides an implementation of {@link HealthCheckResponseBuilder}.
   *
   * @return a vendor specific implemenatation of {@link HealthCheckResponseBuilder}
   */
  HealthCheckResponseBuilder createResponseBuilder();

}
