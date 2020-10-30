package xyz.vopen.mixmicro.components.enhance.schedule.core;

import java.time.Instant;

public class SystemClock implements Clock {

  @Override
  public Instant now() {
    return Instant.now();
  }
}
