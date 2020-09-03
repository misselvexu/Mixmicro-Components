package xyz.vopen.mixmicro.components.mongo.client.mapping.lazy.proxy;

import java.util.ConcurrentModificationException;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class LazyReferenceFetchingException extends ConcurrentModificationException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a LazyReferenceFetchingException with the given message
   *
   * @param msg the message to log
   */
  public LazyReferenceFetchingException(final String msg) {
    super(msg);
  }
}
