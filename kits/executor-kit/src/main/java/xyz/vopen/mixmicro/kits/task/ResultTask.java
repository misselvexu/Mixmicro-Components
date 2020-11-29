package xyz.vopen.mixmicro.kits.task;

import xyz.vopen.mixmicro.kits.Assert;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * {@link ResultTask}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
public interface ResultTask<T> extends Task {

  /**
   * Gets the result of task execution, which blocks the current thread until the task is completed.
   *
   * @return Result of task execution
   */
  T get();

  /**
   * Get the result of the task execution within the specified time, and throw an exception if the
   * task is not completed within the specified time. This method blocks the current thread until
   * the task execution completes, or until the task times out!
   *
   * @param timeout Task execution timeout
   * @param unit Time unit
   * @return Result of task execution
   * @throws TimeoutException This exception is thrown if the task is not completed within the
   *     specified time.
   */
  T get(long timeout, TimeUnit unit) throws TimeoutException;

  class Builder<T> {

    private String type;
    private String id;
    private Progress progress;
    private final ResultableExecutor<T> executor;

    protected Builder(ResultableExecutor<T> executor) {
      Assert.notNull(executor);

      this.executor = executor;
    }

    public Builder<T> type(String type) {
      this.type = type;
      return this;
    }

    public Builder<T> id(String id) {
      this.id = id;
      return this;
    }

    public Builder<T> progress(Progress progress) {
      this.progress = progress;
      return this;
    }

    public ResultTask<T> build() {
      ResultBaseTask<T> task = new ResultBaseTask<>(type, id, executor);
      task.setProgress(this.progress);
      return task;
    }
  }
}
