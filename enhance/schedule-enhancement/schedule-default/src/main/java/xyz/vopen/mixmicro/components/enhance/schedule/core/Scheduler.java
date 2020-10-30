package xyz.vopen.mixmicro.components.enhance.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.schedule.core.SchedulerState.SettableSchedulerState;
import xyz.vopen.mixmicro.components.enhance.schedule.core.concurrent.LoggingRunnable;
import xyz.vopen.mixmicro.components.enhance.schedule.core.stats.StatsRegistry;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.*;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Scheduler implements SchedulerClient {

  public static final double TRIGGER_NEXT_BATCH_WHEN_AVAILABLE_THREADS_RATIO = 0.5;
  public static final String THREAD_PREFIX = "mixmicro-scheduler";
  public static final Duration SHUTDOWN_WAIT = Duration.ofMinutes(30);
  private static final Logger log = LoggerFactory.getLogger(Scheduler.class);
  private final SchedulerClient delegate;
  private final Clock clock;
  private final TaskRepository taskRepository;
  private final TaskResolver taskResolver;
  private final int threadpoolSize;
  private final ExecutorService executorService;
  private final Waiter executeDueWaiter;
  private final Duration deleteUnresolvedAfter;
  protected final List<OnStartup> onStartup;
  private final Waiter detectDeadWaiter;
  private final Duration heartbeatInterval;
  private final StatsRegistry statsRegistry;
  private final int pollingLimit;
  private final ExecutorService dueExecutor;
  private final ExecutorService detectDeadExecutor;
  private final ExecutorService updateHeartbeatExecutor;
  private final Map<Execution, CurrentlyExecuting> currentlyProcessing =
      Collections.synchronizedMap(new HashMap<>());
  private final Waiter heartbeatWaiter;
  private final SettableSchedulerState schedulerState = new SettableSchedulerState();
  private int currentGenerationNumber = 1;

  protected Scheduler(
      Clock clock,
      TaskRepository taskRepository,
      TaskResolver taskResolver,
      int threadpoolSize,
      ExecutorService executorService,
      SchedulerName schedulerName,
      Waiter executeDueWaiter,
      Duration heartbeatInterval,
      boolean enableImmediateExecution,
      StatsRegistry statsRegistry,
      int pollingLimit,
      Duration deleteUnresolvedAfter,
      List<OnStartup> onStartup) {
    this.clock = clock;
    this.taskRepository = taskRepository;
    this.taskResolver = taskResolver;
    this.threadpoolSize = threadpoolSize;
    this.executorService = executorService;
    this.executeDueWaiter = executeDueWaiter;
    this.deleteUnresolvedAfter = deleteUnresolvedAfter;
    this.onStartup = onStartup;
    this.detectDeadWaiter = new Waiter(heartbeatInterval.multipliedBy(2), clock);
    this.heartbeatInterval = heartbeatInterval;
    this.heartbeatWaiter = new Waiter(heartbeatInterval, clock);
    this.statsRegistry = statsRegistry;
    this.pollingLimit = pollingLimit;
    this.dueExecutor =
        Executors.newSingleThreadExecutor(
            ExecutorUtils.defaultThreadFactoryWithPrefix(THREAD_PREFIX + "-execute-due-"));
    this.detectDeadExecutor =
        Executors.newSingleThreadExecutor(
            ExecutorUtils.defaultThreadFactoryWithPrefix(THREAD_PREFIX + "-detect-dead-"));
    this.updateHeartbeatExecutor =
        Executors.newSingleThreadExecutor(
            ExecutorUtils.defaultThreadFactoryWithPrefix(THREAD_PREFIX + "-update-heartbeat-"));
    SchedulerClientEventListener earlyExecutionListener =
        (enableImmediateExecution
            ? new TriggerCheckForDueExecutions(schedulerState, clock, executeDueWaiter)
            : SchedulerClientEventListener.NOOP);
    delegate = new StandardSchedulerClient(taskRepository, earlyExecutionListener);
  }

  public void start() {
    log.info("Starting scheduler.");

    executeOnStartup();

    dueExecutor.submit(
        new RunUntilShutdown(this::executeDue, executeDueWaiter, schedulerState, statsRegistry));
    detectDeadExecutor.submit(
        new RunUntilShutdown(
            this::detectDeadExecutions, detectDeadWaiter, schedulerState, statsRegistry));
    updateHeartbeatExecutor.submit(
        new RunUntilShutdown(
            this::updateHeartbeats, heartbeatWaiter, schedulerState, statsRegistry));

    schedulerState.setStarted();
  }

  protected void executeOnStartup() {
    onStartup.forEach(
        os -> {
          try {
            os.onStartup(this, this.clock);
          } catch (Exception e) {
            log.error("Unexpected error while executing OnStartup tasks. Continuing.", e);
            statsRegistry.register(StatsRegistry.SchedulerStatsEvent.UNEXPECTED_ERROR);
          }
        });
  }

  public void stop() {
    stop(Duration.ofSeconds(1), Duration.ofSeconds(5));
  }

  void stop(Duration utilExecutorsWaitBeforeInterrupt, Duration utilExecutorsWaitAfterInterrupt) {
    if (schedulerState.isShuttingDown()) {
      log.warn("Multiple calls to 'stop()'. Scheduler is already stopping.");
      return;
    }

    schedulerState.setIsShuttingDown();

    log.info("Shutting down Scheduler.");
    if (!ExecutorUtils.shutdownAndAwaitTermination(
        dueExecutor, utilExecutorsWaitBeforeInterrupt, utilExecutorsWaitAfterInterrupt)) {
      log.warn("Failed to shutdown due-executor properly.");
    }
    if (!ExecutorUtils.shutdownAndAwaitTermination(
        detectDeadExecutor, utilExecutorsWaitBeforeInterrupt, utilExecutorsWaitAfterInterrupt)) {
      log.warn("Failed to shutdown detect-dead-executor properly.");
    }
    if (!ExecutorUtils.shutdownAndAwaitTermination(
        updateHeartbeatExecutor,
        utilExecutorsWaitBeforeInterrupt,
        utilExecutorsWaitAfterInterrupt)) {
      log.warn("Failed to shutdown update-heartbeat-executor properly.");
    }

    log.info("Letting running executions finish. Will wait up to 2x{}.", SHUTDOWN_WAIT);
    final Instant startShutdown = clock.now();
    if (ExecutorUtils.shutdownAndAwaitTermination(executorService, SHUTDOWN_WAIT, SHUTDOWN_WAIT)) {
      log.info("Scheduler stopped.");
    } else {
      log.warn(
          "Scheduler stopped, but some tasks did not complete. Was currently running the following executions:\n{}",
          new ArrayList<>(currentlyProcessing.keySet())
              .stream().map(Execution::toString).collect(Collectors.joining("\n")));
    }

    final Duration shutdownTime = Duration.between(startShutdown, clock.now());
    if (shutdownTime.toMillis() >= SHUTDOWN_WAIT.toMillis()) {
      log.info(
          "Shutdown of the scheduler executor service took {}. Consider regularly checking for "
              + "'executionContext.getSchedulerState().isShuttingDown()' in task execution-handler and abort when "
              + "scheduler is shutting down.",
          shutdownTime);
    }
  }

  public SchedulerState getSchedulerState() {
    return schedulerState;
  }

  @Override
  public <T> void schedule(TaskInstance<T> taskInstance, Instant executionTime) {
    this.delegate.schedule(taskInstance, executionTime);
  }

  @Override
  public void reschedule(TaskInstanceId taskInstanceId, Instant newExecutionTime) {
    this.delegate.reschedule(taskInstanceId, newExecutionTime);
  }

  @Override
  public <T> void reschedule(TaskInstanceId taskInstanceId, Instant newExecutionTime, T newData) {
    this.delegate.reschedule(taskInstanceId, newExecutionTime, newData);
  }

  @Override
  public void cancel(TaskInstanceId taskInstanceId) {
    this.delegate.cancel(taskInstanceId);
  }

  @Override
  public void getScheduledExecutions(Consumer<ScheduledExecution<Object>> consumer) {
    this.delegate.getScheduledExecutions(consumer);
  }

  @Override
  public <T> void getScheduledExecutionsForTask(
      String taskName, Class<T> dataClass, Consumer<ScheduledExecution<T>> consumer) {
    this.delegate.getScheduledExecutionsForTask(taskName, dataClass, consumer);
  }

  @Override
  public Optional<ScheduledExecution<Object>> getScheduledExecution(TaskInstanceId taskInstanceId) {
    return this.delegate.getScheduledExecution(taskInstanceId);
  }

  public List<Execution> getFailingExecutions(Duration failingAtLeastFor) {
    return taskRepository.getExecutionsFailingLongerThan(failingAtLeastFor);
  }

  public void triggerCheckForDueExecutions() {
    executeDueWaiter.wakeOrSkipNextWait();
  }

  public List<CurrentlyExecuting> getCurrentlyExecuting() {
    return new ArrayList<>(currentlyProcessing.values());
  }

  protected void executeDue() {
    Instant now = clock.now();
    List<Execution> dueExecutions = taskRepository.getDue(now, pollingLimit);
    log.trace("Found {} taskinstances due for execution", dueExecutions.size());

    this.currentGenerationNumber = this.currentGenerationNumber + 1;
    DueExecutionsBatch newDueBatch =
        new DueExecutionsBatch(
            Scheduler.this.threadpoolSize,
            currentGenerationNumber,
            dueExecutions.size(),
            pollingLimit == dueExecutions.size());

    for (Execution e : dueExecutions) {
      executorService.execute(new PickAndExecute(e, newDueBatch));
    }
    statsRegistry.register(StatsRegistry.SchedulerStatsEvent.RAN_EXECUTE_DUE);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void detectDeadExecutions() {
    log.debug("Deleting executions with unresolved tasks.");
    taskResolver
        .getUnresolvedTaskNames(deleteUnresolvedAfter)
        .forEach(
            taskName -> {
              log.warn(
                  "Deleting all executions for task with name '{}'. They have been unresolved for more than {}",
                  taskName,
                  deleteUnresolvedAfter);
              int removed = taskRepository.removeExecutions(taskName);
              log.info("Removed {} executions", removed);
              taskResolver.clearUnresolved(taskName);
            });

    log.debug("Checking for dead executions.");
    Instant now = clock.now();
    final Instant oldAgeLimit = now.minus(getMaxAgeBeforeConsideredDead());
    List<Execution> oldExecutions = taskRepository.getDeadExecutions(oldAgeLimit);

    if (!oldExecutions.isEmpty()) {
      oldExecutions.forEach(
          execution -> {
            log.info("Found dead execution. Delegating handling to task. Execution: " + execution);
            try {

              Optional<Task> task = taskResolver.resolve(execution.taskInstance.getTaskName());
              if (task.isPresent()) {
                statsRegistry.register(StatsRegistry.SchedulerStatsEvent.DEAD_EXECUTION);
                task.get()
                    .getDeadExecutionHandler()
                    .deadExecution(execution, new ExecutionOperations(taskRepository, execution));
              } else {
                log.error(
                    "Failed to find implementation for task with name '{}' for detected dead execution. Either delete the execution from the databaser, or add an implementation for it.",
                    execution.taskInstance.getTaskName());
              }

            } catch (Throwable e) {
              log.error(
                  "Failed while handling dead execution {}. Will be tried again later.",
                  execution,
                  e);
              statsRegistry.register(StatsRegistry.SchedulerStatsEvent.UNEXPECTED_ERROR);
            }
          });
    } else {
      log.trace("No dead executions found.");
    }
    statsRegistry.register(StatsRegistry.SchedulerStatsEvent.RAN_DETECT_DEAD);
  }

  void updateHeartbeats() {
    if (currentlyProcessing.isEmpty()) {
      log.trace("No executions to update heartbeats for. Skipping.");
      return;
    }

    log.debug("Updating heartbeats for {} executions being processed.", currentlyProcessing.size());
    Instant now = clock.now();
    new ArrayList<>(currentlyProcessing.keySet())
        .forEach(
            execution -> {
              log.trace("Updating heartbeat for execution: " + execution);
              try {
                taskRepository.updateHeartbeat(execution, now);
              } catch (Throwable e) {
                log.error(
                    "Failed while updating heartbeat for execution {}. Will try again later.",
                    execution,
                    e);
                statsRegistry.register(StatsRegistry.SchedulerStatsEvent.UNEXPECTED_ERROR);
              }
            });
    statsRegistry.register(StatsRegistry.SchedulerStatsEvent.RAN_UPDATE_HEARTBEATS);
  }

  private Duration getMaxAgeBeforeConsideredDead() {
    return heartbeatInterval.multipliedBy(4);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private class PickAndExecute extends LoggingRunnable {
    private final Execution candidate;
    private final DueExecutionsBatch addedDueExecutionsBatch;

    public PickAndExecute(Execution candidate, DueExecutionsBatch dueExecutionsBatch) {
      this.candidate = candidate;
      this.addedDueExecutionsBatch = dueExecutionsBatch;
    }

    @Override
    public void runButLogExceptions() {
      if (schedulerState.isShuttingDown()) {
        log.info(
            "Scheduler has been shutdown. Skipping fetched due execution: "
                + candidate.taskInstance.getTaskAndInstance());
        return;
      }

      try {
        if (addedDueExecutionsBatch.isOlderGenerationThan(currentGenerationNumber)) {
          // skipping execution due to it being stale
          addedDueExecutionsBatch.markBatchAsStale();
          statsRegistry.register(StatsRegistry.CandidateStatsEvent.STALE);
          log.trace(
              "Skipping queued execution (current generationNumber: {}, execution generationNumber: {})",
              currentGenerationNumber,
              addedDueExecutionsBatch.getGenerationNumber());
          return;
        }

        final Optional<Execution> pickedExecution = taskRepository.pick(candidate, clock.now());

        if (!pickedExecution.isPresent()) {
          // someone else picked id
          log.debug("Execution picked by another scheduler. Continuing to next due execution.");
          statsRegistry.register(StatsRegistry.CandidateStatsEvent.ALREADY_PICKED);
          return;
        }

        currentlyProcessing.put(
            pickedExecution.get(), new CurrentlyExecuting(pickedExecution.get(), clock));
        try {
          statsRegistry.register(StatsRegistry.CandidateStatsEvent.EXECUTED);
          executePickedExecution(pickedExecution.get());
        } finally {
          if (currentlyProcessing.remove(pickedExecution.get()) == null) {
            // May happen in rare circumstances (typically concurrency tests)
            log.warn(
                "Released execution was not found in collection of executions currently being processed. Should never happen.");
          }
        }
      } finally {
        // Make sure 'executionsLeftInBatch' is decremented for all executions (run or not run)
        addedDueExecutionsBatch.oneExecutionDone(Scheduler.this::triggerCheckForDueExecutions);
      }
    }

    private void executePickedExecution(Execution execution) {
      final Optional<Task> task = taskResolver.resolve(execution.taskInstance.getTaskName());
      if (!task.isPresent()) {
        log.error(
            "Failed to find implementation for task with name '{}'. Should have been excluded in JdbcRepository.",
            execution.taskInstance.getTaskName());
        statsRegistry.register(StatsRegistry.SchedulerStatsEvent.UNEXPECTED_ERROR);
        return;
      }

      Instant executionStarted = clock.now();
      try {
        log.debug("Executing " + execution);
        CompletionHandler completion =
            task.get()
                .execute(
                    execution.taskInstance,
                    new ExecutionContext(schedulerState, execution, Scheduler.this));
        log.debug("Execution done");

        complete(completion, execution, executionStarted);
        statsRegistry.register(StatsRegistry.ExecutionStatsEvent.COMPLETED);

      } catch (RuntimeException unhandledException) {
        log.error(
            "Unhandled exception during execution of task with name '{}'. Treating as failure.",
            task.get().getName(),
            unhandledException);
        failure(task.get().getFailureHandler(), execution, unhandledException, executionStarted);
        statsRegistry.register(StatsRegistry.ExecutionStatsEvent.FAILED);

      } catch (Throwable unhandledError) {
        log.error(
            "Error during execution of task with name '{}'. Treating as failure.",
            task.get().getName(),
            unhandledError);
        failure(task.get().getFailureHandler(), execution, unhandledError, executionStarted);
        statsRegistry.register(StatsRegistry.ExecutionStatsEvent.FAILED);
      }
    }

    private void complete(
        CompletionHandler completion, Execution execution, Instant executionStarted) {
      ExecutionComplete completeEvent =
          ExecutionComplete.success(execution, executionStarted, clock.now());
      try {
        completion.complete(completeEvent, new ExecutionOperations(taskRepository, execution));
        statsRegistry.registerSingleCompletedExecution(completeEvent);
      } catch (Throwable e) {
        statsRegistry.register(StatsRegistry.SchedulerStatsEvent.COMPLETIONHANDLER_ERROR);
        statsRegistry.register(StatsRegistry.SchedulerStatsEvent.UNEXPECTED_ERROR);
        log.error(
            "Failed while completing execution {}. Execution will likely remain scheduled and locked/picked. "
                + "The execution should be detected as dead in {}, and handled according to the tasks DeadExecutionHandler.",
            execution,
            getMaxAgeBeforeConsideredDead(),
            e);
      }
    }

    private void failure(
        FailureHandler failureHandler,
        Execution execution,
        Throwable cause,
        Instant executionStarted) {
      ExecutionComplete completeEvent =
          ExecutionComplete.failure(execution, executionStarted, clock.now(), cause);
      try {
        failureHandler.onFailure(completeEvent, new ExecutionOperations(taskRepository, execution));
        statsRegistry.registerSingleCompletedExecution(completeEvent);
      } catch (Throwable e) {
        statsRegistry.register(StatsRegistry.SchedulerStatsEvent.FAILUREHANDLER_ERROR);
        statsRegistry.register(StatsRegistry.SchedulerStatsEvent.UNEXPECTED_ERROR);
        log.error(
            "Failed while completing execution {}. Execution will likely remain scheduled and locked/picked. "
                + "The execution should be detected as dead in {}, and handled according to the tasks DeadExecutionHandler.",
            execution,
            getMaxAgeBeforeConsideredDead(),
            e);
      }
    }
  }

  public static SchedulerBuilder create(DataSource dataSource, Task<?>... knownTasks) {
    return create(dataSource, Arrays.asList(knownTasks));
  }

  public static SchedulerBuilder create(DataSource dataSource, List<Task<?>> knownTasks) {
    return new SchedulerBuilder(dataSource, knownTasks);
  }
}
