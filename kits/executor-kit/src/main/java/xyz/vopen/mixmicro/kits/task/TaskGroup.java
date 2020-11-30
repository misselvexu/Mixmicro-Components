package xyz.vopen.mixmicro.kits.task;

import xyz.vopen.mixmicro.kits.Assert;
import xyz.vopen.mixmicro.kits.annotation.Unstable;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link TaskGroup}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
@Unstable
public final class TaskGroup {

  private final DefaultThreadPoolExecutor executor;
  private final String name;
  private final String id;

  protected final AtomicInteger counter = new AtomicInteger(0);
  protected final DataTuple dataTuple = new DataTuple();

  private final AtomicInteger latch = new AtomicInteger(0);
  private final Deque<Task> runningTaskQueue = new ConcurrentLinkedDeque<>();

  protected TaskGroup(String name, DefaultThreadPoolExecutor executor) {
    this.name = name;
    this.id = TaskUtils.generateId();
    this.executor = executor;
  }

  public void whenComplete(final Runnable runnable) {
    new Thread(
            () -> {
              await();
              if (runnable != null) {
                runnable.run();
              }
            })
        .start();
  }

  public void await() {
    while (true) {
      if (latch.get() == 0) break;
    }
    executor.removeTaskGroup(this);
  }

  public int getCounter() {
    return counter.get();
  }

  public DataTuple getData() {
    return this.dataTuple;
  }

  public void submit(Item item) {
    Assert.notNull(item);

    item.setTaskGroup(this);
    latch.incrementAndGet();
    runningTaskQueue.offer(item);
    executor.submit(item, this);
  }

  public Builder newItemBuilder(SimpleExecutor executor) {
    Assert.notNull(executor);

    return new Builder(executor, this);
  }

  public Item newItem(SimpleExecutor executor) {
    Assert.notNull(executor);

    return new Builder(executor, this).build();
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public final List<Task> getRunningTasks() {
    return new LinkedList<>(runningTaskQueue);
  }

  public static final class Item extends BaseTask {

    private TaskGroup taskGroup;

    protected Item(String type, String id, SimpleExecutor executor, TaskGroup taskGroup) {
      super(type, id, executor);
      this.taskGroup = taskGroup;
    }

    private void setTaskGroup(TaskGroup group) {
      this.taskGroup = group;
    }

    public String getGroupName() {
      return this.taskGroup.name;
    }
  }

  public static final class Builder extends Task.Builder {

    private final TaskGroup taskGroup;

    protected Builder(SimpleExecutor executor, TaskGroup taskGroup) {
      super(executor);
      this.taskGroup = taskGroup;
    }

    @Override
    public Item build() {
      Item item = new Item(this.type, this.id, executor, taskGroup);
      item.setProgress(this.progress);
      item.setCallback(this.callback);
      return item;
    }
  }

  protected static class ItemExecutor extends TaskExecutor {

    private final TaskGroup group;

    protected ItemExecutor(BaseTask task, TaskGroup group) {
      super(task);
      this.group = group;
    }

    @Override
    public void run() {
      super.run();
    }

    @Override
    protected Context createContext() {
      return new Context(task, group);
    }

    @Override
    protected void finallyExecute() {
      super.finallyExecute();
      this.group.latch.decrementAndGet();
      this.group.runningTaskQueue.remove(this.task);
    }
  }
}
