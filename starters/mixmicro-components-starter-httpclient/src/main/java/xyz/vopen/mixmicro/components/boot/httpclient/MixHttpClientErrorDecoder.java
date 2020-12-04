package xyz.vopen.mixmicro.components.boot.httpclient;

import okhttp3.Request;
import okhttp3.Response;
import xyz.vopen.mixmicro.components.boot.httpclient.exception.MixHttpClientException;

import java.io.IOException;

/**
 * When an exception occurs in the request or an invalid response result is received, the HTTP
 * related information is decoded into the exception, and the invalid response is determined by the
 * business itself.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public interface MixHttpClientErrorDecoder {

  /**
   * When the response is invalid, decode the HTTP information into the exception, invalid response
   * is determined by business.
   *
   * @param request request
   * @param response response
   * @return If it returns null, the processing is ignored and the processing continues with the
   *     original response.
   */
  default RuntimeException invalidRespDecode(Request request, Response response) {
    if (!response.isSuccessful()) {
      throw MixHttpClientException.errorStatus(request, response);
    }
    return null;
  }

  /**
   * When an IO exception occurs in the request, the HTTP information is decoded into the exception.
   *
   * @param request request
   * @param cause IOException
   * @return RuntimeException
   */
  default RuntimeException ioExceptionDecode(Request request, IOException cause) {
    return MixHttpClientException.errorExecuting(request, cause);
  }

  /**
   * When the request has an exception other than the IO exception, the HTTP information is decoded
   * into the exception.
   *
   * @param request request
   * @param cause Exception
   * @return RuntimeException
   */
  default RuntimeException exceptionDecode(Request request, Exception cause) {
    return MixHttpClientException.errorUnknown(request, cause);
  }
}
