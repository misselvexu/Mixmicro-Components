package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

import java.time.Instant;
import java.util.Objects;

@SuppressWarnings("rawtypes")
public final class Execution {
  public final TaskInstance taskInstance;
  public final Instant executionTime;
  public final boolean picked;
  public final String pickedBy;
  public int consecutiveFailures;
  public final Instant lastHeartbeat;
  public final long version;
  public final Instant lastFailure;
  public final Instant lastSuccess;

  public Execution(Instant executionTime, TaskInstance taskInstance) {
    this(executionTime, taskInstance, false, null, null, null, 0, null, 1L);
  }

  public Execution(
      Instant executionTime,
      TaskInstance taskInstance,
      boolean picked,
      String pickedBy,
      Instant lastSuccess,
      Instant lastFailure,
      int consecutiveFailures,
      Instant lastHeartbeat,
      long version) {
    this.executionTime = executionTime;
    this.taskInstance = taskInstance;
    this.picked = picked;
    this.pickedBy = pickedBy;
    this.lastFailure = lastFailure;
    this.lastSuccess = lastSuccess;
    this.consecutiveFailures = consecutiveFailures;
    this.lastHeartbeat = lastHeartbeat;
    this.version = version;
  }

  public Instant getExecutionTime() {
    return executionTime;
  }

  public boolean isPicked() {
    return picked;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Execution execution = (Execution) o;
    return Objects.equals(executionTime, execution.executionTime)
        && Objects.equals(taskInstance, execution.taskInstance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(executionTime, taskInstance);
  }

  @Override
  public String toString() {
    return "Execution: "
        + "task="
        + taskInstance.getTaskName()
        + ", id="
        + taskInstance.getId()
        + ", executionTime="
        + executionTime
        + ", picked="
        + picked
        + ", pickedBy="
        + pickedBy
        + ", lastHeartbeat="
        + lastHeartbeat
        + ", version="
        + version;
  }
}
