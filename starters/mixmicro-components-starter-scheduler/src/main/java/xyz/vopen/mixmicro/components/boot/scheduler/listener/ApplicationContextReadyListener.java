package xyz.vopen.mixmicro.components.boot.scheduler.listener;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import xyz.vopen.mixmicro.components.boot.scheduler.AbstractMixmicroSchedulerLifecycle;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;

public class ApplicationContextReadyListener extends AbstractMixmicroSchedulerLifecycle {

  public ApplicationContextReadyListener(Scheduler scheduler) {
    super(scheduler);
  }

  @EventListener(ContextRefreshedEvent.class)
  public void whenContextIsReady() {
    startup();
  }
}
