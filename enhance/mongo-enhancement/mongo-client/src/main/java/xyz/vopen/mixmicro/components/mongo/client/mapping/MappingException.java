package xyz.vopen.mixmicro.components.mongo.client.mapping;

public class MappingException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  /**
   * Creates an exception with a message
   *
   * @param message the message to record
   */
  public MappingException(final String message) {
    super(message);
  }

  /**
   * Creates an exception with a message and a cause
   *
   * @param message the message to record
   * @param cause the
   */
  public MappingException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
