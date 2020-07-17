package xyz.vopen.mixmicro.components.enhance.mail.service.exception;

public class CannotDeserializeException extends RuntimeException {

  public CannotDeserializeException() {
    super();
  }

  public CannotDeserializeException(final String message) {
    super(message);
  }

  public CannotDeserializeException(final Throwable cause) {
    super(cause);
  }

  public CannotDeserializeException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
