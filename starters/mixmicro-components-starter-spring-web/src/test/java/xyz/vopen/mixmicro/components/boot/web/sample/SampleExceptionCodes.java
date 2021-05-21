package xyz.vopen.mixmicro.components.boot.web.sample;

import xyz.vopen.mixmicro.components.exception.ErrorCode;
import xyz.vopen.mixmicro.components.exception.HttpStatus;

/**
 * {@link SampleExceptionCodes}
 *
 * <p>Class SampleExceptionCodes Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2021/5/20
 */
public enum SampleExceptionCodes implements ErrorCode {

  ERR1(100001, "错误信息", HttpStatus.OK),
  ;

  private final int code;

  private final String message;

  private final HttpStatus httpStatus;

  SampleExceptionCodes(int code, String message, HttpStatus httpStatus) {
    this.code = code;
    this.message = message;
    this.httpStatus = httpStatus;
  }

  /**
   * Error Code
   *
   * @return Return the integer value of this status code.
   */
  @Override
  public int code() {
    return this.code;
  }

  /**
   * Reason Phrase
   *
   * @return Return the reason phrase of this status code.
   */
  @Override
  public String reasonPhrase() {
    return this.message;
  }

  /**
   * Http Status
   *
   * @return Return the status of the http request
   */
  @Override
  public HttpStatus httpStatus() {
    return this.httpStatus;
  }
}
