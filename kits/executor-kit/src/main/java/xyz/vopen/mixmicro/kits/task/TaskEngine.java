package xyz.vopen.mixmicro.kits.task;

import xyz.vopen.mixmicro.kits.task.util.Assert;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public final class TaskEngine {

  /** 默认为 CPU 核数 */
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
   * 执行指定的任务
   *
   * @param task 要执行的任务，使用 {@link TaskEngine#buildTask(MixExecutor)} 创建
   */
  public void go(Task task) {
    Assert.notNull(task);

    executor.submit(task);
  }

  /**
   * 创建无返回结果的任务，任务的执行结果使用回调处理，使用 {@link TaskEngine#go(Task)} 执行创建的任务
   *
   * @param executor 要执行的具体任务
   * @return 任务
   */
  public Task.Builder buildTask(MixExecutor executor) {
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
   * 创建有返回结果的任务，使用 {@link ResultTask#get()} 获取任务执行的结果
   *
   * @param executor 要执行的具体任务
   * @param <T> 任务返回结果的具体类型
   * @return 带有返回结果的任务
   */
  public <T> ResultTask.Builder<T> buildResultTask(ResultExecutor<T> executor) {
    return new ResultTask.Builder<>(executor);
  }

  /**
   * 直接创建并执行一个默认类型和默认 ID 的有返回结果的任务，使用 {@link ResultTask#get()} 获取任务执行的结果
   *
   * @param executor 要执行的具体任务
   * @param <T> 任务返回结果的具体类型
   * @return 有返回结果的任务
   */
  public <T> ResultTask<T> go(ResultExecutor<T> executor) {
    return this.go(null, null, executor);
  }

  /**
   * @param type 任务类型
   * @param id 任务 ID
   * @param executor 要执行的具体任务
   * @param <T> 任务返回结果的具体类型
   * @return 有返回结果的任务
   */
  public <T> ResultTask<T> go(String type, String id, ResultExecutor<T> executor) {
    Assert.notNull(executor);

    ResultTask<T> task = new ResultBaseTask<>(type, id, executor);
    this.executor.submit(task);
    return task;
  }

  /**
   * 判断线程池是否被关闭
   *
   * @return {@code true} 如果线程池已经被关闭
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

  public int getRunningNumberofTask() {
    return this.executor.getRunningNumberofTask();
  }

  // 获取已经完成的任务总量，包含任务组中的任务
  public long getCompletedNumberOfTask() {
    return this.executor.getCompletedNumberOfTask();
  }

  // 获取执行的任务总量
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

    public Builder rejectedExecutionHandler(RejectedExecutionHandler handler) {
      Assert.notNull(handler);

      this.rejectedExecutionHandler = handler;
      return this;
    }

    public Builder completedTaskHandler(CompletedTaskHandler handler) {
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
