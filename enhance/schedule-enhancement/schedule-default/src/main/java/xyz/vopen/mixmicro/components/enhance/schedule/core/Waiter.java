package xyz.vopen.mixmicro.components.enhance.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public class Waiter {
  private static final Logger log = LoggerFactory.getLogger(Waiter.class);

  private final Object lock;
  private boolean woken = false;
  private final Duration duration;
  private Clock clock;
  private boolean isWaiting = false;
  private boolean skipNextWait = false;

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
    long millis = duration.toMillis();

    if (millis > 0) {
      Instant waitUntil = clock.now().plusMillis(millis);

      while (clock.now().isBefore(waitUntil)) {
        synchronized (lock) {
          if (skipNextWait) {
            log.debug("Waiter has been notified to skip next wait-period. Skipping wait.");
            skipNextWait = false;
            return;
          }

          woken = false;
          log.debug("Waiter start wait.");
          this.isWaiting = true;
          lock.wait(millis);
          this.isWaiting = false;
          if (woken) {
            log.debug(
                "Waiter woken, it had {}ms left to wait.",
                (waitUntil.toEpochMilli() - clock.now().toEpochMilli()));
            return;
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

  public void wakeOrSkipNextWait() {
    // Take early lock to avoid race-conditions. Lock is also taken in wake() (lock is re-entrant)
    synchronized (lock) {
      final boolean awoken = wake();
      if (!awoken) {
        log.debug("Waiter not waiting, instructing to skip next wait.");
        this.skipNextWait = true;
      }
    }
  }

  public Duration getWaitDuration() {
    return duration;
  }
}
