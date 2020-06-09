package xyz.vopen.mixmicro.components.enhance.spi.spring.context;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;

/**
 * {@link MixmicroExtensionApplicationContextInitializer}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
public class MixmicroExtensionApplicationContextInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  /**
   * Initialize the given application context.
   *
   * @param applicationContext the application to configure
   */
  @Override
  public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
    SpringMixmicroExtensionLoader.setApplicationContext(applicationContext);
  }
}
