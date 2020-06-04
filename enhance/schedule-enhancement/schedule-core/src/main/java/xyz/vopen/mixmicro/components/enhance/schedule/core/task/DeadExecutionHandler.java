package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public interface DeadExecutionHandler<T> {
  void deadExecution(Execution execution, ExecutionOperations<T> executionOperations);

  class ReviveDeadExecution<T> implements DeadExecutionHandler<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ReviveDeadExecution.class);

    @Override
    public void deadExecution(Execution execution, ExecutionOperations<T> executionOperations) {
      final Instant now = Instant.now();
      LOG.info("Reviving dead execution: " + execution + " to " + now);
      executionOperations.reschedule(
          new ExecutionComplete(execution, now, now, ExecutionComplete.Result.FAILED, null), now);
    }
  }

  class CancelDeadExecution<T> implements DeadExecutionHandler<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ReviveDeadExecution.class);

    @Override
    public void deadExecution(Execution execution, ExecutionOperations<T> executionOperations) {
      LOG.warn("Cancelling dead execution: " + execution);
      executionOperations.stop();
    }
  }
}
