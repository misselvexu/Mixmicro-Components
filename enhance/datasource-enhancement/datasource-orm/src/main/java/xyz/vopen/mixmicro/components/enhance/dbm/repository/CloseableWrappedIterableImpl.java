package xyz.vopen.mixmicro.components.enhance.dbm.repository;

import xyz.vopen.mixmicro.components.enhance.dbm.misc.IOUtils;

import java.io.IOException;

/**
 * Class which is used to help folks use for loops but still close at the end. This is a wrapper to
 * allow multiple threads to iterate across the same repository or the same lazy collection at the same
 * time. See {@link Repository#getWrappedIterable()} or {@link ForeignCollection#getWrappedIterable()}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class CloseableWrappedIterableImpl<T> implements CloseableWrappedIterable<T> {

  private final CloseableIterable<T> iterable;
  private CloseableIterator<T> iterator;

  public CloseableWrappedIterableImpl(CloseableIterable<T> iterable) {
    this.iterable = iterable;
  }

  @Override
  public CloseableIterator<T> iterator() {
    return closeableIterator();
  }

  @Override
  public CloseableIterator<T> closeableIterator() {
    IOUtils.closeQuietly(this);
    iterator = iterable.closeableIterator();
    return iterator;
  }

  @Override
  public void close() throws IOException {
    if (iterator != null) {
      iterator.close();
      iterator = null;
    }
  }
}
