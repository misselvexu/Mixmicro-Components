package xyz.vopen.mixmicro.components.enhance.schedule.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.schedule.core.stats.StatsRegistry;
import xyz.vopen.mixmicro.components.enhance.schedule.core.task.Task;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@SuppressWarnings("rawtypes")
public class TaskResolver {
  private static final Logger LOG = LoggerFactory.getLogger(TaskResolver.class);
  private final StatsRegistry statsRegistry;
  private final Clock clock;
  private final Map<String, Task> taskMap;
  private final Map<String, UnresolvedTask> unresolvedTasks = new ConcurrentHashMap<>();

  public TaskResolver(StatsRegistry statsRegistry, Task<?>... knownTasks) {
    this(statsRegistry, Arrays.asList(knownTasks));
  }

  public TaskResolver(StatsRegistry statsRegistry, List<Task<?>> knownTasks) {
    this(statsRegistry, new SystemClock(), knownTasks);
  }

  public TaskResolver(StatsRegistry statsRegistry, Clock clock, List<Task<?>> knownTasks) {
    this.statsRegistry = statsRegistry;
    this.clock = clock;
    this.taskMap = knownTasks.stream().collect(Collectors.toMap(Task::getName, identity()));
  }

  public Optional<Task> resolve(String taskName) {
    Task task = taskMap.get(taskName);
    if (task == null) {
      addUnresolved(taskName);
      statsRegistry.register(StatsRegistry.SchedulerStatsEvent.UNRESOLVED_TASK);
      LOG.info(
          "Found execution with unknown task-name '{}'. Adding it to the list of known unresolved task-names.",
          taskName);
    }
    return Optional.ofNullable(task);
  }

  private void addUnresolved(String taskName) {
    unresolvedTasks.putIfAbsent(taskName, new UnresolvedTask(taskName));
  }

  public void addTask(Task task) {
    taskMap.put(task.getName(), task);
  }

  public List<UnresolvedTask> getUnresolved() {
    return new ArrayList<>(unresolvedTasks.values());
  }

  public List<String> getUnresolvedTaskNames(Duration unresolvedFor) {
    return unresolvedTasks.values().stream()
        .filter(
            unresolved ->
                Duration.between(unresolved.firstUnresolved, clock.now()).toMillis()
                    > unresolvedFor.toMillis())
        .map(UnresolvedTask::getTaskName)
        .collect(Collectors.toList());
  }

  public void clearUnresolved(String taskName) {
    unresolvedTasks.remove(taskName);
  }

  public class UnresolvedTask {
    private final String taskName;
    private final Instant firstUnresolved;

    public UnresolvedTask(String taskName) {
      this.taskName = taskName;
      firstUnresolved = clock.now();
    }

    public String getTaskName() {
      return taskName;
    }
  }
}
