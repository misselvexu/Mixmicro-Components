package xyz.vopen.mixmicro.components.boot.openfeign.decoder;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Feign;
import feign.Request;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import xyz.vopen.mixmicro.components.common.ResponseEntity;
import xyz.vopen.mixmicro.components.exception.defined.MixmicroInvokeException;
import xyz.vopen.mixmicro.kits.lang.NonNull;

import java.lang.reflect.Constructor;

import static feign.FeignException.errorStatus;
import static xyz.vopen.mixmicro.components.exception.defined.MixmicroInvokeException.build;

/**
 * {@link OpenFeignInvokeErrorDecoder}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/5
 */
public class OpenFeignInvokeErrorDecoder implements ErrorDecoder, EnvironmentAware {

  private Environment environment;

  private static final String FEIGN_HYSTRIX_CONFIG = "feign.hystrix.enabled";

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

    boolean enabled = Boolean.parseBoolean(environment.getProperty(FEIGN_HYSTRIX_CONFIG, "false"));

    if (response.status() >= 400 && response.status() <= 499) {
      MixmicroInvokeException exception = buildException(response, true);
      return enabled ? new HystrixBadRequestException("E4: Client Service Exception", exception) : exception;
    }

    if (response.status() >= 500 && response.status() <= 599) {
      MixmicroInvokeException exception = buildException(response, false);
      return enabled ? new HystrixBadRequestException("E5: Remote Service Exception", exception) : exception;
    }

    // DEFAULT ERROR
    return errorStatus(methodKey, response);
  }

  @SuppressWarnings("unchecked")
  private MixmicroInvokeException buildException(Response response, boolean isClientSide) {
    try {
      Request request = response.request();

      byte[] content = IOUtils.toByteArray(response.body().asInputStream());
      ResponseEntity<String> entity = ResponseEntity.decode(content, ResponseEntity.class);

      /*
      // server error
      if(!isClientSide) {
        ResponseEntity.ExceptionMetadata metadata = entity.getEma();
        if(metadata != null) {
          String className = metadata.getClassName();
          Class<?> clazz = Class.forName(className);
          if (clazz.isAssignableFrom(MixmicroException.class)) {
            return build(new MixmicroInvokeException(response.status(), new RuntimeException(metadata.getDetailMessage())), true);
          }
        }
      }
      */

      if (!isClientSide) {
        ResponseEntity.ExceptionMetadata metadata = entity.getEma();
        if(metadata != null) {
          String className = metadata.getClassName();
          Class<?> clazz = Class.forName(className);
          if(clazz.isAssignableFrom(Throwable.class)) {
            Constructor<?> constructor = clazz.getConstructor(String.class);
            Object o = constructor.newInstance(metadata.getDetailMessage());
            throw new MixmicroInvokeException(response.status(), entity.getMessage(), (Throwable) o);
          }
        }
      }

      return build(new MixmicroInvokeException(response.status(), entity.getMessage(), entity), isClientSide);

    } catch (Exception e) {
      return new MixmicroInvokeException(response.status(), response.reason());
    }
  }

  @Override
  public void setEnvironment(@NonNull Environment environment) {
    this.environment = environment;
  }
}
