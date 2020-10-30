package xyz.vopen.mixmicro.components.boot.httpclient.exception;

public class MixHttpClientIOException extends MixHttpClientException {

  public MixHttpClientIOException(String message, Throwable cause) {
    super(message, cause);
  }

  public MixHttpClientIOException(String message) {
    super(message);
  }
}
