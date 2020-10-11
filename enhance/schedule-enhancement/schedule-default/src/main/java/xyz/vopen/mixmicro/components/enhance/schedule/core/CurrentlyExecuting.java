package xyz.vopen.mixmicro.components.enhance.schedule.core;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.Execution;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.TaskInstance;

import java.time.Duration;
import java.time.Instant;

@SuppressWarnings("rawtypes")
public class CurrentlyExecuting {

  private final Execution execution;
  private final Clock clock;
  private final Instant startTime;

  public CurrentlyExecuting(Execution execution, Clock clock) {
    this.execution = execution;
    this.clock = clock;
    this.startTime = clock.now();
  }

  public Duration getDuration() {
    return Duration.between(startTime, clock.now());
  }

  public TaskInstance getTaskInstance() {
    return execution.taskInstance;
  }
}
