package xyz.vopen.mixmicro.components.boot.httpclient.exception;

public class ServiceInstanceChooseException extends RuntimeException {

  public ServiceInstanceChooseException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceInstanceChooseException(String message) {
    super(message);
  }

  public ServiceInstanceChooseException(Throwable cause) {
    super(cause);
  }
}
