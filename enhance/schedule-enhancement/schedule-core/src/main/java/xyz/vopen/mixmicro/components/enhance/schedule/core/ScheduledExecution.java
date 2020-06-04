package xyz.vopen.mixmicro.components.enhance.schedule.core;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.Execution;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.TaskInstanceId;

import java.time.Instant;
import java.util.Objects;

public class ScheduledExecution<DATA_TYPE> {
  private final Class<DATA_TYPE> dataClass;
  private final Execution execution;

  public ScheduledExecution(Class<DATA_TYPE> dataClass, Execution execution) {
    this.dataClass = dataClass;
    this.execution = execution;
  }

  public TaskInstanceId getTaskInstance() {
    return execution.taskInstance;
  }

  public Instant getExecutionTime() {
    return execution.getExecutionTime();
  }

  @SuppressWarnings("unchecked")
  public DATA_TYPE getData() {
    if (dataClass.isInstance(this.execution.taskInstance.getData())) {
      return (DATA_TYPE) this.execution.taskInstance.getData();
    }
    throw new DataClassMismatchException();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ScheduledExecution<?> that = (ScheduledExecution<?>) o;
    return Objects.equals(execution, that.execution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(execution);
  }

  public static class DataClassMismatchException extends RuntimeException {}
}
