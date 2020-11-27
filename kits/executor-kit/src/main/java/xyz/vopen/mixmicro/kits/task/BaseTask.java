package xyz.vopen.mixmicro.kits.task;

class BaseTask extends AbstractTask {

  private final MixExecutor executor;

  protected BaseTask(MixExecutor executor) {
    this(null, null, executor);
  }

  protected BaseTask(String type, String id, MixExecutor executor) {
    super(type, id);
    this.executor = executor;
  }

  public MixExecutor getExecutor() {
    return executor;
  }
}
