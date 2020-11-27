package xyz.vopen.mixmicro.kits.task;

import xyz.vopen.mixmicro.kits.task.util.Assert;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface Task {

  String DEFAULT_TYPE_NAME = "DEFAULT";

  /**
   * 获取任务创建时间
   *
   * @return 任务创建时间
   */
  long createdAt();

  /**
   * 获取任务开始执行时间
   *
   * @return 任务开始执行的时间
   */
  long getStartTime();

  /**
   * 获取任务结束时间
   *
   * @return 任务结束时间
   */
  long getEndTime();

  /**
   * 获取任务的类型
   *
   * @return 任务类型
   */
  String getType();

  /**
   * 获取当前任务的状态
   *
   * @return 任务状态
   * @see State
   */
  State getState();

  /**
   * 当前任务的唯一 ID
   *
   * @return 任务 ID
   */
  String getId();

  /**
   * 获取当前任务设置的进度回调，使用 {@code Context.onProgress(int)} 设置进度。可能为 {@code null}。
   *
   * @return {@link Progress}
   * @see Context#onProgress(int)
   * @see Progress
   */
  Progress getProgress();

  /**
   * 获取当前任务设置的完成时的回调，可能为 {@code null}
   *
   * @return {@link Callback}
   * @see Context#onSuccess(Object...)
   * @see Context#onError(Exception)
   * @see Context#onError(String, Object)
   * @see Callback
   */
  Callback getCallback();

  /**
   * 取消当前任务
   *
   * @param mayInterruptIfRunning {@code true} 如果执行当前任务的线程需要被中断。否则任务可能会执行完成
   * @return {@code true} 如果当前任务被取消；否则返回 {@code false}
   */
  boolean cancel(boolean mayInterruptIfRunning);

  /** 等待当前任务执行完成，会阻断当前线程继续执行，直到任务完成 */
  void await();

  /**
   * 在指定的时间内等待当前任务执行完成，会阻断当前线程继续执行，直到任务完成或达到了指定的时间。 如果到达指定时间，任务仍未完成，将会抛出异常
   *
   * @param timeout 超时时间
   * @param unit 时间单位
   * @throws TimeoutException 如果到达指定时间，任务仍未完成，将会抛出此异常
   */
  void await(long timeout, TimeUnit unit) throws TimeoutException;

  class Builder {

    protected final MixExecutor executor;
    protected String type;
    protected String id;
    protected Progress progress;
    protected Callback callback;

    protected Builder(MixExecutor executor) {
      Assert.notNull(executor);

      this.executor = executor;
    }

    /**
     * 设置任务的类型，如果不设置，默认为 {@code DEFAULT}
     *
     * @param type 任务类型
     * @return {@link Builder}
     */
    public Builder type(String type) {
      this.type = type;
      return this;
    }

    /**
     * 设置任务的进度回调。使用 {@link Context#onProgress(int)} 会触发该回调
     *
     * @param progress 进度回调
     * @return {@link Builder}
     */
    public Builder progress(Progress progress) {
      this.progress = progress;
      return this;
    }

    /**
     * 设置任务完成时的回调，可以很实用 {@link Context#onSuccess(Object...)}、{@link Context#onError(String, Object)}
     * 或 {@link Context#onError(Exception)} 触发该回调。
     *
     * <p>如果调用 {@link Context#onSuccess(Object...)} 触发回调，任务状态为成功 {@link State#SUCCESS}，并且回调函数的第二个参数
     * {@code Exception} 将为 {@code null}
     *
     * <p>如果调用 {@link Context#onError(String, Object)} 或 {@link Context#onError(Exception)}
     * 触发回调，认为的状态为错误 {@link State#ERROR}， 并且回调函数的第二个参数不为 {@code null}
     *
     * @param callback 任务完成时的回调
     * @return {@link Builder}
     * @see Callback#call(Context, Exception)
     */
    public Builder end(Callback callback) {
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
