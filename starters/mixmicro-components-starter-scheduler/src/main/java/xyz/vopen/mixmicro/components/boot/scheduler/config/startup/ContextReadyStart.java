package xyz.vopen.mixmicro.components.boot.scheduler.config.startup;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;

/**
 * {@link ContextReadyStart}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/4
 */
public class ContextReadyStart extends AbstractSchedulerStarter {
  public ContextReadyStart(Scheduler scheduler) {
    super(scheduler);
  }

  @EventListener(ContextRefreshedEvent.class)
  public void whenContextIsReady() {
    doStart();
  }
}
