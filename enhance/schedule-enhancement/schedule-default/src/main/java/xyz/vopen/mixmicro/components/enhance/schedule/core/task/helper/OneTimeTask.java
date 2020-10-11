package xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.*;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.CompletionHandler.OnCompleteRemove;

import java.time.Duration;

public abstract class OneTimeTask<T> extends Task<T> {

  public OneTimeTask(String name, Class<T> dataClass) {
    this(
        name,
        dataClass,
        new FailureHandler.OnFailureRetryLater<>(Duration.ofMinutes(5)),
        new DeadExecutionHandler.ReviveDeadExecution<>());
  }

  public OneTimeTask(
      String name,
      Class<T> dataClass,
      FailureHandler<T> failureHandler,
      DeadExecutionHandler<T> deadExecutionHandler) {
    super(name, dataClass, failureHandler, deadExecutionHandler);
  }

  @Override
  public CompletionHandler<T> execute(
      TaskInstance<T> instance, ExecutionContext context) {
    executeOnce(instance, context);
    return new OnCompleteRemove<>();
  }

  public abstract void executeOnce(TaskInstance<T> taskInstance, ExecutionContext executionContext);
}
