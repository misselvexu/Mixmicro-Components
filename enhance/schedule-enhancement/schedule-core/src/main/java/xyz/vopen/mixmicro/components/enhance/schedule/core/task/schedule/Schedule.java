package xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.ExecutionComplete;

import java.time.Instant;

public interface Schedule {

  Instant getNextExecutionTime(ExecutionComplete executionComplete);

  /** Used to get the first execution-time for a schedule. Simulates an ExecutionComplete event. */
  default Instant getInitialExecutionTime(Instant now) {
    return getNextExecutionTime(ExecutionComplete.simulatedSuccess(now));
  }

  boolean isDeterministic();
}
