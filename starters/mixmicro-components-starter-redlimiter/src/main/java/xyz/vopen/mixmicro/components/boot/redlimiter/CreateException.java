package xyz.vopen.mixmicro.components.boot.redlimiter;

/**
 * {@link CreateException}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2021/5/21
 */
public class CreateException extends RuntimeException {

  public CreateException() {}

  public CreateException(String message) {
    super(message);
  }

  public CreateException(Throwable cause) {
    super(cause);
  }

  public CreateException(String message, Throwable cause) {
    super(message, cause);
  }
}
