package xyz.vopen.mixmicro.kits.task;

/**
 * {@link ResultableExecutor}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
@FunctionalInterface
public interface ResultableExecutor<T> extends Executor {

  T execute(Context ctx) throws Exception;
}
