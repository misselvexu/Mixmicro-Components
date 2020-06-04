package xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper;

import xyz.vopen.mixmicro.components.enhance.schedule.core.Clock;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.*;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.CompletionHandler.OnCompleteReschedule;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.DeadExecutionHandler.ReviveDeadExecution;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.schedule.Schedule;

public abstract class RecurringTask<T> extends Task<T> implements OnStartup {

  public static final String INSTANCE = "recurring";
  private final OnCompleteReschedule<T> onComplete;
  private ScheduleOnStartup<T> scheduleOnStartup;

  public RecurringTask(String name, Schedule schedule, Class<T> dataClass) {
    this(
        name,
        schedule,
        dataClass,
        new ScheduleOnStartup<>(INSTANCE, null, schedule::getInitialExecutionTime),
        new FailureHandler.OnFailureReschedule<T>(schedule),
        new ReviveDeadExecution<>());
  }

  public RecurringTask(String name, Schedule schedule, Class<T> dataClass, T initialData) {
    this(
        name,
        schedule,
        dataClass,
        new ScheduleOnStartup<>(INSTANCE, initialData, schedule::getInitialExecutionTime),
        new FailureHandler.OnFailureReschedule<T>(schedule),
        new ReviveDeadExecution<>());
  }

  public RecurringTask(
      String name,
      Schedule schedule,
      Class<T> dataClass,
      ScheduleOnStartup<T> scheduleOnStartup,
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
