package xyz.vopen.mixmicro.kits.task;

import xyz.vopen.mixmicro.kits.task.util.Assert;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface ResultTask<T> extends Task {

  /**
   * 获取任务执行的结果，该方法会阻塞当前线程，直到任务执行完成
   *
   * @return 任务执行的结果
   */
  T get();

  /**
   * 在指定的时间内获取任务执行的结果，如果任务在指定的时间内没有完成，则会抛出异常。该方法会阻塞当前线程，直到任务执行完成，或任务超时
   *
   * @param timeout 任务执行超时时间
   * @param unit 时间单位
   * @return 任务执行的结果
   * @throws TimeoutException 如果任务在指定的时间内没有完成，则会抛出该异常
   */
  T get(long timeout, TimeUnit unit) throws TimeoutException;

  class Builder<T> {

    private String type;
    private String id;
    private Progress progress;
    private final ResultExecutor<T> executor;

    protected Builder(ResultExecutor<T> executor) {
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
