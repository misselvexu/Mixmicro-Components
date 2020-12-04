package xyz.vopen.mixmicro.kits.task;

/**
 * {@link BaseTask}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
class BaseTask extends AbstractTask {

  private final SimpleExecutor executor;

  protected BaseTask(SimpleExecutor executor) {
    this(null, null, executor);
  }

  protected BaseTask(String type, String id, SimpleExecutor executor) {
    super(type, id);
    this.executor = executor;
  }

  public SimpleExecutor getExecutor() {
    return executor;
  }
}
