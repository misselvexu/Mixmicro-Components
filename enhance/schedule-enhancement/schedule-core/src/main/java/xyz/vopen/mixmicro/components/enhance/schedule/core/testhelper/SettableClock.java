package xyz.vopen.mixmicro.components.enhance.schedule.core.testhelper;

import xyz.vopen.mixmicro.components.enhance.schedule.core.Clock;

import java.time.Instant;

public class SettableClock implements Clock {

  public Instant now = Instant.now();

  @Override
  public Instant now() {
    return now;
  }

  public void set(Instant newNow) {
    this.now = newNow;
  }
}
