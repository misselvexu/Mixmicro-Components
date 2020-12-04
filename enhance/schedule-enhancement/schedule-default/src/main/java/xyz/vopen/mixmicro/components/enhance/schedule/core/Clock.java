package xyz.vopen.mixmicro.components.enhance.schedule.core;

import java.time.Instant;

public interface Clock {
  Instant now();
}
