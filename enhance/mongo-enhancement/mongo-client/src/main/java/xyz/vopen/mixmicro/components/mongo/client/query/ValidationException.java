package xyz.vopen.mixmicro.components.mongo.client.query;

/**
 * Error during validation.
 *
 * @author ScottHernandez
 */
public class ValidationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a ValidationException with a message and a cause
   *
   * @param message the message to record
   */
  public ValidationException(final String message) {
    super(message);
  }

  /**
   * Creates a ValidationException with a message and a cause
   *
   * @param message the message to record
   * @param cause the underlying cause
   */
  public ValidationException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
