package xyz.vopen.mixmicro.kits.task.exception;

public class ExecutionException extends Exception {

  private final Object obj;

  public ExecutionException(Object obj) {
    this("", obj);
  }

  public ExecutionException(String message) {
    this(message, null);
  }

  public ExecutionException(String message, Object obj) {
    super(message, null, false, false);
    this.obj = obj;
  }

  public Object getObject() {
    return obj;
  }
}
