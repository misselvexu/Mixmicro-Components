package xyz.vopen.mixmicro.kits.task;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * {@link DefaultThreadPoolExecutor}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
final class DefaultThreadPoolExecutor extends ThreadPoolExecutor {

  private final AtomicLong taskNumber = new AtomicLong(0);
  private final AtomicLong completedTaskNumber = new AtomicLong(0);
  private final Deque<Task> runningQueue = new ConcurrentLinkedDeque<>();
  private final Map<String, TaskGroup> runningTaskGrous = new ConcurrentHashMap<>();
  private final LinkedBlockingDeque<Task> completedQueue = new LinkedBlockingDeque<>();

  private final CompletedTaskHandler completedTaskHandler;

  public DefaultThreadPoolExecutor(
      int corePoolSize,
      int maximumPoolSize,
      long keepAliveTime,
      TimeUnit unit,
      BlockingQueue<Runnable> workQueue,
      ThreadFactory threadFactory,
      RejectedExecutionHandler handler,
      CompletedTaskHandler completedTaskHandler) {
    super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    if (completedTaskHandler == null) {
      this.completedTaskHandler = new DefaultCompletedTaskHandler();
    } else {
      this.completedTaskHandler = completedTaskHandler;
    }
    startHandleCompletedTask();
  }

  public void submit(Task task) {
    if (task instanceof BaseTask) {
      BaseTask bTask = (BaseTask) task;
      TaskExecutor taskExecutor = new TaskExecutor(bTask);
      RunnableFuture<Object> future = newTaskFor(task, taskExecutor);
      bTask.setFuture(future);
      bTask.setState(State.INIT, State.QUEUED);
      execute(future);
    } else if (task instanceof ResultBaseTask) {
      ResultBaseTask<?> bTask = (ResultBaseTask<?>) task;
      ResultTaskExecutor<?> executor = new ResultTaskExecutor<>(bTask);
      RunnableFuture<?> future = newTaskFor(task, executor);
      bTask.setFuture(future);
      bTask.setState(State.INIT, State.QUEUED);
      execute(future);
    }
    runningQueue.offer(task);
  }

  public void submit(TaskGroup.Item item, TaskGroup group) {
    TaskGroup.ItemExecutor executor = new TaskGroup.ItemExecutor(item, group);
    RunnableFuture<Object> future = newTaskFor(item, executor);
    item.setFuture(future);
    item.setState(State.INIT, State.QUEUED);
    execute(future);
    runningQueue.offer(item);
  }

  private <T> RunnableFuture<T> newTaskFor(Task task, Callable<T> callable) {
    return new CustomFutureTask<>(task, callable);
  }

  private <T> RunnableFuture<T> newTaskFor(Task task, Runnable runnable) {
    return new CustomFutureTask<>(task, runnable, null);
  }

  @Override
  protected void beforeExecute(Thread t, Runnable r) {
    taskNumber.incrementAndGet();
  }

  @Override
  protected void afterExecute(Runnable r, Throwable t) {
    if (r instanceof CustomFutureTask) {
      CustomFutureTask<?> futureTask = (CustomFutureTask<?>) r;
      Task task = futureTask.getTask();
      runningQueue.remove(task);
      completedQueue.offer(task);
      completedTaskNumber.incrementAndGet();
    }
  }

  private void startHandleCompletedTask() {
    new Thread(
            () -> {
              while (true) {
                try {
                  Task take = completedQueue.take();
                  DefaultThreadPoolExecutor.this.completedTaskHandler.handle(take);
                } catch (Throwable ignore) {
                }
              }
            })
        .start();
  }

  protected final List<Task> getRunningTasks() {
    return new LinkedList<>(runningQueue);
  }

  protected int getRunningNumberOfTask() {
    return runningQueue.size();
  }

  protected long getCompletedNumberOfTask() {
    return completedTaskNumber.get();
  }

  protected long getTotalNumberOfTask() {
    return taskNumber.get();
  }

  protected void addTaskGroup(TaskGroup taskGroup) {
    this.runningTaskGrous.put(taskGroup.getId(), taskGroup);
  }

  protected void removeTaskGroup(TaskGroup taskGroup) {
    this.runningTaskGrous.remove(taskGroup.getId());
  }

  protected Collection<TaskGroup> getRunningTaskGroups() {
    return new LinkedList<>(this.runningTaskGrous.values());
  }

  private static class CustomFutureTask<V> extends FutureTask<V> {

    private final Task task;

    public CustomFutureTask(Task task, Callable<V> callable) {
      super(callable);
      this.task = task;
    }

    public CustomFutureTask(Task task, Runnable runnable, V result) {
      super(runnable, result);
      this.task = task;
    }

    public final Task getTask() {
      return task;
    }
  }
}
