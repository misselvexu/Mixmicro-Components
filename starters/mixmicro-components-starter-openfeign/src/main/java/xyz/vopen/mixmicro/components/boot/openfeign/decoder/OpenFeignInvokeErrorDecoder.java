package xyz.vopen.mixmicro.components.boot.openfeign.decoder;

import feign.Feign;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import xyz.vopen.mixmicro.components.exception.defined.MixmicroInvokeClientException;
import xyz.vopen.mixmicro.components.exception.defined.MixmicroInvokeServerException;

import static feign.FeignException.errorStatus;

/**
 * {@link OpenFeignInvokeErrorDecoder}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/5
 */
public class OpenFeignInvokeErrorDecoder implements ErrorDecoder {

  /**
   * Implement this method in order to decode an HTTP {@link Response} when {@link
   * Response#status()} is not in the 2xx range. Please raise application-specific exceptions where
   * possible. If your exception is retryable, wrap or subclass {@link RetryableException}
   *
   * @param methodKey {@link Feign#configKey} of the java method that invoked the request. ex.
   *     {@code IAM#getUser()}
   * @param response HTTP response where {@link Response#status() status} is greater than or equal
   *     to {@code 300}.
   * @return Exception IOException, if there was a network error reading the response or an
   *     application-specific exception decoded by the implementation. If the throwable is
   *     retryable, it should be wrapped, or a subtype of {@link RetryableException}
   */
  @Override
  public Exception decode(String methodKey, Response response) {

    if (response.status() >= 400 && response.status() <= 499) {
      return new MixmicroInvokeClientException(response.status(), response.reason());
    }

    if (response.status() >= 500 && response.status() <= 599) {
      return new MixmicroInvokeServerException(response.status(), response.reason());
    }

    // DEFAULT ERROR
    return errorStatus(methodKey, response);
  }
}
