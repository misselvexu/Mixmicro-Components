package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

public interface CompletableExecutionHandler<T> {

  /**
   * Task Executor
   *
   * @param instance instance of {@link TaskInstance} with genic-type
   * @param context instance of {@link ExecutionContext }
   * @return {@link CompletionHandler}
   */
  CompletionHandler<T> execute(TaskInstance<T> instance, ExecutionContext context);
}
