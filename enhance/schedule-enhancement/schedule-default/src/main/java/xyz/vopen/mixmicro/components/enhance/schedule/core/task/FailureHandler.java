package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public interface FailureHandler<T> {

  void onFailure(ExecutionComplete executionComplete, ExecutionOperations<T> executionOperations);

  // TODO: Failure handler with backoff: if (isFailing(.)) then nextTry = 2*
  // duration_from_first_failure (minimum 1m, max 1d)
  class OnFailureRetryLater<T> implements FailureHandler<T> {

    private static final Logger log =
        LoggerFactory.getLogger(CompletionHandler.OnCompleteReschedule.class);
    private final Duration sleepDuration;

    public OnFailureRetryLater(Duration sleepDuration) {
      this.sleepDuration = sleepDuration;
    }

    @Override
    public void onFailure(
        ExecutionComplete executionComplete, ExecutionOperations<T> executionOperations) {
      Instant nextTry = Instant.now().plus(sleepDuration);
      log.debug(
          "Execution failed. Retrying task {} at {}",
          executionComplete.getExecution().taskInstance,
          nextTry);
      executionOperations.reschedule(executionComplete, nextTry);
    }
  }

  class OnFailureReschedule<T> implements FailureHandler<T> {

    private static final Logger log =
        LoggerFactory.getLogger(CompletionHandler.OnCompleteReschedule.class);
    private final Schedule schedule;

    public OnFailureReschedule(Schedule schedule) {
      this.schedule = schedule;
    }

    @Override
    public void onFailure(
        ExecutionComplete executionComplete, ExecutionOperations<T> executionOperations) {
      Instant nextExecution = schedule.getNextExecutionTime(executionComplete);
      log.debug(
          "Execution failed. Rescheduling task {} to {}",
          executionComplete.getExecution().taskInstance,
          nextExecution);
      executionOperations.reschedule(executionComplete, nextExecution);
    }
  }
}
