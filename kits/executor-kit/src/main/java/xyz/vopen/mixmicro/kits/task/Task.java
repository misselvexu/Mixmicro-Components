package xyz.vopen.mixmicro.kits.task;

import xyz.vopen.mixmicro.kits.Assert;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * {@link Task}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
public interface Task {

  String DEFAULT_TYPE_NAME = "DEFAULT";

  /**
   * Get task creation time
   *
   * @return Task creation time
   */
  long createdAt();

  /**
   * Get task start time
   *
   * @return The time when the task was started.
   */
  long getStartTime();

  /**
   * Get end of mission time
   *
   * @return Task end time
   */
  long getEndTime();

  /**
   * Get the type of task
   *
   * @return Task type
   */
  String getType();

  /**
   * Get the status of the current task
   *
   * @return Task Status
   * @see State
   */
  State getState();

  /**
   * Unique ID of the current task
   *
   * @return Task ID
   */
  String getId();

  /**
   * Gets a callback to the progress of the current task setting, using {@code Context.onProgress(int)} to set the progress. Could be {@code null}.
   *
   * @return {@link Progress}
   * @see Context#onProgress(int)
   * @see Progress
   */
  Progress getProgress();

  /**
   * Get the callback on completion of the current task setup, possibly {@code null}.
   *
   * @return {@link Callback}
   * @see Context#onSuccess(Object...)
   * @see Context#onError(Exception)
   * @see Context#onError(String, Object)
   * @see Callback
   */
  Callback getCallback();

  /**
   * Cancel current task
   *
   * @param mayInterruptIfRunning {@code true} If the thread executing the current task needs to be interrupted. Otherwise the task may be completed!
   * @return {@code true} if the current task is cancelled; otherwise return {@code false}
   */
  boolean cancel(boolean mayInterruptIfRunning);

  /* Waiting for the current task to complete will block the current thread from continuing until the task is completed */
  void await();

  /**
   * Waiting for the completion of the current task execution for the specified time will block the current thread from continuing until the task is completed or the specified time is reached. If the task remains incomplete after the specified time, an exception is thrown.
   *
   * @param timeout timeout
   * @param unit Time unit
   * @throws TimeoutException This exception will be thrown if the task is still incomplete after the specified time.
   */
  void await(long timeout, TimeUnit unit) throws TimeoutException;

  class Builder {

    protected final SimpleExecutor executor;
    protected String type;
    protected String id;
    protected Progress progress;
    protected Callback callback;

    protected Builder(SimpleExecutor executor) {
      Assert.notNull(executor);

      this.executor = executor;
    }

    /**
     * Set the type of task, if not, default is {@code DEFAULT}
     *
     * @param type Task type
     * @return {@link Builder}
     */
    public Builder type(String type) {
      this.type = type;
      return this;
    }

    /**
     * Sets a progress callback for the task. Use {@link Context#onProgress(int)} to trigger the callback.
     *
     * @param progress progress callback
     * @return {@link Builder}
     */
    public Builder progress(Progress progress) {
      this.progress = progress;
      return this;
    }

    /**
     * Setting callbacks on task completion can be useful {@link Context#onSuccess(Object...)} {@link Context#onError(String, Object)}
     * or {@link Context#onError(Exception)} triggers this callback.
     *
     * <p>If {@link Context#onSuccess(Object...)} is called, the callback is triggered, the task state is successful {@link State#SUCCESS}, and the second argument of the callback function is successful. triggers the callback, the task state is success {@link State#SUCCESS}, and the second argument of the callback function
     * {@code Exception} will be {@code null}
     *
     * <p>If you call {@link Context#onError(String, Object)} or {@link Context#onError(Exception)}
     * The callback is triggered, the considered state is an error {@link State#ERROR}, and the second argument of the callback function is not {@code null}.
     *
     * @param callback A callback on task completion.
     * @return {@link Builder}
     * @see Callback#call(Context, Exception)
     */
    public Builder afterExecuted(Callback callback) {
      this.callback = callback;
      return this;
    }

    public Task build() {
      BaseTask task = new BaseTask(this.type, this.id, executor);
      task.setProgress(progress);
      task.setCallback(callback);
      return task;
    }
  }
}
