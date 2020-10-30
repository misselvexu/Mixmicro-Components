package xyz.vopen.mixmicro.components.boot.httpclient;

import java.net.URI;

@FunctionalInterface
public interface ServiceInstanceChooser {

  /**
   * Chooses a ServiceInstance URI from the LoadBalancer for the specified service.
   *
   * @param serviceId The service ID to look up the LoadBalancer.
   * @return Return the uri of ServiceInstance
   */
  URI choose(String serviceId);
}
