package xyz.vopen.mixmicro.components.boot.web.core.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import xyz.vopen.mixmicro.components.boot.web.MixmicroWebConfigProperties;
import xyz.vopen.mixmicro.components.exception.EntityBody;
import xyz.vopen.mixmicro.components.exception.defined.BizException;
import xyz.vopen.mixmicro.components.exception.defined.CompatibleMixmicroException;
import xyz.vopen.mixmicro.components.exception.defined.MixmicroException;

import java.lang.reflect.Constructor;
import java.util.Optional;

/**
 * {@link MixmicroExceptionAdvice}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2019-05-30.
 */
@ControllerAdvice
public class MixmicroExceptionAdvice extends AbstractAdvice {

  private static final Logger log = LoggerFactory.getLogger(MixmicroExceptionAdvice.class);

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
    MixmicroWebConfigProperties properties = getProperties();
    Class<?> aClass = properties.getException().getHandlerClass();
    if (aClass == null) {
      return getExceptionHandlerFromBeanFactory();
    }

    try {
      Constructor<?> constructor = aClass.getConstructor();
      Object o = constructor.newInstance();
      return (xyz.vopen.mixmicro.components.boot.web.ExceptionHandler) o;
    } catch (Exception e) {
      log.warn("@ExceptionHandler extended class must default constructor .");
    }

    return getExceptionHandlerFromBeanFactory();
  }

  private Optional<ResponseEntity<?>> handlerException(Exception e) {
    xyz.vopen.mixmicro.components.boot.web.ExceptionHandler handler = getExceptionHandler();
    if (handler != null) {
      return Optional.ofNullable(handler.handlerException(e));
    }
    return Optional.empty();
  }

  private void printStackTrace(Exception e) {
    if (getProperties().getException().isPrintStackTrace()) {
      e.printStackTrace();
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
            EntityBody.<Void, String>builder()
                .message("missing request parameter(s) [" + e.getParameterName() + "]")
                .build());
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

    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
        .body(EntityBody.<Void, String>builder().message(e.getMessage()).build());
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

      return ResponseEntity.status(cme.httpStatusCode())
          .body(
              xyz.vopen.mixmicro.components.common.ResponseEntity.builder()
                  .message(cme.getMessage())
                  .code(cme.code())
                  .build());
    }

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EntityBody.exception(e));
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

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EntityBody.exception(e));
  }
}
