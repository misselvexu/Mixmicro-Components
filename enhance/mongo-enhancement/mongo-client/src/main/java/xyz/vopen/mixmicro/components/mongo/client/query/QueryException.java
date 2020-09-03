package xyz.vopen.mixmicro.components.mongo.client.query;

/**
 * Error during query.
 *
 * @author ScottHernandez
 */
public class QueryException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a QueryException with a message
   *
   * @param message the message to record
   */
  public QueryException(final String message) {
    super(message);
  }

  /**
   * Creates a QueryException with a message and a cause
   *
   * @param message the message to record
   * @param cause the underlying cause
   */
  public QueryException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
