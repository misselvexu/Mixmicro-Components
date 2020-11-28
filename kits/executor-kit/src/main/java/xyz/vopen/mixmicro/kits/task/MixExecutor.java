package xyz.vopen.mixmicro.kits.task;

/**
 * {@link MixExecutor}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
@FunctionalInterface
public interface MixExecutor extends Executor {

  void execute(Context ctx);
}
