package xyz.vopen.mixmicro.components.boot.scheduler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.validation.annotation.Validated;
import xyz.vopen.mixmicro.components.enhance.schedule.core.JdbcTaskRepository;
import xyz.vopen.mixmicro.components.enhance.schedule.core.SchedulerBuilder;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.*;

@Validated
@ConfigurationProperties("mixmicro.scheduler")
public class MixmicroSchedulerProperties {

  /** Whether to enable auto configuration of the mixmicro-scheduler. */
  private boolean enabled = true;

  /***
   * <p>Number of threads.
   */
  private int threads = 10;

  /** How often to update the heartbeat timestamp for running executions. */
  @DurationUnit(MINUTES)
  @NotNull
  private Duration heartbeatInterval = SchedulerBuilder.DEFAULT_HEARTBEAT_INTERVAL;

  /**
   * Name of this scheduler-instance. The name is stored in the database when an execution is picked
   * by a scheduler.
   *
   * <p>If the name is {@code null} or not configured, the hostname of the running machine will be
   * used.
   */
  private String schedulerName;

  /**
   * Name of the table used to track task-executions. Must match the database. Change name in the
   * table definitions accordingly when creating or modifying the table.
   */
  @NotNull private String tableName = JdbcTaskRepository.DEFAULT_TABLE_NAME;

  /**
   * If this is enabled, the scheduler will attempt to directly execute tasks that are scheduled to
   * {@code now()}, or a time in the past. For this to work, the call to {@code schedule(..)} must
   * not occur from within a transaction, because the record will not yet be visible to the
   * scheduler (if this is a requirement, see the method {@code
   * scheduler.triggerCheckForDueExecutions())}
   */
  private boolean immediateExecutionEnabled = false;

  /** How often the scheduler checks the database for due executions. */
  @DurationUnit(SECONDS)
  @NotNull
  private Duration pollingInterval = SchedulerBuilder.DEFAULT_POLLING_INTERVAL;

  /** Maximum number of executions to fetch on a check for due executions. */
  private Optional<Integer> pollingLimit = Optional.empty();

  /**
   * Whether to start the scheduler when the application context has been loaded or as soon as
   * possible.
   */
  private boolean delayStartupUntilContextReady = false;

  /** The time after which executions with unknown tasks are automatically deleted. */
  @DurationUnit(HOURS)
  @NotNull
  private Duration deleteUnresolvedAfter =
      SchedulerBuilder.DEFAULT_DELETION_OF_UNRESOLVED_TASKS_DURATION;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(final boolean enabled) {
    this.enabled = enabled;
  }

  public int getThreads() {
    return threads;
  }

  public void setThreads(final int threads) {
    this.threads = threads;
  }

  public Duration getHeartbeatInterval() {
    return heartbeatInterval;
  }

  public void setHeartbeatInterval(final Duration heartbeatInterval) {
    this.heartbeatInterval = heartbeatInterval;
  }

  public String getSchedulerName() {
    return schedulerName;
  }

  public void setSchedulerName(String schedulerName) {
    this.schedulerName = schedulerName;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(final String tableName) {
    this.tableName = tableName;
  }

  public boolean isImmediateExecutionEnabled() {
    return immediateExecutionEnabled;
  }

  public void setImmediateExecutionEnabled(boolean immediateExecutionEnabled) {
    this.immediateExecutionEnabled = immediateExecutionEnabled;
  }

  public Duration getPollingInterval() {
    return pollingInterval;
  }

  public void setPollingInterval(final Duration pollingInterval) {
    this.pollingInterval = pollingInterval;
  }

  public Optional<Integer> getPollingLimit() {
    return pollingLimit;
  }

  public void setPollingLimit(final Optional<Integer> pollingLimit) {
    this.pollingLimit = pollingLimit;
  }

  public boolean isDelayStartupUntilContextReady() {
    return delayStartupUntilContextReady;
  }

  public void setDelayStartupUntilContextReady(final boolean delayStartupUntilContextReady) {
    this.delayStartupUntilContextReady = delayStartupUntilContextReady;
  }

  public Duration getDeleteUnresolvedAfter() {
    return deleteUnresolvedAfter;
  }

  public void setDeleteUnresolvedAfter(Duration deleteUnresolvedAfter) {
    this.deleteUnresolvedAfter = deleteUnresolvedAfter;
  }
}
