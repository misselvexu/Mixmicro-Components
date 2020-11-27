package xyz.vopen.mixmicro.kits.task;

import xyz.vopen.mixmicro.kits.task.exception.ExecutionException;
import xyz.vopen.mixmicro.kits.task.tuples.Tuple;

public final class Context {

  private final Task task;
  private final Group group;
  private final Tuple result = new Tuple();

  public static final class Group {

    private final TaskGroup group;

    protected Group(TaskGroup group) {
      this.group = group;
    }

    public final int incrementCounter() {
      if (group != null) {
        return group.counter.incrementAndGet();
      }
      return -1;
    }

    public final void addData(String key, Object value) {
      if (group != null) {
        group.data.add(key, value);
      }
    }

    public final void addData(Object value) {
      if (group != null) {
        group.data.add(value);
      }
    }
  }

  protected Context(Task task) {
    this.task = task;
    this.group = new Group(null);
  }

  protected Context(Task task, TaskGroup group) {
    this.task = task;
    this.group = new Group(group);
  }

  public final void onProgress(int progress) {
    if (task.getProgress() != null) {
      task.getProgress().call(progress);
    }
  }

  public final void onSuccess(Object... objs) {
    if (((AbstractTask) task).setState(State.RUNNING, State.SUCCESS)
        && task.getCallback() != null) {
      this.toResult(objs);
      task.getCallback().call(this, null);
    }
  }

  public final void onError(String message, Object obj) {
    onError(new ExecutionException(message, obj));
  }

  public final void onError(Exception error) {
    if (((AbstractTask) task).setState(State.RUNNING, State.ERROR) && task.getCallback() != null) {
      task.getCallback().call(null, error);
    }
  }

  public final String getId() {
    return task.getId();
  }

  public final State getState() {
    return task.getState();
  }

  public final String getType() {
    return task.getType();
  }

  public final void toResult(Object... objs) {
    if (objs != null && objs.length > 0) {
      for (Object obj : objs) {
        result.add(obj);
      }
    }
  }

  public final Tuple getResult() {
    return this.result;
  }

  public final Group group() {
    return this.group;
  }
}
