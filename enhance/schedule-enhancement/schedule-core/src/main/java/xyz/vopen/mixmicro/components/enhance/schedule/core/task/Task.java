package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

public abstract class Task<T> implements ExecutionHandler<T> {
  protected final String name;
  private final FailureHandler<T> failureHandler;
  private final DeadExecutionHandler<T> deadExecutionHandler;
  private final Class<T> dataClass;

  public Task(
      String name,
      Class<T> dataClass,
      FailureHandler<T> failureHandler,
      DeadExecutionHandler<T> deadExecutionHandler) {
    this.name = name;
    this.dataClass = dataClass;
    this.failureHandler = failureHandler;
    this.deadExecutionHandler = deadExecutionHandler;
  }

  public String getName() {
    return name;
  }

  public Class<T> getDataClass() {
    return dataClass;
  }

  public TaskInstance<T> instance(String id) {
    return new TaskInstance<>(this.name, id);
  }

  public TaskInstance<T> instance(String id, T data) {
    return new TaskInstance<>(this.name, id, data);
  }

  public abstract CompletionHandler<T> execute(
      TaskInstance<T> taskInstance, ExecutionContext executionContext);

  public FailureHandler<T> getFailureHandler() {
    return failureHandler;
  }

  public DeadExecutionHandler<T> getDeadExecutionHandler() {
    return deadExecutionHandler;
  }

  @Override
  public String toString() {
    return "Task " + "task=" + getName();
  }
}
