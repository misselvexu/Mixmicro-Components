package xyz.vopen.mixmicro.components.mongo.client.query;

/**
 * Error during update.
 *
 * @author ScottHernandez
 */
public class UpdateException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a UpdateException with a message and a cause
   *
   * @param message the message to record
   */
  public UpdateException(final String message) {
    super(message);
  }

  /**
   * Creates a UpdateException with a message and a cause
   *
   * @param message the message to record
   * @param cause the underlying cause
   */
  public UpdateException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
