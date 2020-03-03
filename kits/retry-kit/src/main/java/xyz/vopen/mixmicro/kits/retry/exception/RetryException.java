package xyz.vopen.mixmicro.kits.retry.exception;

public class RetryException extends RuntimeException {

  public RetryException(String message, Throwable cause) {
    super(message, cause);
  }

  public RetryException(String message) {
    super(message);
  }
}
