package xyz.vopen.mixmicro.components.boot.shutdown.autoconfigure;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 * {@link GracefulShutdownTomcatContainerCustomizer}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version v1.0 - 12/10/2018.
 */
public class GracefulShutdownTomcatContainerCustomizer
    implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

  private final GracefulShutdownTomcatConnectorCustomizer connectorCustomizer;

  public GracefulShutdownTomcatContainerCustomizer(
      GracefulShutdownTomcatConnectorCustomizer connectorCustomizer) {
    this.connectorCustomizer = connectorCustomizer;
  }

  @Override
  public void customize(TomcatServletWebServerFactory factory) {
    factory.addConnectorCustomizers(connectorCustomizer);
  }
}
