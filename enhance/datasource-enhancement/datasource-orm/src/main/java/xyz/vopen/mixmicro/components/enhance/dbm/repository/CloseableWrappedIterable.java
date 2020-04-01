package xyz.vopen.mixmicro.components.enhance.dbm.repository;

import java.io.Closeable;
import java.io.IOException;

/**
 * Extension to CloseableIterable which defines a class which has an iterator() method that returns
 * a {@link CloseableIterator} but also can be closed itself. This allows us to do something like
 * this pattern:
 *
 * <pre>
 * CloseableWrappedIterable&lt;Foo&gt; wrapperIterable = fooRepository.getCloseableIterable();
 * try {
 *   for (Foo foo : wrapperIterable) {
 *       ...
 *   }
 * } finally {
 *   wrapperIterable.close();
 * }
 * </pre>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface CloseableWrappedIterable<T> extends CloseableIterable<T>, Closeable {

  /** This will close the last iterator returned by the {@link #iterator()} method. */
  @Override
  public void close() throws IOException;
}
