package xyz.vopen.mixmicro.components.boot.scheduler.config;

import xyz.vopen.mixmicro.components.enhance.schedule.core.SchedulerName;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Serializer;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

/**
 * Provides functionality for customizing various aspects of the db-scheduler configuration that is
 * not easily done with properties.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/4
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
