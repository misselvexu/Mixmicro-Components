package xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.*;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.CompletionHandler.OnCompleteRemove;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.DeadExecutionHandler.ReviveDeadExecution;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.FailureHandler.OnFailureRetryLater;

import java.time.Duration;

public abstract class OneTimeTask<T> extends Task<T> {

  public OneTimeTask(String name, Class<T> dataClass) {
    this(
        name,
        dataClass,
        new OnFailureRetryLater<>(Duration.ofMinutes(5)),
        new ReviveDeadExecution<>());
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
      TaskInstance<T> taskInstance, ExecutionContext executionContext) {
    executeOnce(taskInstance, executionContext);
    return new OnCompleteRemove<>();
  }

  public abstract void executeOnce(TaskInstance<T> taskInstance, ExecutionContext executionContext);
}
