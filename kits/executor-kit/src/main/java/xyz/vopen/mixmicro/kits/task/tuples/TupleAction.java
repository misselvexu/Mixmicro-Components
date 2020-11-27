package xyz.vopen.mixmicro.kits.task.tuples;

/**
 * {@link TupleAction}
 *
 * <p>Class TupleAction Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/11/27
 */
public interface TupleAction<N, I, V> {

  /**
   * Tuple Action Execute .
   *
   * @param name tuple key name
   * @param index tuple index
   * @param value tuple value
   */
  void apply(N name, I index, V value);
}
