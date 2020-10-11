package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

import java.util.function.Supplier;

public final class TaskInstance<T> implements TaskInstanceId {

  private final String taskName;
  private final String id;
  private final Supplier<T> dataSupplier;

  public TaskInstance(String taskName, String id) {
    this(taskName, id, (T) null);
  }

  public TaskInstance(String taskName, String id, T data) {
    this(taskName, id, () -> data);
  }

  public TaskInstance(String taskName, String id, Supplier<T> dataSupplier) {
    this.taskName = taskName;
    this.id = id;
    this.dataSupplier = dataSupplier;
  }

  public String getTaskAndInstance() {
    return taskName + "_" + id;
  }

  public String getTaskName() {
    return taskName;
  }

  @Override
  public String getId() {
    return id;
  }

  public T getData() {
    return dataSupplier.get();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TaskInstance<?> that = (TaskInstance<?>) o;

    if (!taskName.equals(that.taskName)) return false;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    int result = taskName.hashCode();
    result = 31 * result + id.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return "TaskInstance: " + "task=" + taskName + ", id=" + id;
  }
}
