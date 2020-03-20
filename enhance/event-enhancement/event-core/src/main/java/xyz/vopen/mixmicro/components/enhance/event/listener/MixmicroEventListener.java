package xyz.vopen.mixmicro.components.enhance.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.bus.event.DefaultRemoteApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * {@link MixmicroEventListener}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/20
 */
public abstract class MixmicroEventListener
    implements ApplicationListener<DefaultRemoteApplicationEvent<?>> {

  private static final Logger log = LoggerFactory.getLogger(MixmicroEventListener.class);

  /**
   * Handle an application event.
   *
   * @param event the event to respond to
   */
  @Override
  public void onApplicationEvent(DefaultRemoteApplicationEvent<?> event) {
    if (log.isDebugEnabled()) {
      log.debug("[ON MIX EVENT] , {}", event);
    }
    onEvent(event);
  }

  /**
   * Handle an application event.
   *
   * @param event the event to respond to
   */
  public abstract void onEvent(DefaultRemoteApplicationEvent<?> event);
}
