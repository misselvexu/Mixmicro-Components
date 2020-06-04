package xyz.vopen.mixmicro.components.enhance.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.schedule.core.jdbc.AutodetectJdbcCustomization;
import xyz.vopen.mixmicro.components.enhance.schedule.core.jdbc.JdbcCustomization;
import xyz.vopen.mixmicro.components.enhance.schedule.core.stats.StatsRegistry;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.OnStartup;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.Task;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Optional.ofNullable;
import static xyz.vopen.mixmicro.components.enhance.schedule.core.ExecutorUtils.defaultThreadFactoryWithPrefix;
import static xyz.vopen.mixmicro.components.enhance.schedule.core.Scheduler.THREAD_PREFIX;

public class SchedulerBuilder {
  private static final Logger LOG = LoggerFactory.getLogger(SchedulerBuilder.class);
  private static final int POLLING_CONCURRENCY_MULTIPLIER = 3;

  protected Clock clock = new SystemClock(); // if this is set, waiter-clocks must be updated

  protected final DataSource dataSource;
  protected SchedulerName schedulerName;
  protected int executorThreads = 10;
  protected final List<Task<?>> knownTasks = new ArrayList<>();
  protected final List<OnStartup> startTasks = new ArrayList<>();
  protected Waiter waiter = new Waiter(Duration.ofSeconds(10), clock);
  protected int pollingLimit;
  protected boolean useDefaultPollingLimit;
  protected StatsRegistry statsRegistry = StatsRegistry.NOOP;
  protected Duration heartbeatInterval = Duration.ofMinutes(5);
  protected Serializer serializer = Serializer.DEFAULT_JAVA_SERIALIZER;
  protected String tableName = JdbcTaskRepository.DEFAULT_TABLE_NAME;
  protected boolean enableImmediateExecution = false;
  protected ExecutorService executorService;
  protected Duration deleteUnresolvedAfter = Duration.ofDays(14);
  protected JdbcCustomization jdbcCustomization = null;

  public SchedulerBuilder(DataSource dataSource, List<Task<?>> knownTasks) {
    this.dataSource = dataSource;
    this.knownTasks.addAll(knownTasks);
    this.pollingLimit = calculatePollingLimit();
    this.useDefaultPollingLimit = true;
  }

  @SafeVarargs
  public final <T extends Task<?> & OnStartup> SchedulerBuilder startTasks(T... startTasks) {
    return startTasks(Arrays.asList(startTasks));
  }

  public <T extends Task<?> & OnStartup> SchedulerBuilder startTasks(List<T> startTasks) {
    knownTasks.addAll(startTasks);
    this.startTasks.addAll(startTasks);
    return this;
  }

  public SchedulerBuilder pollingInterval(Duration pollingInterval) {
    waiter = new Waiter(pollingInterval, clock);
    return this;
  }

  public SchedulerBuilder pollingLimit(int pollingLimit) {
    if (pollingLimit <= 0) {
      throw new IllegalArgumentException("pollingLimit must be a positive integer");
    }
    this.pollingLimit = pollingLimit;
    this.useDefaultPollingLimit = false;
    return this;
  }

  private int calculatePollingLimit() {
    return executorThreads * POLLING_CONCURRENCY_MULTIPLIER;
  }

  public SchedulerBuilder heartbeatInterval(Duration duration) {
    this.heartbeatInterval = duration;
    return this;
  }

  public SchedulerBuilder threads(int numberOfThreads) {
    this.executorThreads = numberOfThreads;
    if (useDefaultPollingLimit) {
      this.pollingLimit = calculatePollingLimit();
    }
    return this;
  }

  public SchedulerBuilder executorService(ExecutorService executorService) {
    this.executorService = executorService;
    return this;
  }

  public SchedulerBuilder statsRegistry(StatsRegistry statsRegistry) {
    this.statsRegistry = statsRegistry;
    return this;
  }

  public SchedulerBuilder schedulerName(SchedulerName schedulerName) {
    this.schedulerName = schedulerName;
    return this;
  }

  public SchedulerBuilder serializer(Serializer serializer) {
    this.serializer = serializer;
    return this;
  }

  public SchedulerBuilder tableName(String tableName) {
    this.tableName = tableName;
    return this;
  }

  public SchedulerBuilder enableImmediateExecution() {
    this.enableImmediateExecution = true;
    return this;
  }

  public SchedulerBuilder deleteUnresolvedAfter(Duration deleteAfter) {
    this.deleteUnresolvedAfter = deleteAfter;
    return this;
  }

  public SchedulerBuilder jdbcCustomization(JdbcCustomization jdbcCustomization) {
    this.jdbcCustomization = jdbcCustomization;
    return this;
  }

  public Scheduler build() {
    if (pollingLimit < executorThreads) {
      LOG.warn("Polling-limit is less than number of threads. Should be equal or higher.");
    }

    if (schedulerName == null) {
      schedulerName = new SchedulerName.Hostname();
    }

    final TaskResolver taskResolver = new TaskResolver(statsRegistry, clock, knownTasks);
    final JdbcCustomization jdbcCustomization =
        ofNullable(this.jdbcCustomization).orElse(new AutodetectJdbcCustomization(dataSource));
    final JdbcTaskRepository taskRepository =
        new JdbcTaskRepository(
            dataSource, jdbcCustomization, tableName, taskResolver, schedulerName, serializer);

    ExecutorService candidateExecutorService = executorService;
    if (candidateExecutorService == null) {
      candidateExecutorService =
          Executors.newFixedThreadPool(
              executorThreads, defaultThreadFactoryWithPrefix(THREAD_PREFIX + "-"));
    }

    LOG.info(
        "Creating Scheduler with configuration: threads={}, pollInterval={}s, heartbeat={}s enable-immediate-execution={}, table-name={}, name={}",
        executorThreads,
        waiter.getWaitDuration().getSeconds(),
        heartbeatInterval.getSeconds(),
        enableImmediateExecution,
        tableName,
        schedulerName.getName());
    return new Scheduler(
        clock,
        taskRepository,
        taskResolver,
        executorThreads,
        candidateExecutorService,
        schedulerName,
        waiter,
        heartbeatInterval,
        enableImmediateExecution,
        statsRegistry,
        pollingLimit,
        deleteUnresolvedAfter,
        startTasks);
  }
}
