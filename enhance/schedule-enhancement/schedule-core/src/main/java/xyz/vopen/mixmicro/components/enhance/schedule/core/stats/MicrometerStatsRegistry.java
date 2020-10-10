package xyz.vopen.mixmicro.components.enhance.schedule.core.stats;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.ExecutionComplete;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.Task;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class MicrometerStatsRegistry implements StatsRegistry {

  private static final String RESULT_SUCCESS = "ok";
  private static final String RESULT_FAILURE = "failed";
  private final MeterRegistry meterRegistry;

  private Map<String, MetricsHolder> metricsMap = new HashMap<>();

  public MicrometerStatsRegistry(
      MeterRegistry meterRegistry, List<? extends Task<?>> expectedTasks) {
    this.meterRegistry = meterRegistry;
    initializeMetricsForAllTasks(expectedTasks);
  }

  private void initializeMetricsForAllTasks(List<? extends Task<?>> expectedTasks) {
    expectedTasks.forEach(task -> getOrInitMetricHolder(task.getName()));
  }

  private MetricsHolder getOrInitMetricHolder(String taskName) {
    MetricsHolder preSynchronizedValue = metricsMap.get(taskName);
    if (preSynchronizedValue != null) {
      return preSynchronizedValue;
    } else {
      // Thread-safe init
      synchronized (this) {
        MetricsHolder currentValue = metricsMap.get(taskName);
        if (currentValue == null) {
          currentValue = new MetricsHolder(taskName);
          metricsMap.put(taskName, currentValue);
        }
        return currentValue;
      }
    }
  }

  @Override
  public void register(SchedulerStatsEvent e) {}

  @Override
  public void register(CandidateStatsEvent e) {}

  @Override
  public void register(ExecutionStatsEvent e) {}

  @Override
  public void registerSingleCompletedExecution(ExecutionComplete completeEvent) {
    String taskName = completeEvent.getExecution().taskInstance.getTaskName();
    MetricsHolder metrics = getOrInitMetricHolder(taskName);
    metrics.registerExecution(completeEvent);
  }

  private class MetricsHolder {
    private final AtomicReference<Double> lastDurationForTask = new AtomicReference<>((double) 0);
    private final AtomicLong lastRunTimestampForTask = new AtomicLong(0);
    private final Counter successesForTask;
    private final Counter failuresForTask;
    private final Timer durationsForTask;

    MetricsHolder(String taskName) {
      Gauge.builder("mixmicro_scheduler_task_last_run_duration", lastDurationForTask::get)
          .description("Duration in seconds for last execution of this task")
          .tag("task", taskName)
          .register(meterRegistry);

      Gauge.builder("mixmicro_scheduler_task_last_run_timestamp_seconds", lastRunTimestampForTask::get)
          .description("Time when last run completed")
          .tag("task", taskName)
          .register(meterRegistry);

      successesForTask =
          Counter.builder("mixmicro_scheduler_task_completions")
              .description("Successes and failures by task")
              .tag("task", taskName)
              .tag("result", RESULT_SUCCESS)
              .register(meterRegistry);

      failuresForTask =
          Counter.builder("mixmicro_scheduler_task_completions")
              .description("Successes and failures by task")
              .tag("task", taskName)
              .tag("result", RESULT_FAILURE)
              .register(meterRegistry);

      durationsForTask =
          Timer.builder("mixmicro_scheduler_task_duration")
              .description("Duration of executions")
              .tag("task", taskName)
              .register(meterRegistry);
    }

    void registerExecution(ExecutionComplete completeEvent) {
      lastDurationForTask.set((completeEvent.getDuration().toNanos()) / 1E9);
      lastRunTimestampForTask.set(completeEvent.getTimeDone().getEpochSecond());

      durationsForTask.record(completeEvent.getDuration().toMillis(), TimeUnit.MILLISECONDS);
      if (completeEvent.getResult() == ExecutionComplete.Result.OK) {
        successesForTask.increment();
      } else {
        failuresForTask.increment();
      }
    }
  }
}
