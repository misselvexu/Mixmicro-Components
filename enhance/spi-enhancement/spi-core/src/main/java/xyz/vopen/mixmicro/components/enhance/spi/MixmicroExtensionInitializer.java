package xyz.vopen.mixmicro.components.enhance.spi;

import xyz.vopen.mixmicro.components.enhance.spi.exception.MixmicroSpiException;

/**
 * {@link MixmicroExtensionInitializer}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
public interface MixmicroExtensionInitializer {

  /**
   * Application Initialize Action
   *
   * @throws MixmicroSpiException maybe thrown {@link MixmicroSpiException}
   */
  void initialize() throws MixmicroSpiException;
}
