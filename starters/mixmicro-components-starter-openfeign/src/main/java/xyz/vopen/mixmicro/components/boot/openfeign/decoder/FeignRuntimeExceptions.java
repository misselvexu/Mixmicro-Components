package xyz.vopen.mixmicro.components.boot.openfeign.decoder;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.common.ExceptionResponse;
import xyz.vopen.mixmicro.components.common.ResponseEntity;
import xyz.vopen.mixmicro.components.exception.defined.MixmicroInvokeServerException;
import xyz.vopen.mixmicro.kits.lang.NonNull;

import java.util.Optional;

/**
 * {@link FeignRuntimeExceptions}
 *
 * <p>Class FeignRuntimeExceptions Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2021/5/19
 */
public final class FeignRuntimeExceptions {

  private static final Logger log = LoggerFactory.getLogger(FeignRuntimeExceptions.class);

  /**
   * Parse feign call exceptions, get business exception status code and response body
   *
   * <p>Usage:
   *
   * <pre>
   * try {
   *     // Feign Remote Call Object
   *     DemoFeignClient client = new DemoFeignClient();
   *
   *     // Feign Remote Method Call
   *     Object result = client.invokeRemoteServer(param1, param2, param3);
   *
   *     // ....
   *
   *  } catch(Exception e) {
   *
   *     // Parsing call exceptions
   *     Optional&lt;ExceptionResponse&lt;?>>optional = FeignRuntimeExceptions.resolving(e);
   *
   *     if (optional.isPresent()) {
   *
   *         // Get call exception code
   *         int bizCode = optional.get().getCode();
   *
   *         // Exception description information
   *         String bizMessage = optional.get().getMessage();
   *
   *         // Judgment exception handling
   *         switch (bizCode) {
   *
   *         case 1001:
   *             // .......
   *             break;
   *         case 1002:
   *             // .......
   *             break;
   *         default:
   *             // .......
   *             break;
   *
   *         }
   *     }
   *  }
   * </pre>
   *
   * @param cause Intercepted feign client calls, caught exception objects
   * @return (optional) Server-side business exception codes and response bodies
   */
  @SuppressWarnings("DuplicatedCode")
  public static Optional<ExceptionResponse<?>> resolving(@NonNull Exception cause) {

    //noinspection ConstantConditions
    if (cause == null) {
      return Optional.empty();
    }

    try {

      if(cause instanceof HystrixBadRequestException) {

        HystrixBadRequestException hbre = (HystrixBadRequestException) cause;

        Throwable throwable = hbre.getCause();

        // decode feign invoke server exception condition
        if (throwable instanceof MixmicroInvokeServerException) {

          MixmicroInvokeServerException ise = (MixmicroInvokeServerException) throwable;
          Object response = ise.getResponse();
          if (response instanceof ResponseEntity<?>) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) response;
            log.info(
                "[FEIGN INVOKE EXCEPTION] http state code : {}, biz code : {}, biz exception : {}",
                ise.getHttpCode(),
                responseEntity.getCode(),
                responseEntity.getMessage());

            return Optional.of(
                ExceptionResponse.builder()
                    .code(responseEntity.getCode())
                    .message(responseEntity.getMessage())
                    .data(responseEntity.getData())
                    .timestamp(responseEntity.getTimestamp())
                    .build());
          }
        }

      } else {

        // decode feign invoke server exception condition
        if (cause instanceof MixmicroInvokeServerException) {

          MixmicroInvokeServerException ise = (MixmicroInvokeServerException) cause;
          Object response = ise.getResponse();
          if (response instanceof ResponseEntity<?>) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) response;
            log.info(
                "[FEIGN INVOKE EXCEPTION] http state code : {}, biz code : {}, biz exception : {}",
                ise.getHttpCode(),
                responseEntity.getCode(),
                responseEntity.getMessage());

            return Optional.of(
                ExceptionResponse.builder()
                    .code(responseEntity.getCode())
                    .message(responseEntity.getMessage())
                    .data(responseEntity.getData())
                    .timestamp(responseEntity.getTimestamp())
                    .build());
          }
        }
      }

    } catch (Exception e) {
      log.warn("[FEIGN INVOKE EXCEPTION] remote exception resolve failed", e);
    }

    // DEFAULT
    return Optional.empty();
  }
}
