package xyz.vopen.mixmicro.components.enhance.schedule.core.task.helper;

import xyz.vopen.mixmicro.components.enhance.schedule.core.Clock;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.Task;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.TaskInstance;

import java.time.Instant;
import java.util.function.Function;

class ScheduleOnceOnStartup<T> implements ScheduleOnStartup<T> {
  private final String instance;
  private final T data;
  private final Function<Instant, Instant> firstExecutionTime;

  ScheduleOnceOnStartup(String instance) {
    this(instance, null);
  }

  ScheduleOnceOnStartup(String instance, T data) {
    this(instance, data, Function.identity());
  }

  ScheduleOnceOnStartup(String instance, T data, Function<Instant, Instant> firstExecutionTime) {
    this.firstExecutionTime = firstExecutionTime;
    this.instance = instance;
    this.data = data;
  }

  public void apply(Scheduler scheduler, Clock clock, Task<T> task) {
    scheduler.schedule(getSchedulableInstance(task), firstExecutionTime.apply(clock.now()));
  }

  private TaskInstance<T> getSchedulableInstance(Task<T> task) {
    if (data == null) {
      return task.instance(instance);
    } else {
      return task.instance(instance, data);
    }
  }
}
