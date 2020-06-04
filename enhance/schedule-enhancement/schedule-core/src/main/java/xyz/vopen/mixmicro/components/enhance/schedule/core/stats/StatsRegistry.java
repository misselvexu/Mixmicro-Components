package xyz.vopen.mixmicro.components.enhance.schedule.core.stats;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.ExecutionComplete;

public interface StatsRegistry {

  enum SchedulerStatsEvent {
    UNEXPECTED_ERROR,
    COMPLETIONHANDLER_ERROR,
    FAILUREHANDLER_ERROR,
    DEAD_EXECUTION,
    RAN_UPDATE_HEARTBEATS,
    RAN_DETECT_DEAD,
    RAN_EXECUTE_DUE,
    UNRESOLVED_TASK
  }

  enum CandidateStatsEvent {
    STALE,
    ALREADY_PICKED,
    EXECUTED
  }

  enum ExecutionStatsEvent {
    COMPLETED,
    FAILED
  }

  void register(SchedulerStatsEvent e);

  void register(CandidateStatsEvent e);

  void register(ExecutionStatsEvent e);

  void registerSingleCompletedExecution(ExecutionComplete completeEvent);

  StatsRegistry NOOP = new DefaultStatsRegistry();

  class DefaultStatsRegistry implements StatsRegistry {

    @Override
    public void register(SchedulerStatsEvent e) {}

    @Override
    public void register(CandidateStatsEvent e) {}

    @Override
    public void register(ExecutionStatsEvent e) {}

    @Override
    public void registerSingleCompletedExecution(ExecutionComplete completeEvent) {}
  }
}
