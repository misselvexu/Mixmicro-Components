package xyz.vopen.mixmicro.components.enhance.aksk.core.signatures;

public class AuthenticationException extends RuntimeException {

  public AuthenticationException() {}

  public AuthenticationException(final String message) {
    super(message);
  }

  public AuthenticationException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
