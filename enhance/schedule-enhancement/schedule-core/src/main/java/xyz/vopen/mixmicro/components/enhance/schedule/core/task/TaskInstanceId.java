package xyz.vopen.mixmicro.components.enhance.schedule.core.task;

import java.util.Objects;

public interface TaskInstanceId {
  String getTaskName();

  String getId();

  static TaskInstanceId of(String taskName, String id) {
    return new StandardTaskInstanceId(taskName, id);
  }

  class StandardTaskInstanceId implements TaskInstanceId {
    private final String taskName;
    private final String id;

    public StandardTaskInstanceId(String taskName, String id) {
      this.taskName = taskName;
      this.id = id;
    }

    @Override
    public String getTaskName() {
      return this.taskName;
    }

    @Override
    public String getId() {
      return this.id;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      StandardTaskInstanceId that = (StandardTaskInstanceId) o;
      return Objects.equals(taskName, that.taskName) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
      return Objects.hash(taskName, id);
    }
  }
}
