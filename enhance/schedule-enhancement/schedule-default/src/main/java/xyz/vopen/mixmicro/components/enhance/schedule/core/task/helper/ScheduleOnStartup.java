package xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper;

import xyz.vopen.mixmicro.components.enhance.schedule.core.Clock;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.Task;

public interface ScheduleOnStartup<T> {
  void apply(Scheduler scheduler, Clock clock, Task<T> task);
}
