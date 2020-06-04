package xyz.vopen.mixmicro.components.enhance.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public class Waiter {
  private static final Logger LOG = LoggerFactory.getLogger(Waiter.class);

  private Object lock;
  private boolean woken = false;
  private final Duration duration;
  private Clock clock;
  private boolean isWaiting = false;

  public Waiter(Duration duration) {
    this(duration, new SystemClock());
  }

  public Waiter(Duration duration, Clock clock) {
    this(duration, clock, new Object());
  }

  Waiter(Duration duration, Clock clock, Object lock) {
    this.duration = duration;
    this.clock = clock;
    this.lock = lock;
  }

  public void doWait() throws InterruptedException {
    final long millis = duration.toMillis();
    if (millis > 0) {
      Instant waitUntil = clock.now().plusMillis(millis);

      while (clock.now().isBefore(waitUntil)) {
        synchronized (lock) {
          woken = false;
          LOG.debug("Waiter start wait.");
          this.isWaiting = true;
          lock.wait(millis);
          this.isWaiting = false;
          if (woken) {
            LOG.debug(
                "Waiter woken, it had {}ms left to wait.",
                (waitUntil.toEpochMilli() - clock.now().toEpochMilli()));
            break;
          }
        }
      }
    }
  }

  public boolean wake() {
    synchronized (lock) {
      if (!isWaiting) {
        return false;
      } else {
        woken = true;
        lock.notify();
        return true;
      }
    }
  }

  public Duration getWaitDuration() {
    return duration;
  }
}
