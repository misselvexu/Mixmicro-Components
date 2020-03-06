package xyz.vopen.mixmicro.components.enhance.cache;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheInvokeException extends CacheException {

  private static final long serialVersionUID = -9002505061387176702L;

  public CacheInvokeException(String message, Throwable cause) {
    super(message, cause);
  }

  public CacheInvokeException(Throwable cause) {
    super(cause);
  }
}
