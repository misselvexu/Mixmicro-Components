package xyz.vopen.mixmicro.components.boot.httpclient.exception;

public class RetrofitIOException extends RetrofitException {

  public RetrofitIOException(String message, Throwable cause) {
    super(message, cause);
  }

  public RetrofitIOException(String message) {
    super(message);
  }
}
