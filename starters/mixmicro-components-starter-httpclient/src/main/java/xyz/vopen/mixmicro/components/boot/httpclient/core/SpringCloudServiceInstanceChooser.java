package xyz.vopen.mixmicro.components.boot.httpclient.core;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.net.URI;

public class SpringCloudServiceInstanceChooser implements ServiceInstanceChooser {

  private final LoadBalancerClient loadBalancerClient;

  public SpringCloudServiceInstanceChooser(LoadBalancerClient loadBalancerClient) {
    this.loadBalancerClient = loadBalancerClient;
  }

  /**
   * Chooses a ServiceInstance URI from the LoadBalancer for the specified service.
   *
   * @param serviceId The service ID to look up the LoadBalancer.
   * @return Return the uri of ServiceInstance
   */
  @Override
  public URI choose(String serviceId) {
    ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
    return serviceInstance.getUri();
  }
}
