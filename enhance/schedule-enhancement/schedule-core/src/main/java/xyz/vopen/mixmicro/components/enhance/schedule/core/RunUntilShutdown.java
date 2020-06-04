package xyz.vopen.mixmicro.components.enhance.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.schedule.core.stats.StatsRegistry;

class RunUntilShutdown implements Runnable {
  private static final Logger LOG = LoggerFactory.getLogger(RunUntilShutdown.class);
  private final Runnable toRun;
  private final Waiter waitBetweenRuns;
  private final SchedulerState schedulerState;
  private final StatsRegistry statsRegistry;

  public RunUntilShutdown(
      Runnable toRun,
      Waiter waitBetweenRuns,
      SchedulerState schedulerState,
      StatsRegistry statsRegistry) {
    this.toRun = toRun;
    this.waitBetweenRuns = waitBetweenRuns;
    this.schedulerState = schedulerState;
    this.statsRegistry = statsRegistry;
  }

  @Override
  public void run() {
    while (!schedulerState.isShuttingDown()) {
      try {
        toRun.run();
      } catch (Throwable e) {
        LOG.error("Unhandled exception. Will keep running.", e);
        statsRegistry.register(StatsRegistry.SchedulerStatsEvent.UNEXPECTED_ERROR);
      }

      try {
        waitBetweenRuns.doWait();
      } catch (InterruptedException interruptedException) {
        if (schedulerState.isShuttingDown()) {
          LOG.debug("Thread '{}' interrupted due to shutdown.", Thread.currentThread().getName());
        } else {
          LOG.error("Unexpected interruption of thread. Will keep running.", interruptedException);
          statsRegistry.register(StatsRegistry.SchedulerStatsEvent.UNEXPECTED_ERROR);
        }
      }
    }
  }
}
