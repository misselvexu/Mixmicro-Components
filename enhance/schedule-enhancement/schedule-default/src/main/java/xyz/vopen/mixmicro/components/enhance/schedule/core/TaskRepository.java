package xyz.vopen.mixmicro.components.enhance.schedule.core;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.Execution;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface TaskRepository {

  boolean createIfNotExists(Execution execution);

  List<Execution> getDue(Instant now, int limit);

  void getScheduledExecutions(Consumer<Execution> consumer);

  void getScheduledExecutions(String taskName, Consumer<Execution> consumer);

  void remove(Execution execution);

  boolean reschedule(
      Execution execution,
      Instant nextExecutionTime,
      Instant lastSuccess,
      Instant lastFailure,
      int consecutiveFailures);

  boolean reschedule(
      Execution execution,
      Instant nextExecutionTime,
      Object newData,
      Instant lastSuccess,
      Instant lastFailure,
      int consecutiveFailures);

  Optional<Execution> pick(Execution e, Instant timePicked);

  List<Execution> getDeadExecutions(Instant olderThan);

  void updateHeartbeat(Execution execution, Instant heartbeatTime);

  List<Execution> getExecutionsFailingLongerThan(Duration interval);

  Optional<Execution> getExecution(String taskName, String taskInstanceId);

  int removeExecutions(String taskName);
}
