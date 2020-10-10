package xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper;

import xyz.vopen.mixmicro.components.enhance.schedule.core.task.*;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule.Schedule;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;

public class Tasks {
  public static final Duration DEFAULT_RETRY_INTERVAL = Duration.ofMinutes(5);

  public static RecurringTaskBuilder<Void> recurring(String name, Schedule schedule) {
    return new RecurringTaskBuilder<>(name, schedule, Void.class);
  }

  public static <T> RecurringTaskBuilder<T> recurring(
      String name, Schedule schedule, Class<T> dataClass) {
    return new RecurringTaskBuilder<>(name, schedule, dataClass);
  }

  public static OneTimeTaskBuilder<Void> oneTime(String name) {
    return new OneTimeTaskBuilder<>(name, Void.class);
  }

  public static <T> OneTimeTaskBuilder<T> oneTime(String name, Class<T> dataClass) {
    return new OneTimeTaskBuilder<>(name, dataClass);
  }

  public static <T> TaskBuilder<T> custom(String name, Class<T> dataClass) {
    return new TaskBuilder<>(name, dataClass);
  }

  public static class RecurringTaskBuilder<T> {
    private final String name;
    private final Schedule schedule;
    private Class<T> dataClass;
    private FailureHandler<T> onFailure;
    private DeadExecutionHandler<T> onDeadExecution;
    private ScheduleRecurringOnStartup<T> scheduleOnStartup;

    public RecurringTaskBuilder(String name, Schedule schedule, Class<T> dataClass) {
      this.name = name;
      this.schedule = schedule;
      this.dataClass = dataClass;
      this.onFailure = new FailureHandler.OnFailureReschedule<>(schedule);
      this.onDeadExecution = new DeadExecutionHandler.ReviveDeadExecution<>();
      this.scheduleOnStartup =
          new ScheduleRecurringOnStartup<>(RecurringTask.INSTANCE, null, schedule);
    }

    public RecurringTaskBuilder<T> onFailureReschedule() {
      this.onFailure = new FailureHandler.OnFailureReschedule<>(schedule);
      return this;
    }

    public RecurringTaskBuilder<T> onDeadExecutionRevive() {
      this.onDeadExecution = new DeadExecutionHandler.ReviveDeadExecution<>();
      return this;
    }

    public RecurringTaskBuilder<T> onFailure(FailureHandler<T> failureHandler) {
      this.onFailure = failureHandler;
      return this;
    }

    public RecurringTaskBuilder<T> onDeadExecution(DeadExecutionHandler<T> deadExecutionHandler) {
      this.onDeadExecution = deadExecutionHandler;
      return this;
    }

    public RecurringTaskBuilder<T> initialData(T initialData) {
      this.scheduleOnStartup =
          new ScheduleRecurringOnStartup<>(RecurringTask.INSTANCE, initialData, schedule);
      return this;
    }

    public RecurringTask<T> execute(VoidExecutionHandler<T> executionHandler) {
      return new RecurringTask<T>(
          name, schedule, dataClass, scheduleOnStartup, onFailure, onDeadExecution) {
        @Override
        public void executeRecurringly(TaskInstance<T> taskInstance, ExecutionContext executionContext) {
          executionHandler.execute(taskInstance, executionContext);
        }
      };
    }
  }

  public static class OneTimeTaskBuilder<T> {
    private final String name;
    private Class<T> dataClass;
    private FailureHandler<T> onFailure;
    private DeadExecutionHandler<T> onDeadExecution;

    public OneTimeTaskBuilder(String name, Class<T> dataClass) {
      this.name = name;
      this.dataClass = dataClass;
      this.onDeadExecution = new DeadExecutionHandler.ReviveDeadExecution<>();
      this.onFailure = new FailureHandler.OnFailureRetryLater<>(DEFAULT_RETRY_INTERVAL);
    }

    public OneTimeTaskBuilder<T> onFailureRetryLater() {
      this.onFailure = new FailureHandler.OnFailureRetryLater<>(DEFAULT_RETRY_INTERVAL);
      return this;
    }

    public OneTimeTaskBuilder<T> onDeadExecutionRevive() {
      this.onDeadExecution = new DeadExecutionHandler.ReviveDeadExecution<>();
      return this;
    }

    public OneTimeTaskBuilder<T> onFailure(FailureHandler<T> failureHandler) {
      this.onFailure = failureHandler;
      return this;
    }

    public OneTimeTaskBuilder<T> onDeadExecution(DeadExecutionHandler<T> deadExecutionHandler) {
      this.onDeadExecution = deadExecutionHandler;
      return this;
    }

    public OneTimeTask<T> execute(VoidExecutionHandler<T> executionHandler) {
      return new OneTimeTask<T>(name, dataClass, onFailure, onDeadExecution) {
        @Override
        public void executeOnce(TaskInstance<T> taskInstance, ExecutionContext executionContext) {
          executionHandler.execute(taskInstance, executionContext);
        }
      };
    }
  }

  public static class TaskBuilder<T> {
    private final String name;
    private Class<T> dataClass;
    private FailureHandler<T> onFailure;
    private DeadExecutionHandler<T> onDeadExecution;
    private ScheduleOnStartup<T> onStartup;

    public TaskBuilder(String name, Class<T> dataClass) {
      this.name = name;
      this.dataClass = dataClass;
      this.onDeadExecution = new DeadExecutionHandler.ReviveDeadExecution<>();
      this.onFailure = new FailureHandler.OnFailureRetryLater<T>(DEFAULT_RETRY_INTERVAL);
    }

    public TaskBuilder<T> onFailureReschedule(Schedule schedule) {
      this.onFailure = new FailureHandler.OnFailureReschedule<T>(schedule);
      return this;
    }

    public TaskBuilder<T> onDeadExecutionRevive() {
      this.onDeadExecution = new DeadExecutionHandler.ReviveDeadExecution<>();
      return this;
    }

    public TaskBuilder<T> onFailure(FailureHandler<T> failureHandler) {
      this.onFailure = failureHandler;
      return this;
    }

    public TaskBuilder<T> onDeadExecution(DeadExecutionHandler<T> deadExecutionHandler) {
      this.onDeadExecution = deadExecutionHandler;
      return this;
    }

    public TaskBuilder<T> scheduleOnStartup(
        String instance, T initialData, Function<Instant, Instant> firstExecutionTime) {
      this.onStartup = new ScheduleOnceOnStartup<T>(instance, initialData, firstExecutionTime);
      return this;
    }

    public CustomTask<T> execute(CompletableExecutionHandler<T> handler) {
      return new CustomTask<T>(name, dataClass, onStartup, onFailure, onDeadExecution) {
        @Override
        public CompletionHandler<T> execute(
            TaskInstance<T> instance, ExecutionContext context) {
          return handler.execute(instance, context);
        }
      };
    }
  }
}
