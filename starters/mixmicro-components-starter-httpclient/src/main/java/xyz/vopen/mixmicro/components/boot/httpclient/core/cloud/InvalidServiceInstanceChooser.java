package xyz.vopen.mixmicro.components.boot.httpclient.core.cloud;

import xyz.vopen.mixmicro.components.boot.httpclient.ServiceInstanceChooser;
import xyz.vopen.mixmicro.components.boot.httpclient.exception.ServiceInstanceChooseException;

import java.net.URI;

public class InvalidServiceInstanceChooser implements ServiceInstanceChooser {

  /**
   * Chooses a ServiceInstance URI from the LoadBalancer for the specified service.
   *
   * @param serviceId The service ID to look up the LoadBalancer.
   * @return Return the uri of ServiceInstance
   */
  @Override
  public URI choose(String serviceId) {
    throw new ServiceInstanceChooseException("No valid service instance selector .");
  }
}
