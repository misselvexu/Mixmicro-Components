package xyz.vopen.mixmicro.components.boot.security.exception;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
public class MixSecurityDecryptionException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public MixSecurityDecryptionException(final String message) {
    super(message);
  }

  public MixSecurityDecryptionException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
