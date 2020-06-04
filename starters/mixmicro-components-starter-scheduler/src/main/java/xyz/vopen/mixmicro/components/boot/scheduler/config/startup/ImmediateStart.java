package xyz.vopen.mixmicro.components.boot.scheduler.config.startup;

import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;

import javax.annotation.PostConstruct;

/**
 * {@link ImmediateStart}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/4
 */
public class ImmediateStart extends AbstractSchedulerStarter {
  public ImmediateStart(final Scheduler scheduler) {
    super(scheduler);
  }

  @PostConstruct
  void startImmediately() {
    doStart();
  }
}
