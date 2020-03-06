package xyz.vopen.mixmicro.components.enhance.cache;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheConfigException extends CacheException {
  private static final long serialVersionUID = -3401839239922905427L;

  public CacheConfigException(Throwable cause) {
    super(cause);
  }

  public CacheConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  public CacheConfigException(String message) {
    super(message);
  }
}
