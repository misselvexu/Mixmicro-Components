package xyz.vopen.mixmicro.components.boot.httpclient;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ErrorDecoderInterceptor implements Interceptor {

  private final MixHttpClientErrorDecoder httpClientErrorDecoder;

  private static Map<MixHttpClientErrorDecoder, ErrorDecoderInterceptor> cache = new HashMap<>(4);

  private ErrorDecoderInterceptor(MixHttpClientErrorDecoder httpClientErrorDecoder) {
    this.httpClientErrorDecoder = httpClientErrorDecoder;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    boolean decoded = false;
    try {
      Response response = chain.proceed(request);
      if (httpClientErrorDecoder == null) {
        return response;
      }
      decoded = true;
      Exception exception = httpClientErrorDecoder.invalidRespDecode(request, response);
      if (exception == null) {
        return response;
      }
      throw exception;
    } catch (IOException e) {
      if (decoded) {
        throw e;
      }
      throw httpClientErrorDecoder.ioExceptionDecode(request, e);
    } catch (Exception e) {
      if (decoded && e instanceof RuntimeException) {
        throw (RuntimeException) e;
      }
      throw httpClientErrorDecoder.exceptionDecode(request, e);
    }
  }

  public static ErrorDecoderInterceptor create(MixHttpClientErrorDecoder httpClientErrorDecoder) {
    ErrorDecoderInterceptor interceptor = cache.get(httpClientErrorDecoder);
    if (interceptor != null) {
      return interceptor;
    }
    interceptor = new ErrorDecoderInterceptor(httpClientErrorDecoder);
    cache.put(httpClientErrorDecoder, interceptor);
    return interceptor;
  }
}
