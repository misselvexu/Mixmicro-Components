package xyz.vopen.mixmicro.components.boot.scheduler.autoconfigure;

import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.autoconfigure.health.HealthContributorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import xyz.vopen.mixmicro.components.boot.scheduler.MixmicroSchedulerCustomizer;
import xyz.vopen.mixmicro.components.boot.scheduler.MixmicroSchedulerLifecycle;
import xyz.vopen.mixmicro.components.boot.scheduler.MixmicroSchedulerProperties;
import xyz.vopen.mixmicro.components.boot.scheduler.actuator.MixmicroSchedulerHealthIndicator;
import xyz.vopen.mixmicro.components.boot.scheduler.listener.ApplicationContextReadyListener;
import xyz.vopen.mixmicro.components.boot.scheduler.listener.BeanPostInitializedListener;
import xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler;
import xyz.vopen.mixmicro.components.enhance.schedule.core.SchedulerBuilder;
import xyz.vopen.mixmicro.components.enhance.schedule.core.SchedulerName;
import xyz.vopen.mixmicro.components.enhance.schedule.core.stats.MicrometerStatsRegistry;
import xyz.vopen.mixmicro.components.enhance.schedule.core.stats.StatsRegistry;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.OnStartup;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.Task;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(MixmicroSchedulerProperties.class)
@AutoConfigurationPackage
@AutoConfigureAfter({
  DataSourceAutoConfiguration.class,
  HealthContributorAutoConfiguration.class,
  MetricsAutoConfiguration.class,
  CompositeMeterRegistryAutoConfiguration.class,
})
@ConditionalOnBean(DataSource.class)
@ConditionalOnProperty(value = "mixmicro.scheduler.enabled", matchIfMissing = true)
public class MixmicroSchedulerAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(MixmicroSchedulerAutoConfiguration.class);

  private static final Predicate<Task<?>> shouldBeStarted = task -> task instanceof OnStartup;

  private final MixmicroSchedulerProperties config;
  private final DataSource existingDataSource;
  private final List<Task<?>> configuredTasks;

  public MixmicroSchedulerAutoConfiguration(
      MixmicroSchedulerProperties schedulerProperties,
      DataSource dataSource,
      List<Task<?>> configuredTasks) {

    this.config =
        Objects.requireNonNull(
            schedulerProperties,
            "Can't configure mixmicro-scheduler without required configuration");
    this.existingDataSource =
        Objects.requireNonNull(dataSource, "An existing javax.sql.DataSource is required");
    this.configuredTasks =
        Objects.requireNonNull(configuredTasks, "At least one Task must be configured");
  }

  /** Provide an empty customizer if not present in the context. */
  @ConditionalOnMissingBean
  @Bean
  public MixmicroSchedulerCustomizer noopCustomizer() {
    return new MixmicroSchedulerCustomizer() {};
  }

  @ConditionalOnClass(MeterRegistry.class)
  @ConditionalOnBean(MeterRegistry.class)
  @ConditionalOnMissingBean(StatsRegistry.class)
  @Bean
  StatsRegistry micrometerStatsRegistry(MeterRegistry registry) {
    log.debug(
        "Missing StatsRegistry bean in context but Micrometer detected. Will use: {}",
        registry.getClass().getName());
    return new MicrometerStatsRegistry(registry, configuredTasks);
  }

  @ConditionalOnMissingBean(StatsRegistry.class)
  @Bean
  StatsRegistry noopStatsRegistry() {
    log.debug("Missing StatsRegistry bean in context, creating a no-op StatsRegistry");
    return StatsRegistry.NOOP;
  }

  @ConditionalOnBean(DataSource.class)
  @ConditionalOnMissingBean
  @Bean(destroyMethod = "stop")
  public Scheduler scheduler(MixmicroSchedulerCustomizer customizer, StatsRegistry registry) {
    log.info("Creating mixmicro-scheduler using tasks from Spring context: {}", configuredTasks);

    // Ensure that we are using a transactional aware data source
    DataSource transactionalDataSource = configureDataSource(existingDataSource);

    // Instantiate a new builder
    final SchedulerBuilder builder =
        Scheduler.create(transactionalDataSource, nonStartupTasks(configuredTasks));

    builder.threads(config.getThreads());

    // Polling
    builder.pollingInterval(config.getPollingInterval());
    config.getPollingLimit().ifPresent(builder::pollingLimit);

    builder.heartbeatInterval(config.getHeartbeatInterval());

    // Use scheduler name implementation from customizer if available, otherwise use
    // configured scheduler name (String). If both is absent, use the library default
    if (customizer.schedulerName().isPresent()) {
      builder.schedulerName(customizer.schedulerName().get());
    } else if (config.getSchedulerName() != null) {
      builder.schedulerName(new SchedulerName.Fixed(config.getSchedulerName()));
    }

    builder.tableName(config.getTableName());

    // Use custom serializer if provided
    customizer.serializer().ifPresent(builder::serializer);

    if (config.isImmediateExecutionEnabled()) {
      builder.enableImmediateExecution();
    }

    // Use custom executor service if provided
    customizer.executorService().ifPresent(builder::executorService);

    builder.deleteUnresolvedAfter(config.getDeleteUnresolvedAfter());

    // Add recurring jobs and jobs that implements OnStartup
    builder.startTasks(startupTasks(configuredTasks));

    // Expose metrics
    builder.statsRegistry(registry);

    return builder.build();
  }

  @ConditionalOnEnabledHealthIndicator("mixmicro-scheduler")
  @ConditionalOnClass(HealthIndicator.class)
  @ConditionalOnBean(Scheduler.class)
  @Bean
  public HealthIndicator dbScheduler(Scheduler scheduler) {
    return new MixmicroSchedulerHealthIndicator(scheduler);
  }

  @ConditionalOnBean(Scheduler.class)
  @ConditionalOnMissingBean
  @Bean
  public MixmicroSchedulerLifecycle dbSchedulerStarter(Scheduler scheduler) {
    if (config.isDelayStartupUntilContextReady()) {
      return new ApplicationContextReadyListener(scheduler);
    }

    return new BeanPostInitializedListener(scheduler);
  }

  private static DataSource configureDataSource(DataSource existingDataSource) {
    if (existingDataSource instanceof TransactionAwareDataSourceProxy) {
      log.debug("Using an already transaction aware DataSource");
      return existingDataSource;
    }

    log.debug(
        "The configured DataSource is not transaction aware: '{}'. Wrapping in TransactionAwareDataSourceProxy.",
        existingDataSource);

    return new TransactionAwareDataSourceProxy(existingDataSource);
  }

  @SuppressWarnings("unchecked")
  private static <T extends Task<?> & OnStartup> List<T> startupTasks(List<Task<?>> tasks) {
    return tasks.stream()
        .filter(shouldBeStarted)
        .map(task -> (T) task)
        .collect(Collectors.toList());
  }

  private static List<Task<?>> nonStartupTasks(List<Task<?>> tasks) {
    return tasks.stream().filter(shouldBeStarted.negate()).collect(Collectors.toList());
  }
}
