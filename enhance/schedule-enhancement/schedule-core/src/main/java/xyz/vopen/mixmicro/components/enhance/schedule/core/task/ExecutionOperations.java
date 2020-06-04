package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

import xyz.vopen.mixmicro.components.enhance.schedule.core.TaskRepository;

import java.time.Instant;

public class ExecutionOperations<T> {

  private final TaskRepository taskRepository;
  private final Execution execution;

  public ExecutionOperations(TaskRepository taskRepository, Execution execution) {
    this.taskRepository = taskRepository;
    this.execution = execution;
  }

  public void stop() {
    taskRepository.remove(execution);
  }

  public void reschedule(ExecutionComplete completed, Instant nextExecutionTime) {
    if (completed.getResult() == ExecutionComplete.Result.OK) {
      taskRepository.reschedule(
          execution, nextExecutionTime, completed.getTimeDone(), execution.lastFailure, 0);
    } else {
      taskRepository.reschedule(
          execution,
          nextExecutionTime,
          execution.lastSuccess,
          completed.getTimeDone(),
          execution.consecutiveFailures + 1);
    }
  }

  public void reschedule(ExecutionComplete completed, Instant nextExecutionTime, T newData) {
    if (completed.getResult() == ExecutionComplete.Result.OK) {
      taskRepository.reschedule(
          execution, nextExecutionTime, newData, completed.getTimeDone(), execution.lastFailure, 0);
    } else {
      taskRepository.reschedule(
          execution,
          nextExecutionTime,
          newData,
          execution.lastSuccess,
          completed.getTimeDone(),
          execution.consecutiveFailures + 1);
    }
  }
}
