package xyz.vopen.mixmicro.components.boot.web.core.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.vopen.mixmicro.components.boot.web.MixmicroWebConfigProperties;
import xyz.vopen.mixmicro.components.exception.defined.*;
import xyz.vopen.mixmicro.kits.StringUtils;
import xyz.vopen.mixmicro.kits.reflect.ReflectionKit;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Optional;

import static xyz.vopen.mixmicro.components.common.MixmicroConstants.MIXMICRO_SERVICE_FEIGN_INVOKE_HEADER;
import static xyz.vopen.mixmicro.components.common.MixmicroConstants.MIXMICRO_SERVICE_INVOKE_HEADER;

/**
 * {@link MixmicroExceptionAdvice}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-05-30.
 */
@ControllerAdvice
public class MixmicroExceptionAdvice extends AbstractAdvice {

  private static final Logger log = LoggerFactory.getLogger(MixmicroExceptionAdvice.class);

  private static final String HYSTRIX_EXCEPTION_CLASS_NAME = "com.netflix.hystrix.exception.HystrixBadRequestException";
  private static final String CAUSE_METHOD = "getCause";

  private xyz.vopen.mixmicro.components.boot.web.ExceptionHandler handler;

  @Nullable
  private xyz.vopen.mixmicro.components.boot.web.ExceptionHandler
      getExceptionHandlerFromBeanFactory() {
    if (beanFactory != null) {
      try{
        return beanFactory.getBean(xyz.vopen.mixmicro.components.boot.web.ExceptionHandler.class);
      } catch (Exception ignored) {
      }
    }
    return null;
  }

  @Nullable
  private xyz.vopen.mixmicro.components.boot.web.ExceptionHandler getExceptionHandler() {

    if(this.handler != null) {
      return this.handler;
    }

    MixmicroWebConfigProperties properties = getProperties();
    try{
      Class<?> aClass = properties.getException().getHandlerClass();
      if (aClass != null) {
        try {
          Constructor<?> constructor = aClass.getConstructor();
          Object o = constructor.newInstance();
          this.handler = (xyz.vopen.mixmicro.components.boot.web.ExceptionHandler) o;
        } catch (Exception e) {
          log.warn("@ExceptionHandler extended class must default constructor .");
        }
      }
    } catch (Exception ignored) {
    }

    if(this.handler == null) {
      this.handler = getExceptionHandlerFromBeanFactory();
    }

    return this.handler;
  }

  private Optional<ResponseEntity<?>> handlerException(Exception e) {
    xyz.vopen.mixmicro.components.boot.web.ExceptionHandler handler = getExceptionHandler();
    if (handler != null) {
      return Optional.ofNullable(handler.handlerException(e));
    }
    return Optional.empty();
  }

  private void printStackTrace(Exception e) {
    if(e != null) {
      // global stack config
      if (getProperties().getException().isPrintStackTrace()) {
        // check exception has custom config ?
        Map<Class<? extends Exception>, Boolean> exceptions = getProperties().getException().getSensitiveStacks();
        if (exceptions.containsKey(e.getClass())) { // true
          Boolean isPrint = exceptions.get(e.getClass());
          if(isPrint) {
            log.error(e.getMessage(), e);
          } else {
            log.error(e.getMessage());
          }
        } else {
          if (e instanceof MixmicroException) {
            if (getProperties().getException().isPrintMixmicroStackTrace()) { // true
              log.error(e.getMessage(), e);
            } else {
              log.error(e.getMessage());
            }
          } else {
            log.error(e.getMessage(), e);
          }
        }
      } else {
        log.error(e.getMessage());
      }
    }
  }

