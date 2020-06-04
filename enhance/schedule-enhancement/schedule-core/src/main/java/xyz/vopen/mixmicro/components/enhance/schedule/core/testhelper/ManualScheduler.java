package xyz.vopen.mixmicro.components.enhance.schedule.core.testhelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.schedule.core.*;
import xyz.vopen.mixmicro.components.enhance.schedule.core.stats.StatsRegistry;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.OnStartup;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ManualScheduler extends Scheduler {
  private static final Logger LOG = LoggerFactory.getLogger(ManualScheduler.class);
  private final SettableClock clock;

  ManualScheduler(
      SettableClock clock,
      TaskRepository taskRepository,
      TaskResolver taskResolver,
      int maxThreads,
      ExecutorService executorService,
      SchedulerName schedulerName,
      Waiter waiter,
      Duration heartbeatInterval,
      boolean executeImmediately,
      StatsRegistry statsRegistry,
      int pollingLimit,
      Duration deleteUnresolvedAfter,
      List<OnStartup> onStartup) {
    super(
        clock,
        taskRepository,
        taskResolver,
        maxThreads,
        executorService,
        schedulerName,
        waiter,
        heartbeatInterval,
        executeImmediately,
        statsRegistry,
        pollingLimit,
        deleteUnresolvedAfter,
        onStartup);
    this.clock = clock;
  }

  public SettableClock getClock() {
    return clock;
  }

  public void tick(Duration moveClockForward) {
    clock.set(clock.now.plus(moveClockForward));
  }

  public void setTime(Instant newtime) {
    clock.set(newtime);
  }

  public void runAnyDueExecutions() {
    super.executeDue();
  }

  public void runDeadExecutionDetection() {
    super.detectDeadExecutions();
  }

  @Override
  public void start() {
    LOG.info("Starting manual scheduler. Executing on-startup tasks.");
    executeOnStartup();
  }
}
