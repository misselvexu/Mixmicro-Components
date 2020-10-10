package xyz.vopen.mixmicro.components.boot.scheduler.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.SchedulerState;

import java.util.Objects;

public class MixmicroSchedulerHealthIndicator implements HealthIndicator {

  private final SchedulerState state;

  public MixmicroSchedulerHealthIndicator(Scheduler scheduler) {
    this.state = Objects.requireNonNull(scheduler).getSchedulerState();
  }

  @Override
  public Health health() {
    if (state.isStarted() && !state.isShuttingDown()) {
      return Health.up().withDetail("state", "started").build();
    } else if (state.isStarted() && state.isShuttingDown()) {
      return Health.outOfService().withDetail("state", "shutting_down").build();
    } else if (!state.isStarted() && !state.isShuttingDown()) {
      return Health.down().withDetail("state", "not_started").build();
    } else {
      return Health.down().build();
    }
  }
}
