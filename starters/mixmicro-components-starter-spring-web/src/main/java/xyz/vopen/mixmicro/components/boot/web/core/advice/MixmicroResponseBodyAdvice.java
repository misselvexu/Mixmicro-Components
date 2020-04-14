package xyz.vopen.mixmicro.components.boot.web.core.advice;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import xyz.vopen.mixmicro.components.common.ResponseEntity;
import xyz.vopen.mixmicro.components.common.rest.MixmicroResponseBody;
import xyz.vopen.mixmicro.components.exception.EntityBody;
import xyz.vopen.mixmicro.components.exception.defined.MixmicroException;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static xyz.vopen.mixmicro.components.common.MixmicroConstants.MIXMICRO_SERVICE_INVOKE_HEADER;

/**
 * {@link MixmicroResponseBodyAdvice}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/4
 */
@RestControllerAdvice
public class MixmicroResponseBodyAdvice extends AbstractAdvice implements ResponseBodyAdvice<Object> {

  private static final Logger log = LoggerFactory.getLogger(MixmicroResponseBodyAdvice.class);

  /**
   * Whether this component supports the given controller method return type and the selected {@code
   * HttpMessageConverter} type.
   *
   * @param returnType the return type
   * @param converterType the selected converter type
   * @return {@code true} if {@link #beforeBodyWrite} should be invoked; {@code false} otherwise
   */
  @Override
  public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  /**
   * Invoked after an {@code HttpMessageConverter} is selected and just before
   * its write method is invoked.
   *
   * @param body                  the body to be written
   * @param returnType            the return type of the controller method
   * @param selectedContentType   the content type selected through content negotiation
   * @param selectedConverterType the converter type selected to write to the response
   * @param request               the current request
   * @param response              the current response
   * @return the body that was passed in or a modified (possibly new) instance
   */
  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

    if(returnType.hasMethodAnnotation(MixmicroResponseBody.class)) {
      MixmicroResponseBody mixmicroResponseBody = returnType.getMethodAnnotation(MixmicroResponseBody.class);
      if(mixmicroResponseBody != null) {
        if(mixmicroResponseBody.direct()) {
          if (selectedConverterType.isAssignableFrom(FastJsonHttpMessageConverter.class)
              || selectedConverterType.isAssignableFrom(AbstractJackson2HttpMessageConverter.class)) {

            throw new MixmicroException("if you annotate method with annotation [@MixmicroResponseBody(direct = true)] ," +
                "not supported produces with media-type [application/json] .");
          } else {
            // direct model
            response.getHeaders().add(CONTENT_TYPE, TEXT_PLAIN_VALUE);
            return body;
          }

        } else {
          response.getHeaders().add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
          return ResponseEntity.ok(getProperties().getResponse().getDefaultSuccessResponseCode(), body).toString();
        }
      }
    } else {
      if(selectedConverterType.isAssignableFrom(StringHttpMessageConverter.class)) {
        response.getHeaders().add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        return ResponseEntity.ok(getProperties().getResponse().getDefaultSuccessResponseCode(), body).toString();
      }
    }

    if(request.getHeaders().containsKey(MIXMICRO_SERVICE_INVOKE_HEADER)) {
      if(log.isDebugEnabled()) {
        log.debug("[Request] from openfeign client , req: {}", request);
      }
      return body;
    }

    if(body instanceof ResponseEntity) {
      return body;
    }

    if(body instanceof EntityBody) {
      //noinspection unchecked
      return ResponseEntity.fail(getProperties().getException().getDefaultExceptionResponseCode(), ((EntityBody<?, String>) body).getMessage());
    }

    // RETURN
    return ResponseEntity.ok(getProperties().getResponse().getDefaultSuccessResponseCode(), body);
  }
}
