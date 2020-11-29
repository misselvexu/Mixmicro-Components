package xyz.vopen.mixmicro.kits.task;

import xyz.vopen.mixmicro.kits.Assert;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link TaskEngine}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
public final class TaskEngine {

  private static final int DEFAULT_CORE_SIZE = Runtime.getRuntime().availableProcessors();

  private static final int DEFAULT_MAX_POOL_SIZE = Integer.MAX_VALUE;
  private static final int DEFAULT_QUEUE_CAPACITY = Integer.MAX_VALUE;
  private static final int DEFAULT_KEEP_ALIVE_SECONDS = 60; // S
  private static final String DEFAULT_TASK_GROUP_NAME_PREFIX = "mixmicro-task-group-";

  private final AtomicLong taskGroupNumber = new AtomicLong(0);

  private final DefaultThreadPoolExecutor executor;

  private TaskEngine(DefaultThreadPoolExecutor executor) {
    this.executor = executor;
  }

  /**
   * Performs assigned tasks
   *
   * @param task The task to be executed, using {@link TaskEngine#newTaskBuilder(SimpleExecutor)} to
   *     create the
   */
  public void execute(Task task) {
    Assert.notNull(task);

    executor.submit(task);
  }

  /**
   * Create tasks with no return result, use callbacks for task execution, and execute the created
   * task using {@link TaskEngine#execute(Task)}.
   *
   * @param executor specific task to be performed
   * @return Task
   */
  public Task.Builder newTaskBuilder(SimpleExecutor executor) {
    return new Task.Builder(executor);
  }

  public TaskGroup prepareGroup() {
    return this.prepareGroup(DEFAULT_TASK_GROUP_NAME_PREFIX + taskGroupNumber.incrementAndGet());
  }

  public TaskGroup prepareGroup(String name) {
    TaskGroup taskGroup = new TaskGroup(name, executor);
    executor.addTaskGroup(taskGroup);
    return taskGroup;
  }

  /**
   * Create a task with a return result and use {@link ResultTask#get()} to get the result of the
   * task execution.
   *
   * @param executor specific task to be performed
   * @param <T> The specific type of result returned by the task.
   * @return Tasks with returned results
   */
  public <T> ResultTask.Builder<T> newResultTaskBuilder(ResultableExecutor<T> executor) {
    return new ResultTask.Builder<>(executor);
  }

  /**
   * Directly create and execute a task with a default type and a default ID with the result
   * returned, using {@link ResultTask#get()} to get the result of the task execution.
   *
   * @param executor specific task to be performed
   * @param <T> The specific type of result returned by the task.
   * @return Tasks with returned results
   */
  public <T> ResultTask<T> execute(ResultableExecutor<T> executor) {
    return this.execute(null, null, executor);
  }

  /**
   * @param type Task type
   * @param id Task ID
   * @param executor specific task to be performed
   * @param <T> The specific type of result returned by the task.
   * @return Tasks with returned results
   */
  public <T> ResultTask<T> execute(String type, String id, ResultableExecutor<T> executor) {
    Assert.notNull(executor);

    ResultTask<T> task = new ResultBaseTask<>(type, id, executor);
    this.executor.submit(task);
    return task;
  }

  /**
   * Determines if the thread pool is closed
   *
   * @return {@code true} if the thread pool has been closed
   * @see ThreadPoolExecutor#isShutdown()
   */
  public boolean isShutdown() {
    return this.executor.isShutdown();
  }

  /**
   * 试图关闭线程池
   *
   * @see ThreadPoolExecutor#shutdown()
   */
  public void shutdown() {
    this.executor.shutdown();
  }

  public List<Task> getRunningTasks() {
    return executor.getRunningTasks();
  }

  public int getRunningNumberOfTask() {
    return this.executor.getRunningNumberOfTask();
  }

  public long getCompletedNumberOfTask() {
    return this.executor.getCompletedNumberOfTask();
  }

  public long getTotalNumberOfTask() {
    return this.executor.getTotalNumberOfTask();
  }

  protected Collection<TaskGroup> getRunningTaskGroups() {
    return this.executor.getRunningTaskGroups();
  }

  public static class Builder {

    private int coreSize = DEFAULT_CORE_SIZE;
    private int maxPoolSize = DEFAULT_MAX_POOL_SIZE;
    private int queueCapacity = DEFAULT_QUEUE_CAPACITY;
    private int keepAliveSeconds = DEFAULT_KEEP_ALIVE_SECONDS;
    private RejectedExecutionHandler rejectedExecutionHandler;
    private CompletedTaskHandler completedTaskHandler;

    public Builder corePoolSize(int corePoolSize) {
      if (corePoolSize > 0) {
        this.coreSize = corePoolSize;
      }
      return this;
    }

    public Builder maxPoolSize(int maxPoolSize) {
      if (maxPoolSize > 0) {
        this.maxPoolSize = maxPoolSize;
      }
      return this;
    }

    public Builder keepAliveSeconds(int keepAliveSeconds) {
      if (keepAliveSeconds >= 0) {
        this.keepAliveSeconds = keepAliveSeconds;
      }
      return this;
    }

    public Builder queueCapacity(int queueCapacity) {
      this.queueCapacity = queueCapacity;
      return this;
    }

    public Builder rejectedPolicy(RejectedExecutionHandler handler) {
      Assert.notNull(handler);

      this.rejectedExecutionHandler = handler;
      return this;
    }

    public Builder completedHandler(CompletedTaskHandler handler) {
      this.completedTaskHandler = handler;
      return this;
    }

    public TaskEngine build() {
      BlockingQueue<Runnable> queue = createQueue(this.queueCapacity);
      DefaultThreadPoolExecutor executor =
          new DefaultThreadPoolExecutor(
              this.coreSize,
              this.maxPoolSize,
              this.keepAliveSeconds,
              TimeUnit.SECONDS,
              queue,
              Executors.defaultThreadFactory(),
              getRejectedExecutionHandler(this.rejectedExecutionHandler),
              this.completedTaskHandler);
      return new TaskEngine(executor);
    }

    private BlockingQueue<Runnable> createQueue(int queueCapacity) {
      if (queueCapacity > 0) {
        return new LinkedBlockingQueue<>(queueCapacity);
      }
      return new SynchronousQueue<>();
    }

    private RejectedExecutionHandler getRejectedExecutionHandler(RejectedExecutionHandler handler) {
      if (handler != null) {
        return handler;
      }
      return new ThreadPoolExecutor.AbortPolicy();
    }
  }
}
