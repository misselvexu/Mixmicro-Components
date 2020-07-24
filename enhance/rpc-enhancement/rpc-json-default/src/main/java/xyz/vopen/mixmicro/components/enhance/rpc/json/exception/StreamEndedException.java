package xyz.vopen.mixmicro.components.enhance.rpc.json.exception;

import java.io.IOException;

/**
 * {@link StreamEndedException}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@SuppressWarnings({"serial", "WeakerAccess", "unused"})
public class StreamEndedException extends IOException {

  public StreamEndedException() {
  }

  /**
   * @param message the detail message
   */
  public StreamEndedException(String message) {
    super(message);
  }

  /**
   * @param cause the cause (a null value is permitted, and indicates that the cause is nonexistent or unknown)
   */
  public StreamEndedException(Throwable cause) {
    super(cause);
  }

  /**
   * @param message the detail message
   * @param cause   the cause (a null value is permitted, and indicates that the cause is nonexistent or unknown)
   */
  public StreamEndedException(String message, Throwable cause) {
    super(message, cause);
  }

}
