package xyz.vopen.mixmicro.components.common;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import xyz.vopen.mixmicro.kits.annotation.JustForTest;
import xyz.vopen.mixmicro.kits.jackson.JacksonDateFormat;

import java.util.Date;

import static xyz.vopen.mixmicro.components.common.MixmicroConstants.MIXMICRO_FULL_DATE_FORMATTER;

/**
 * {@link ResponseEntity}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/4
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEntity<T> extends SerializableBean {

  public ResponseEntity() {}

  /**
   * Biz request process status code
   *
   * <p>default: -1
   */
  @Builder.Default private int code = -1;

  /**
   * Response message detail.
   *
   * <p>
   */
  @Builder.Default private String message;

  /**
   * Response body serialized content.
   *
   * <p>
   */
  private T data;

  /**
   * Response Entity return timestamp.
   *
   * <p>
   */
  @Builder.Default @JacksonDateFormat
  @JSONField(format = MIXMICRO_FULL_DATE_FORMATTER)
  private Date timestamp = new Date();

  // exception de-Serialize metadata

  @JustForTest private ExceptionMetadata ema;


  /**
   * Defined response body class type.
   *
   * <p>
   */
  private transient Class<?> bodyClassType;

  // Defined normal response

  /**
   * Empty Response Bean , without <code>code</code> and <code>message</code>
   *
   * <pre>
   *   ResponseEntity.<Void>builder().build();
   * </pre>
   *
   * <p>
   */
  public static final ResponseEntity<Void> EMPTY_RESPONSE = ResponseEntity.<Void>builder().build();

  /**
   * Build success body response
   *
   * @param body response content
   * @return success instance of {@link ResponseEntity}
   */
  public static ResponseEntity<Object> ok(Object body) {
    return ok(0, body);
  }

  public static <T> ResponseEntity<T> ok(Class<T> clazz, T body) {
    return ok(clazz, 0, body);
  }

  public static ResponseEntity<Object> ok(int code, Object body) {
    return ok(code, body, null);
  }

  public static <T> ResponseEntity<T> ok(Class<T> clazz, int code, T body) {
    return ok(clazz, code, body, null);
  }

  public static ResponseEntity<Object> ok(int code, Object body, String message) {
    return ResponseEntity.builder().code(code).data(body).message(message).build();
  }

  public static <T> ResponseEntity<T> ok(Class<T> clazz, int code, T body, String message) {
    return ResponseEntity.<T>builder().bodyClassType(clazz).code(code).data(body).message(message).build();
  }

  /**
   * Build fail response
   *
   * @param code error code
   * @param message fail message
   * @return fail instance of {@link ResponseEntity}
   */
  public static ResponseEntity<Object> fail(int code, String message) {
    return ResponseEntity.builder().code(code).message(message).build();
  }

  public static <T> ResponseEntity<T> fail(Class<T> clazz, int code, String message) {
    return ResponseEntity.<T>builder().bodyClassType(clazz).code(code).message(message).build();
  }

  public static ResponseEntity<Object> fail(int code, Object body, String message) {
    return ResponseEntity.builder().code(code).data(body).message(message).build();
  }

  public static <T> ResponseEntity<T> fail(Class<T> clazz, int code, T body, String message) {
    return ResponseEntity.<T>builder().bodyClassType(clazz).code(code).data(body).message(message).build();
  }


  @Getter
  @Setter
  @Builder
  @JustForTest
  @AllArgsConstructor
  public static class ExceptionMetadata extends SerializableBean {

    private String className;

    private String detailMessage;

    private transient Throwable exception;

  }
}
