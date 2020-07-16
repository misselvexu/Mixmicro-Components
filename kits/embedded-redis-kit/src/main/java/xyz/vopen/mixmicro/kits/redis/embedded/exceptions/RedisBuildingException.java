package xyz.vopen.mixmicro.kits.redis.embedded.exceptions;

/**
 * {@link RedisBuildingException}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class RedisBuildingException extends RuntimeException {
  public RedisBuildingException(String message, Throwable cause) {
    super(message, cause);
  }

  public RedisBuildingException(String message) {
    super(message);
  }
}
