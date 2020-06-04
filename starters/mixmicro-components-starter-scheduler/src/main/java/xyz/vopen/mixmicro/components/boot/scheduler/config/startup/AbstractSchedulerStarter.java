package xyz.vopen.mixmicro.components.boot.scheduler.config.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.boot.scheduler.config.MixmicroSchedulerStarter;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.SchedulerState;

import java.util.Objects;

/**
 * {@link AbstractSchedulerStarter}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/4
 */
public abstract class AbstractSchedulerStarter implements MixmicroSchedulerStarter {
  private final Logger log = LoggerFactory.getLogger(this.getClass());
  private final Scheduler scheduler;

  protected AbstractSchedulerStarter(Scheduler scheduler) {
    this.scheduler = Objects.requireNonNull(scheduler, "A scheduler must be provided");
  }

  @Override
  public void doStart() {
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
