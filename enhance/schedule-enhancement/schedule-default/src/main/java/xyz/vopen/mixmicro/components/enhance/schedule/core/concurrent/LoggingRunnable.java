package xyz.vopen.mixmicro.components.enhance.schedule.core.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoggingRunnable implements Runnable {
  private final Logger log = LoggerFactory.getLogger(getClass());

  public abstract void runButLogExceptions();

  @Override
  public void run() {
    try {
      runButLogExceptions();
    } catch (Exception e) {
      log.error("Unexcepted exception when executing Runnable", e);
    }
  }
}
