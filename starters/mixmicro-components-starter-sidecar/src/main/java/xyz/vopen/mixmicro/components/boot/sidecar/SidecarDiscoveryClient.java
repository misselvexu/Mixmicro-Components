package xyz.vopen.mixmicro.components.boot.sidecar;

public interface SidecarDiscoveryClient {

  /**
   * register instance.
   *
   * @param applicationName applicationName
   * @param ip ip
   * @param port port
   */
  void registerInstance(String applicationName, String ip, Integer port);

  /**
   * deregister instance.
   *
   * @param applicationName applicationName
   * @param ip ip
   * @param port port
   */
  void deregisterInstance(String applicationName, String ip, Integer port);
}
