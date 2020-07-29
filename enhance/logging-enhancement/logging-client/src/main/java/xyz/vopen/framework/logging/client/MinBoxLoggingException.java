package xyz.vopen.framework.logging.client;

import lombok.NoArgsConstructor;

/**
 * Custom runtime log exception
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@NoArgsConstructor
public class MinBoxLoggingException extends RuntimeException {
  /**
   * Constructor initializes exception object
   *
   * @param message Exception message
   */
  public MinBoxLoggingException(String message) {
    super(message);
  }

  /**
   * Constructor initializes exception object
   *
   * @param message Exception message
   * @param cause {@link Throwable} stack information
   */
  public MinBoxLoggingException(String message, Throwable cause) {
    super(message, cause);
  }
}
