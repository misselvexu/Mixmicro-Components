package xyz.vopen.mixmicro.kits.timer;

import java.util.concurrent.TimeUnit;

/**
 * A task which is executed after the delay specified with {@link Timer#newTimeout(TimerTask, long,
 * TimeUnit)}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface TimerTask {

  /**
   * Executed after the delay specified with {@link Timer#newTimeout(TimerTask, long, TimeUnit)}.
   *
   * @param timeout a handle which is associated with this task
   */
  void run(Timeout timeout) throws Exception;
}
