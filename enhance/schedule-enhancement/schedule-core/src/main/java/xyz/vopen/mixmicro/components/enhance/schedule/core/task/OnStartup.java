package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

import xyz.vopen.mixmicro.components.enhance.schedule.core.Clock;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;

public interface OnStartup {

  void onStartup(Scheduler scheduler, Clock clock);
}
