package xyz.vopen.mixmicro.components.boot.scheduler.listener;

import xyz.vopen.mixmicro.components.boot.scheduler.AbstractMixmicroSchedulerLifecycle;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;

import javax.annotation.PostConstruct;

public class BeanPostInitializedListener extends AbstractMixmicroSchedulerLifecycle {

  public BeanPostInitializedListener(final Scheduler scheduler) {
    super(scheduler);
  }

  @PostConstruct
  void startImmediately() {
    startup();
  }
}
