package xyz.vopen.mixmicro.components.enhance.dbm.repository;

/**
 * Extension to Iterable to provide a iterator() method that returns a {@link CloseableIterator}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface CloseableIterable<T> extends Iterable<T> {

  /** Returns an iterator over a set of elements of type T which can be closed. */
  public CloseableIterator<T> closeableIterator();
}
