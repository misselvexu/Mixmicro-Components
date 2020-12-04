package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

public interface VoidExecutionHandler<T> {

  /**
   * Task Executor
   *
   * @param instance instance of {@link TaskInstance} with genic-type
   * @param context instance of {@link ExecutionContext }
   */
  void execute(TaskInstance<T> instance, ExecutionContext context);
}
