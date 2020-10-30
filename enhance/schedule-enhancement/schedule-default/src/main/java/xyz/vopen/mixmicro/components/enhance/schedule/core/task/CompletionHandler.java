package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public interface CompletionHandler<T> {

  void complete(ExecutionComplete executionComplete, ExecutionOperations<T> executionOperations);

  class OnCompleteRemove<T> implements CompletionHandler<T> {

    @Override
    public void complete(
        ExecutionComplete executionComplete, ExecutionOperations<T> executionOperations) {
      executionOperations.stop();
    }
  }

  class OnCompleteReschedule<T> implements CompletionHandler<T> {

    private static final Logger log = LoggerFactory.getLogger(OnCompleteReschedule.class);
    private final Schedule schedule;

    public OnCompleteReschedule(Schedule schedule) {
      this.schedule = schedule;
    }

    @Override
    public void complete(
        ExecutionComplete executionComplete, ExecutionOperations<T> executionOperations) {
      Instant nextExecution = schedule.getNextExecutionTime(executionComplete);
      log.debug(
          "Rescheduling task {} to {}",
          executionComplete.getExecution().taskInstance,
          nextExecution);
      executionOperations.reschedule(executionComplete, nextExecution);
    }
  }
}
