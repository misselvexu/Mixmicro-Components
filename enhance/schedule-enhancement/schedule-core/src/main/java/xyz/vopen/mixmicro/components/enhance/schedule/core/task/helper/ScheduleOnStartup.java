package xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper;

import xyz.vopen.mixmicro.components.enhance.schedule.core.Clock;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.Task;

import java.time.Instant;
import java.util.function.Function;

class ScheduleOnStartup<T> {
  String instance;
  T data;
  Function<Instant, Instant> firstExecutionTime;

  ScheduleOnStartup(String instance) {
    this(instance, null);
  }

  ScheduleOnStartup(String instance, T data) {
    this(instance, data, Function.identity());
  }

  ScheduleOnStartup(String instance, T data, Function<Instant, Instant> firstExecutionTime) {
    this.firstExecutionTime = firstExecutionTime;
    this.instance = instance;
    this.data = data;
  }

  public void apply(Scheduler scheduler, Clock clock, Task<T> task) {
    if (data == null) {
      scheduler.schedule(task.instance(instance), firstExecutionTime.apply(clock.now()));
    } else {
      scheduler.schedule(task.instance(instance, data), firstExecutionTime.apply(clock.now()));
    }
  }
}
