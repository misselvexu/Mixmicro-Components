package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

public interface ExecutionHandler<T> {
  CompletionHandler<T> execute(TaskInstance<T> taskInstance, ExecutionContext executionContext);
}
