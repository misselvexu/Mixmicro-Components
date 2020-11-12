package xyz.vopen.mixmicro.components.boot.autohttpclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.vopen.mixmicro.components.boot.httpclient.degrade.FallbackFactory;

/**
 * {@link DemoHttpApiFallbackFactory}
 *
 * <p>Class DemoHttpApiFallbackFactory Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/12
 */
@Component
public class DemoHttpApiFallbackFactory implements FallbackFactory<DemoHttpApi> {

  private static final Logger log = LoggerFactory.getLogger(DemoHttpApiFallbackFactory.class);

  /**
   * Returns an instance of the fallback appropriate for the given cause
   *
   * @param cause fallback cause
   * @return an instance that implements the client interface.
   */
  @Override
  public DemoHttpApi build(Throwable cause) {
    log.warn("[熔断]", cause);
    return new DemoHttpApiFallback();
  }
}
