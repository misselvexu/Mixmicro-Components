package xyz.vopen.mixmicro.kits.task;

/**
 * {@link CompletedTaskHandler}
 *
 * <p>Class Tuple Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
public interface CompletedTaskHandler {

  void handle(final Task task);
}
