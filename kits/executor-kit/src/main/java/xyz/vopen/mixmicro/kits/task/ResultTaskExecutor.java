package xyz.vopen.mixmicro.kits.task;

import java.util.concurrent.Callable;

/**
 * {@link ResultTaskExecutor}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
class ResultTaskExecutor<T> implements Callable<T> {

  private final ResultBaseTask<T> task;

  protected ResultTaskExecutor(ResultBaseTask<T> task) {
    this.task = task;
  }

  @Override
  public T call() throws Exception {
    task.setStartTime(System.currentTimeMillis());
    if (task.setState(State.QUEUED, State.RUNNING)) {
      Context context = createContext();
      try {
        T result = task.getExecutor().execute(context);
        context.onSuccess();
        return result;
      } finally {
        finallyExecute();
      }
    } else {
      finallyExecute();
      return null;
    }
  }

  protected Context createContext() {
    return new Context(task);
  }

  protected void finallyExecute() {
    task.setEndTime(System.currentTimeMillis());
  }
}