  /**
   * 缺少参数异常拦截
   *
   * @param e MissingServletRequestParameterException
   * @return 缺少返回返回值
   */
  @ExceptionHandler(value = MissingServletRequestParameterException.class)
  public ResponseEntity<?> missingServletRequestParameterException(
      MissingServletRequestParameterException e) {
    printStackTrace(e);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            xyz.vopen.mixmicro.components.common.ResponseEntity.fail(
                xyz.vopen.mixmicro.components.exception.HttpStatus.BAD_REQUEST.code(),
                "missing request parameter(s) [" + e.getParameterName() + "]"));
  }

  // === 定义自定义异常全局拦截机制

  /**
   * 全局业务Service处理异常
   *
   * @param e instance of {@link xyz.vopen.mixmicro.components.exception.defined.BizException}
   * @return 返回值
   */
  @ExceptionHandler(value = BizException.class)
  public ResponseEntity<?> bizServiceException(BizException e) {
    printStackTrace(e);

    handlerException(e);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            xyz.vopen.mixmicro.components.common.ResponseEntity.fail(
                getProperties().getException().getDefaultExceptionResponseCode(), e));
  }

  /**
   * 框架顶级异常
   *
   * @param e exception
   * @return 通用返回值
   */
  @ExceptionHandler(value = MixmicroException.class)
  public ResponseEntity<?> mixmicroException(MixmicroException e) {

    printStackTrace(e);

    Optional<ResponseEntity<?>> o = handlerException(e);
    if (o.isPresent()) {
      return o.get();
    }

    if (e instanceof CompatibleMixmicroException) {
      // COMPATIBLE: 兼容性异常
      CompatibleMixmicroException cme = (CompatibleMixmicroException) e;

      int httpStatusCode = cme.httpStatusCode();

      // Adapt Fix by @Jake : if request invoked by service feign client , revert http code with 500.
      try{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
          HttpServletRequest request = attributes.getRequest();
          // check request#header#MIXMICRO_SERVICE_INVOKE_HEADER value
          if (StringUtils.isNotBlank(request.getHeader(MIXMICRO_SERVICE_INVOKE_HEADER)) || StringUtils.isNotBlank(request.getHeader(MIXMICRO_SERVICE_FEIGN_INVOKE_HEADER))) {
            httpStatusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
          }
        }
      } catch (Exception ignored) {
      }

      return ResponseEntity.status(httpStatusCode)
          .body(
              xyz.vopen.mixmicro.components.common.ResponseEntity.builder()
                  .message(cme.getMessage())
                  // fix: carry biz custom exception data object
                  .data(cme.data())
                  .code(cme.code())
                  .build());
    }

    // features: supported service invoke service exception transport.
    if (e instanceof MixmicroInvokeServerException) {
      MixmicroInvokeServerException mise = (MixmicroInvokeServerException) e;
      HttpStatus httpStatus = HttpStatus.OK;
      // Adapt Fix by @Jake : if request invoked by service feign client , revert http code with 500.
      try{
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
          HttpServletRequest request = attributes.getRequest();
          // check request#header#MIXMICRO_SERVICE_INVOKE_HEADER value
          if (StringUtils.isNotBlank(request.getHeader(MIXMICRO_SERVICE_INVOKE_HEADER)) || StringUtils.isNotBlank(request.getHeader(MIXMICRO_SERVICE_FEIGN_INVOKE_HEADER))) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
          }
        }
      } catch (Exception ignored) {
      }
      xyz.vopen.mixmicro.components.common.ResponseEntity<?> responseEntity = (xyz.vopen.mixmicro.components.common.ResponseEntity<?>) mise.getResponse();
      return ResponseEntity.status(httpStatus)
              .body(
                      xyz.vopen.mixmicro.components.common.ResponseEntity.builder()
                              .message(mise.getMessage())
                              .code(responseEntity != null ? responseEntity.getCode() : getProperties().getException().getDefaultExceptionResponseCode())
                              .build());
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            xyz.vopen.mixmicro.components.common.ResponseEntity.fail(
                getProperties().getException().getDefaultExceptionResponseCode(), e));
  }

  // === end

  /**
   * 最底层异常拦截
   *
   * @param e exception
   * @return 通用返回值
   */
  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<?> exception(Exception e) {
    printStackTrace(e);

    Optional<ResponseEntity<?>> o = handlerException(e);
    if (o.isPresent()) {
      return o.get();
    }

    try{
      // process hystrix exception for web front
      if(org.apache.commons.lang3.StringUtils.equals(e.getClass().getName(), HYSTRIX_EXCEPTION_CLASS_NAME)) {
        Object cause = ReflectionKit.invokeMethodByName(e, CAUSE_METHOD, new Object[]{});
        if(cause instanceof MixmicroInvokeException) {
          MixmicroInvokeException ie = (MixmicroInvokeException) cause;
          Object response = ie.getResponse();
          return ResponseEntity.status(ie.getHttpCode()).body(response);
        }
      }
    } catch (Exception exception) {
      log.warn("@ExceptionHandler process hystrix exception failed, {}", exception.getMessage());
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            xyz.vopen.mixmicro.components.common.ResponseEntity.fail(
                getProperties().getException().getDefaultExceptionResponseCode(), e));
  }
}
