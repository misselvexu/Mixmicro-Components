package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

public interface VoidExecutionHandler<T> {
  void execute(TaskInstance<T> taskInstance, ExecutionContext executionContext);
}
