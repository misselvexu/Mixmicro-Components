package xyz.vopen.mixmicro.components.boot.scheduler;

import xyz.vopen.mixmicro.components.enhance.schedule.core.SchedulerName;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Serializer;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

/**
 * Provides functionality for customizing various aspects of the mixmicro-scheduler configuration
 * that is not easily done with properties.
 */
public interface MixmicroSchedulerCustomizer {
  /** Provide a custom {@link SchedulerName} implementation. */
  default Optional<SchedulerName> schedulerName() {
    return Optional.empty();
  }

  /** A custom serializer for task data. */
  default Optional<Serializer> serializer() {
    return Optional.empty();
  }

  /** Provide an existing {@link ExecutorService} instance. */
  default Optional<ExecutorService> executorService() {
    return Optional.empty();
  }
}
