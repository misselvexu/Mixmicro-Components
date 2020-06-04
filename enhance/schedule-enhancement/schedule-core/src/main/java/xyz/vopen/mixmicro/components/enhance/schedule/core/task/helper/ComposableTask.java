package xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.*;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule.Schedule;

import java.time.Duration;

@Deprecated
public class ComposableTask {

  public static RecurringTask<Void> recurringTask(
      String name, Schedule schedule, VoidExecutionHandler<Void> executionHandler) {
    return new RecurringTask<Void>(name, schedule, Void.class) {
      @Override
      public void executeRecurringly(
          TaskInstance<Void> taskInstance, ExecutionContext executionContext) {
        executionHandler.execute(taskInstance, executionContext);
      }
    };
  }

  public static <T> OneTimeTask<T> onetimeTask(
      String name, Class<T> dataClass, VoidExecutionHandler<T> executionHandler) {
    return new OneTimeTask<T>(name, dataClass) {
      @Override
      public void executeOnce(TaskInstance<T> taskInstance, ExecutionContext executionContext) {
        executionHandler.execute(taskInstance, executionContext);
      }
    };
  }

  public static <T> Task<T> customTask(
      String name,
      Class<T> dataClass,
      CompletionHandler<T> completionHandler,
      VoidExecutionHandler<T> executionHandler) {
    return new Task<T>(
        name,
        dataClass,
        new FailureHandler.OnFailureRetryLater<>(Duration.ofMinutes(5)),
        new DeadExecutionHandler.ReviveDeadExecution<>()) {
      @Override
      public CompletionHandler<T> execute(
          TaskInstance<T> taskInstance, ExecutionContext executionContext) {
        executionHandler.execute(taskInstance, executionContext);
        return completionHandler;
      }
    };
  }

  public static <T> Task<T> customTask(
      String name,
      Class<T> dataClass,
      CompletionHandler<T> completionHandler,
      FailureHandler<T> failureHandler,
      VoidExecutionHandler<T> executionHandler) {
    return new Task<T>(
        name, dataClass, failureHandler, new DeadExecutionHandler.ReviveDeadExecution<>()) {
      @Override
      public CompletionHandler<T> execute(
          TaskInstance<T> taskInstance, ExecutionContext executionContext) {
        executionHandler.execute(taskInstance, executionContext);
        return completionHandler;
      }
    };
  }
}
