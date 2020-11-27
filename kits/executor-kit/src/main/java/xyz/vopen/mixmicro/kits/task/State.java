package xyz.vopen.mixmicro.kits.task;

public enum State {
  UNKNOWN("unknown status", -1),
  INIT("task is initialized", 0),
  QUEUED("task is waiting in queue", 1),
  RUNNING("task is running", 2),
  CANCEL("task is canceled", 3),
  SUCCESS("task execute success", 4),
  ERROR("task execute failed", 5),
  TIMEOUT("task execute timeout", 6);

  private final String name;
  private final int value;

  State(String name, int value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public int getValue() {
    return value;
  }
}
