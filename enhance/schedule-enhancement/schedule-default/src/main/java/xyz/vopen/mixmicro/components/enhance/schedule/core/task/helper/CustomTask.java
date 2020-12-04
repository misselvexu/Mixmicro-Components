package xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper;

import xyz.vopen.mixmicro.components.enhance.schedule.core.Clock;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.DeadExecutionHandler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.FailureHandler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.OnStartup;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.Task;

public abstract class CustomTask<T> extends Task<T> implements OnStartup {

  private final ScheduleOnStartup<T> scheduleOnStartup;

  public CustomTask(
      String name,
      Class<T> dataClass,
      ScheduleOnStartup<T> scheduleOnStartup,
      FailureHandler<T> failureHandler,
      DeadExecutionHandler<T> deadExecutionHandler) {
    super(name, dataClass, failureHandler, deadExecutionHandler);
    this.scheduleOnStartup = scheduleOnStartup;
  }

  @Override
  public void onStartup(Scheduler scheduler, Clock clock) {
    if (scheduleOnStartup != null) {
      scheduleOnStartup.apply(scheduler, clock, this);
    }
  }
}
