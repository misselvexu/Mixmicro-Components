package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

import java.time.Instant;
import java.util.Optional;

public class ExecutionFailed {
  private final Execution execution;
  private final Instant timeDone;
  private final Throwable cause;

  public ExecutionFailed(Execution execution, Instant timeDone, Throwable cause) {
    this.cause = cause;
    this.execution = execution;
    this.timeDone = timeDone;
  }

  public Execution getExecution() {
    return execution;
  }

  public Instant getTimeDone() {
    return timeDone;
  }

  public Optional<Throwable> getCause() {
    return Optional.ofNullable(cause);
  }
}
