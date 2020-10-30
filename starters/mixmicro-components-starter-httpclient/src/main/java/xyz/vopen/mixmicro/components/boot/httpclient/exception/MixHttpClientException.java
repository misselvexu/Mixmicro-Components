package xyz.vopen.mixmicro.components.boot.httpclient.exception;

import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.StringUtils;
import xyz.vopen.mixmicro.components.boot.httpclient.kits.MixHttpClientKit;

import java.io.IOException;

public class MixHttpClientException extends RuntimeException {

  public MixHttpClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public MixHttpClientException(String message) {
    super(message);
  }

  public static MixHttpClientException errorStatus(Request request, Response response) {
    String msg = String.format("invalid Response! request=%s, response=%s", request, response);
    try {
      String responseBody = MixHttpClientKit.readResponseBody(response);
      if (StringUtils.hasText(responseBody)) {
        msg += ", body=" + responseBody;
      }
    } catch (ReadResponseBodyException e) {
      throw new MixHttpClientException(
          String.format("read ResponseBody error! request=%s, response=%s", request, response), e);
    }
    return new MixHttpClientException(msg);
  }

  public static MixHttpClientException errorExecuting(Request request, IOException cause) {
    return new MixHttpClientIOException(cause.getMessage() + ", request=" + request, cause);
  }

  public static MixHttpClientException errorUnknown(Request request, Exception cause) {
    if (cause instanceof MixHttpClientException) {
      return (MixHttpClientException) cause;
    }
    return new MixHttpClientException(cause.getMessage() + ", request=" + request, cause);
  }
}
