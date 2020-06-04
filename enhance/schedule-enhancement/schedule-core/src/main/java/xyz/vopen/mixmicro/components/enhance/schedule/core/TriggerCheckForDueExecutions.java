package xyz.vopen.mixmicro.components.enhance.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

class TriggerCheckForDueExecutions implements SchedulerClientEventListener {
  private static final Logger LOG = LoggerFactory.getLogger(TriggerCheckForDueExecutions.class);
  private SchedulerState schedulerState;
  private Clock clock;
  private Waiter executeDueWaiter;

  public TriggerCheckForDueExecutions(
      SchedulerState schedulerState, Clock clock, Waiter executeDueWaiter) {
    this.schedulerState = schedulerState;
    this.clock = clock;
    this.executeDueWaiter = executeDueWaiter;
  }

  @Override
  public void newEvent(ClientEvent event) {
    ClientEvent.ClientEventContext ctx = event.getContext();
    ClientEvent.EventType eventType = ctx.getEventType();

    if (!schedulerState.isStarted() || schedulerState.isShuttingDown()) {
      LOG.debug(
          "Will not act on scheduling event for execution (task: '{}', id: '{}') as scheduler is starting or shutting down.",
          ctx.getTaskInstanceId().getTaskName(),
          ctx.getTaskInstanceId().getId());
      return;
    }

    if (eventType == ClientEvent.EventType.SCHEDULE
        || eventType == ClientEvent.EventType.RESCHEDULE) {

      Instant scheduledToExecutionTime = ctx.getExecutionTime();
      if (scheduledToExecutionTime.toEpochMilli() <= clock.now().toEpochMilli()) {
        LOG.info(
            "Task-instance scheduled to run directly, triggering check for due exections (unless it is already running). Task: {}, instance: {}",
            ctx.getTaskInstanceId().getTaskName(),
            ctx.getTaskInstanceId().getId());
        executeDueWaiter.wake();
      }
    }
  }
}
