package xyz.vopen.mixmicro.components.enhance.spi;

import xyz.vopen.mixmicro.components.enhance.spi.exception.MixmicroSpiException;

/**
 * {@link MixmicroExtensionPostProcessor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
public interface MixmicroExtensionPostProcessor {

  /**
   * Apply this {@code IntegrateExtensionPostProcessor} to the given new bean instance <i>after</i>
   * any bean initialization callbacks (like InitializingBean's {@code afterPropertiesSet} or a
   * custom init-method). The bean will already be populated with property values. The returned bean
   * instance may be a wrapper around the original.
   *
   * <p>The default implementation returns the given {@code bean} as-is.
   *
   * @param bean the new bean instance
   * @param beanName the name of the bean
   * @return the bean instance to use, either the original or a wrapped one; if {@code null}, no
   *     subsequent BeanPostProcessors will be invoked
   * @throws MixmicroSpiException in case of errors
   */
  default Object postProcessAfterInitialization(Object bean, String beanName)
      throws MixmicroSpiException {
    return bean;
  }
}
