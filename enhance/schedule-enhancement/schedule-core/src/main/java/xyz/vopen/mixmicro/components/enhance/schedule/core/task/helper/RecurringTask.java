package xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper;

import xyz.vopen.mixmicro.components.enhance.schedule.core.Clock;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.*;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.CompletionHandler.OnCompleteReschedule;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule.Schedule;

public abstract class RecurringTask<T> extends Task<T> implements OnStartup {

  public static final String INSTANCE = "recurring";
  private final OnCompleteReschedule<T> onComplete;
  private final ScheduleOnStartup<T> scheduleOnStartup;

  public RecurringTask(String name, Schedule schedule, Class<T> dataClass) {
    this(
        name,
        schedule,
        dataClass,
        new ScheduleRecurringOnStartup<>(INSTANCE, null, schedule),
        new FailureHandler.OnFailureReschedule<T>(schedule),
        new DeadExecutionHandler.ReviveDeadExecution<>());
  }

  public RecurringTask(String name, Schedule schedule, Class<T> dataClass, T initialData) {
    this(
        name,
        schedule,
        dataClass,
        new ScheduleRecurringOnStartup<>(INSTANCE, initialData, schedule),
        new FailureHandler.OnFailureReschedule<T>(schedule),
        new DeadExecutionHandler.ReviveDeadExecution<>());
  }

  public RecurringTask(
      String name,
      Schedule schedule,
      Class<T> dataClass,
      ScheduleRecurringOnStartup<T> scheduleOnStartup,
      FailureHandler<T> failureHandler,
      DeadExecutionHandler<T> deadExecutionHandler) {
    super(name, dataClass, failureHandler, deadExecutionHandler);
    onComplete = new OnCompleteReschedule<>(schedule);
    this.scheduleOnStartup = scheduleOnStartup;
  }

  @Override
  public void onStartup(Scheduler scheduler, Clock clock) {
    if (scheduleOnStartup != null) {
      scheduleOnStartup.apply(scheduler, clock, this);
    }
  }

  @Override
  public CompletionHandler<T> execute(
      TaskInstance<T> taskInstance, ExecutionContext executionContext) {
    executeRecurringly(taskInstance, executionContext);
    return onComplete;
  }

  public abstract void executeRecurringly(
      TaskInstance<T> taskInstance, ExecutionContext executionContext);
}
