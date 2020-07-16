package xyz.vopen.mixmicro.components.enhance.mail.service.exception;

public class CannotSendEmailException extends Exception {

  public CannotSendEmailException() {
    super();
  }

  public CannotSendEmailException(final String message) {
    super(message);
  }

  public CannotSendEmailException(final Throwable cause) {
    super(cause);
  }

  public CannotSendEmailException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
