package xyz.vopen.mixmicro.components.enhance.spi.spring.context.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.lang.NonNull;

import static xyz.vopen.mixmicro.components.enhance.spi.spring.context.SpringMixmicroExtensionLoader.finishedExtensionInitialize;

/**
 * {@link SpringContextEventListener}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
public class SpringContextEventListener implements ApplicationListener<ApplicationEvent> {

  /**
   * Handle an application event.
   *
   * @param event the event to respond to
   */
  @Override
  public void onApplicationEvent(@NonNull ApplicationEvent event) {

    if (event instanceof ContextStartedEvent) {
      // TODO invoked {@link IntegrateExtensionInitializer#initialize}
      // TODO ..
    }

    if (event instanceof ContextRefreshedEvent) {
      finishedExtensionInitialize();
    }
  }
}
