package xyz.vopen.mixmicro.components.boot.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.SchedulerState;

import java.util.Objects;

public abstract class AbstractMixmicroSchedulerLifecycle implements MixmicroSchedulerLifecycle {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private final Scheduler scheduler;

  protected AbstractMixmicroSchedulerLifecycle(Scheduler scheduler) {
    this.scheduler = Objects.requireNonNull(scheduler, "A scheduler must be provided");
  }

  @Override
  public void startup() {
    SchedulerState state = scheduler.getSchedulerState();

    if (state.isShuttingDown()) {
      log.warn("Scheduler is shutting down - will not attempting to start");
      return;
    }

    if (state.isStarted()) {
      log.info("Scheduler already started - will not attempt to start again");
      return;
    }

    log.info("Triggering scheduler start");
    scheduler.start();
  }
}
