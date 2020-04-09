package xyz.vopen.mixmicro.components.exception.defined;

import xyz.vopen.mixmicro.components.common.ResponseEntity;

/**
 * {@link MixmicroInvokeClientException}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/5
 */
public class MixmicroInvokeClientException extends MixmicroInvokeException {

  /**
   * Constructs a new exception with the specified detail message. The cause is not initialized, and
   * may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param httpCode http state code
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public MixmicroInvokeClientException(int httpCode, String message) {
    super(httpCode, message);
  }

  /**
   * Constructs a new exception with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated in this exception's detail message.
   *
   * @param httpCode http state code
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method).
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or
   *     unknown.)
   * @since 1.4
   */
  public MixmicroInvokeClientException(int httpCode, String message, Throwable cause) {
    super(httpCode, message, cause);
  }

  /**
   * Constructs a new runtime exception with the specified cause and a detail message of
   * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the class and detail
   * message of <tt>cause</tt>). This constructor is useful for runtime exceptions that are little
   * more than wrappers for other throwables.
   *
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or
   *     unknown.)
   * @since 1.4
   */
  public MixmicroInvokeClientException(Throwable cause) {
    super(cause);
  }

  public MixmicroInvokeClientException(int httpCode, String message, ResponseEntity<?> response) {
    super(httpCode, message, response);
  }

  public MixmicroInvokeClientException(
      int httpCode, String message, ResponseEntity<?> response, Throwable cause) {
    super(httpCode, message, response, cause);
  }
}
