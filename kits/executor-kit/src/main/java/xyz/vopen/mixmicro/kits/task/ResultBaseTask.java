package xyz.vopen.mixmicro.kits.task;

import xyz.vopen.mixmicro.kits.task.exception.TaskException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * {@link ResultBaseTask}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
class ResultBaseTask<T> extends AbstractTask implements ResultTask<T> {

  private final ResultExecutor<T> executor;

  protected ResultBaseTask(ResultExecutor<T> executor) {
    this(null, null, executor);
  }

  protected ResultBaseTask(String type, String id, ResultExecutor<T> executor) {
    super(type, id);
    this.executor = executor;
  }

  public ResultExecutor<T> getExecutor() {
    return executor;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T get() {
    if (future != null) {
      try {
        return (T) future.get();
      } catch (InterruptedException | ExecutionException e) {
        throw new TaskException(e);
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T get(long timeout, TimeUnit unit) throws TimeoutException {
    if (future != null) {
      try {
        return (T) future.get(timeout, unit);
      } catch (InterruptedException | ExecutionException e) {
        throw new TaskException(e);
      }
    }
    return null;
  }
}
