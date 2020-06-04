package xyz.vopen.mixmicro.components.enhance.schedule.core;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.TaskInstanceId;

import java.time.Instant;

public class ClientEvent {

  enum EventType {
    SCHEDULE,
    RESCHEDULE,
    CANCEL
  }

  private ClientEventContext ctx;

  public ClientEvent(ClientEventContext ctx) {
    this.ctx = ctx;
  }

  public ClientEventContext getContext() {
    return ctx;
  }

  public static class ClientEventContext {
    private final EventType eventType;
    private final TaskInstanceId taskInstanceId;
    private final Instant executionTime;

    public ClientEventContext(
        EventType eventType, TaskInstanceId taskInstanceId, Instant executionTime) {
      this.eventType = eventType;
      this.taskInstanceId = taskInstanceId;
      this.executionTime = executionTime;
    }

    public EventType getEventType() {
      return eventType;
    }

    public TaskInstanceId getTaskInstanceId() {
      return taskInstanceId;
    }

    public Instant getExecutionTime() {
      return executionTime;
    }
  }
}
