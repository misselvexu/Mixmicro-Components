package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

import xyz.vopen.mixmicro.components.enhance.schedule.core.SchedulerClient;
import xyz.vopen.mixmicro.components.enhance.schedule.core.SchedulerState;

public class ExecutionContext {

  private final SchedulerState schedulerState;
  private final Execution execution;
  private final SchedulerClient schedulerClient;

  public ExecutionContext(
      SchedulerState schedulerState, Execution execution, SchedulerClient schedulerClient) {
    this.schedulerState = schedulerState;
    this.execution = execution;
    this.schedulerClient = schedulerClient;
  }

  public SchedulerState getSchedulerState() {
    return schedulerState;
  }

  /**
   * Primarily enables ExecutionHandlers to schedule new tasks. Can not be used to modify the
   * "current" execution.
   */
  public SchedulerClient getSchedulerClient() {
    return schedulerClient;
  }

  public Execution getExecution() {
    return execution;
  }
}
