package xyz.vopen.mixmicro.components.enhance.aksk.core.signatures;

public class MissingRequiredHeaderException extends AuthenticationException {

  public MissingRequiredHeaderException(final String key) {
    super(key);
  }
}
