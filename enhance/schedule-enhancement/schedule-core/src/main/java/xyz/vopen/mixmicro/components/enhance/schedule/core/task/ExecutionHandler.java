package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

public interface ExecutionHandler<T> {

  /**
   * Task Executor
   *
   * @param taskInstance instance of {@link TaskInstance} with genic-type
   * @param executionContext instance of {@link ExecutionContext }
   * @return {@link CompletionHandler}
   */
  CompletionHandler<T> execute(TaskInstance<T> taskInstance, ExecutionContext executionContext);
}
