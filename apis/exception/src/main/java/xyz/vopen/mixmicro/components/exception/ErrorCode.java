package xyz.vopen.mixmicro.components.exception;

/**
 * {@link ErrorCode}
 *
 * <p>Step1. Defined your {@link ErrorCode}
 *
 * <pre>
 *
 *   public enum SampleErrorCode implements ErrorCode {
 *
 *     // Defined Your Error Codes .
 *     ERROR_1(1, "error 1", HttpStatus.OK),
 *     ERROR_2(2, "error 2", HttpStatus.INTERNAL_SERVER_ERROR);
 *
 *     // Normal Properties Fields . (No change required)
 *     private int code;
 *
 *     private String reasonPhrase;
 *
 *     private HttpStatus httpStatus;
 *
 *     SampleErrorCode(int code, String reasonPhrase, HttpStatus httpStatus) {
 *       this.code = code;
 *       this.reasonPhrase = reasonPhrase;
 *       this.httpStatus = httpStatus;
 *     }
 *
 *     public int code() {
 *       return this.code;
 *     }
 *
 *     public String reasonPhrase() {
 *       return this.reasonPhrase;
 *     }
 *
 *     public HttpStatus httpStatus() {
 *       return this.httpStatus;
 *     }
 *   }
 * </pre>
 *
 * <p>Step2. Usage
 *
 * <pre>
 *
 *   // throw you defined exception with sub-class {@link xyz.vopen.mixmicro.components.exception.defined.CompatibleMixmicroException}
 *   throw new CompatibleMixmicroException(SampleErrorCode.ERROR_1);
 *
 * </pre>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/19
 */
public interface ErrorCode {

  /**
   * Error Code
   *
   * @return Return the integer value of this status code.
   */
  int code();

  /**
   * Reason Phrase
   *
   * @return Return the reason phrase of this status code.
   */
  String reasonPhrase();

  /**
   * Http Status
   *
   * @return Return the status of the http request
   */
  default HttpStatus httpStatus() {
    return HttpStatus.OK;
  }
}
