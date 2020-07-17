package xyz.vopen.mixmicro.kits.redis.embedded.exceptions;

/**
 * {@link OsDetectionException}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class OsDetectionException extends RuntimeException {
  public OsDetectionException(String message) {
    super(message);
  }

  public OsDetectionException(Throwable cause) {
    super(cause);
  }
}
