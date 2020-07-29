package xyz.vopen.mixmicro.components.boot.logging.tracing;

import xyz.vopen.framework.logging.client.LoggingFactoryBean;

/**
 * logging factory bean customizer Initialization logging factory bean calls all implementation
 * classes
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface LoggingFactoryBeanCustomizer {
  /**
   * Customize the given a {@link LoggingFactoryBean} object.
   *
   * @param factoryBean the logging factory bean
   */
  void customize(LoggingFactoryBean factoryBean);
}
