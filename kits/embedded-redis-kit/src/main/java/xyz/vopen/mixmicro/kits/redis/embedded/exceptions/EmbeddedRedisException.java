package xyz.vopen.mixmicro.kits.redis.embedded.exceptions;

/**
 * {@link EmbeddedRedisException}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class EmbeddedRedisException extends RuntimeException {
  public EmbeddedRedisException(String message, Throwable cause) {
    super(message, cause);
  }

  public EmbeddedRedisException(String message) {
    super(message);
  }
}
