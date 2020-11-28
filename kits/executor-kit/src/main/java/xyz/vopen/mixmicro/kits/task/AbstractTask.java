package xyz.vopen.mixmicro.kits.task;

import xyz.vopen.mixmicro.kits.Assert;
import xyz.vopen.mixmicro.kits.task.exception.TaskException;
import xyz.vopen.mixmicro.kits.task.util.Utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * {@link AbstractTask}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
abstract class AbstractTask implements Task {

  private String id; // 任务的 ID
  private String type; // 任务的类型
  private Progress progress; // 进度回调函数
  private Callback callback;
  private final AtomicReference<State> state = new AtomicReference<>(State.INIT); // 任务的状态
  protected Future<?> future;

  private final long createTime;
  private long startTime;
  private long endTime;

  protected AbstractTask(String type, String id) {
    if (Utils.isEmpty(type)) {
      this.type = DEFAULT_TYPE_NAME;
    } else {
      this.type = type;
    }
    if (Utils.isEmpty(id)) {
      this.id = Utils.generateId();
    } else {
      this.id = id;
    }
    createTime = System.currentTimeMillis();
  }

  @Override
  public long createdAt() {
    return createTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  @Override
  public long getStartTime() {
    return startTime;
  }

  public void setEndTime(long endTime) {
    this.endTime = endTime;
  }

  @Override
  public long getEndTime() {
    return endTime;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Override
  public String getType() {
    return this.type;
  }

  public final boolean setState(State expect, State update) {
    Assert.notNull(state);

    return this.state.compareAndSet(expect, update);
  }

  private void setStateToCancel() {
    this.state.set(State.CANCEL);
  }

  @Override
  public State getState() {
    return this.state.get();
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String getId() {
    return this.id;
  }

  public void setProgress(Progress progress) {
    this.progress = progress;
  }

  @Override
  public Progress getProgress() {
    return this.progress;
  }

  public void setCallback(Callback callback) {
    this.callback = callback;
  }

  @Override
  public Callback getCallback() {
    return callback;
  }

  public void setFuture(Future<?> future) {
    this.future = future;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    if (future == null) {
      setStateToCancel();
      return true;
    }
    if (future.cancel(mayInterruptIfRunning)) {
      setStateToCancel();
      return true;
    }
    return false;
  }

  @Override
  public void await() {
    if (future != null) {
      try {
        future.get();
      } catch (InterruptedException | ExecutionException e) {
        throw new TaskException(e);
      }
    }
  }

  @Override
  public void await(long timeout, TimeUnit unit) throws TimeoutException {
    if (future != null) {
      try {
        future.get(timeout, unit);
      } catch (InterruptedException | ExecutionException e) {
        throw new TaskException(e);
      }
    }
  }
}
