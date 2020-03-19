package xyz.vopen.mixmicro.components.exception.defined;

import lombok.Builder;
import xyz.vopen.mixmicro.components.exception.HttpStatus;

import static xyz.vopen.mixmicro.components.exception.HttpStatus.OK;

/**
 * {@link CompatibleMixmicroException} 基于异常的响应结构处理，兼容老业务习惯返回
 *
 * <pre>
 *    - 200 (成功,通用的成功状态码) {@link HttpStatus#OK}
 *    - 400 (前端错误,如参数错误等) {@link HttpStatus#BAD_REQUEST}
 *    - 500 (后端错误,如用户不存在,远程调用错误等) {@link HttpStatus#INTERNAL_SERVER_ERROR}
 *
 *    不需要前端做特别处理的都用以上三种状态码
 *    对于特定环境的状态码需要自己添加, 2xx 开头的都是成功状态码(如,登录时,需要绑定手机号,这样的状态码也是成功的,所以需要各个业务组自己设置 2xx 的状态码来返回)
 *    4xx 是来描述前端错误的特定的错误码,
 *    5xx 是来描述后端错误的特定的错误码.
 *
 *    <b><font style="color:red;">强调,不需要前端做特殊处理的都仅使用 200,400,500 三种状态码</font></b>
 * </pre>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/16
 */
@SuppressWarnings("unused")
public class CompatibleMixmicroException extends MixmicroException {

  /**
   * Biz Process Result return With {@link HttpStatus}
   *
   * @see HttpStatus
   */
  @Builder.Default private HttpStatus httpStatus = OK;

  /**
   * 业务处理错误识别代码，默认：-1
   *
   * <p>
   */
  @Builder.Default private int code = -1;

  public HttpStatus httpStatus() {
    return this.httpStatus;
  }

  public int httpStatusCode() {
    return this.httpStatus.code();
  }

  public int code() {
    return this.code;
  }

  // Constructors Defined.

  /**
   * Constructs a new exception with the specified detail message. The cause is not initialized, and
   * may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param httpStatus Http Request & Response Status Code
   * @param code biz error code
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public CompatibleMixmicroException(HttpStatus httpStatus, int code, String message) {
    super(message);
    this.httpStatus = httpStatus;
    this.code = code;
  }

  /**
   * Constructs a new exception with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated in this exception's detail message.
   *
   * @param httpStatus Http Request & Response Status Code
   * @param code biz error code
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method).
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or
   *     unknown.)
   * @since 1.4
   */
  @Builder
  public CompatibleMixmicroException(
      HttpStatus httpStatus, int code, String message, Throwable cause) {
    super(message, cause);
    this.httpStatus = httpStatus;
    this.code = code;

    // check
    if (httpStatus == null) this.httpStatus = OK;
  }

  /**
   * Constructs a new runtime exception with the specified cause and a detail message of
   * <tt>(cause==null ? null : cause.toString())</tt> (which typically contains the class and detail
   * message of <tt>cause</tt>). This constructor is useful for runtime exceptions that are little
   * more than wrappers for other throwables.
   *
   * @param httpStatus Http Request & Response Status Code
   * @param code biz error code
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or
   *     unknown.)
   * @since 1.4
   */
  public CompatibleMixmicroException(HttpStatus httpStatus, int code, Throwable cause) {
    super(cause);
    this.httpStatus = httpStatus;
    this.code = code;
  }
}
