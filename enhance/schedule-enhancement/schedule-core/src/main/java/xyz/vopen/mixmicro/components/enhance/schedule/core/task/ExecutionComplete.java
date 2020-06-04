package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class ExecutionComplete {
  private final Execution execution;
  private final Instant timeStarted;
  private final Instant timeDone;
  private final Result result;
  private final Throwable cause;

  ExecutionComplete(
      Execution execution, Instant timeStarted, Instant timeDone, Result result, Throwable cause) {
    this.timeStarted = timeStarted;
    this.cause = cause;
    if (result == Result.OK && cause != null) {
      throw new IllegalArgumentException("Result 'OK' should never have a cause.");
    }
    this.execution = execution;
    this.timeDone = timeDone;
    this.result = result;
  }

  public static ExecutionComplete success(
      Execution execution, Instant timeStarted, Instant timeDone) {
    return new ExecutionComplete(execution, timeStarted, timeDone, Result.OK, null);
  }

  public static ExecutionComplete failure(
      Execution execution, Instant timeStarted, Instant timeDone, Throwable cause) {
    return new ExecutionComplete(execution, timeStarted, timeDone, Result.FAILED, cause);
  }

  /** Simulated ExecutionComplete used to generate first execution-time from a Schedule. */
  public static ExecutionComplete simulatedSuccess(Instant timeDone) {
    TaskInstance nonExistingTaskInstance = new TaskInstance("non-existing-task", "non-existing-id");
    Execution nonExistingExecution =
        new Execution(
            timeDone,
            nonExistingTaskInstance,
            false,
            "simulated-picked-by",
            timeDone,
            null,
            0,
            null,
            1);
    return new ExecutionComplete(
        nonExistingExecution, timeDone.minus(Duration.ofSeconds(1)), timeDone, Result.OK, null);
  }

  public Execution getExecution() {
    return execution;
  }

  public Instant getTimeDone() {
    return timeDone;
  }

  public Duration getDuration() {
    return Duration.between(timeStarted, timeDone);
  }

  public Result getResult() {
    return result;
  }

  public Optional<Throwable> getCause() {
    return Optional.ofNullable(cause);
  }

  public enum Result {
    OK,
    FAILED
  }
}
