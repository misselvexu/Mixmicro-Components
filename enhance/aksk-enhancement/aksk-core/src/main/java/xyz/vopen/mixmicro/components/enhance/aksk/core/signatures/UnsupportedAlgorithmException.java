package xyz.vopen.mixmicro.components.enhance.aksk.core.signatures;

public class UnsupportedAlgorithmException extends AuthenticationException {

  public UnsupportedAlgorithmException(final String message) {
    super(message);
  }

  public UnsupportedAlgorithmException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
