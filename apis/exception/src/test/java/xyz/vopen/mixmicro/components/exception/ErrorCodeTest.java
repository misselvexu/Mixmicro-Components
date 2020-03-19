package xyz.vopen.mixmicro.components.exception;

import org.junit.Test;

import static xyz.vopen.mixmicro.components.exception.ErrorCodeTest.Sample.test;
import static xyz.vopen.mixmicro.components.exception.ErrorCodeTest.SampleErrorCode.ERROR_1;

/**
 * {@link ErrorCodeTest}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/19
 */
public class ErrorCodeTest {

  @Test
  public void code() {

    test(ERROR_1);

  }

  @Test
  public void reasonPhrase() {}

  public static class Sample {

    public static void test(ErrorCode errorCode) {
      System.out.println(errorCode.code());
      System.out.println(errorCode.reasonPhrase());
    }
  }

  public static abstract class CustomErrorCoder {

    /**
     * Error Code
     *
     * @return Return the integer value of this status code.
     */
    public abstract int code();

    /**
     * Reason Phrase
     *
     * @return Return the reason phrase of this status code.
     */
    public abstract String reasonPhrase();

  }

  public enum SampleErrorCode implements ErrorCode {

    ERROR_1(1, "error 1", HttpStatus.OK),
    ERROR_2(2, "error 2", HttpStatus.INTERNAL_SERVER_ERROR);

    private int code;

    private String reasonPhrase;

    private HttpStatus httpStatus;

    SampleErrorCode(int code, String reasonPhrase, HttpStatus httpStatus) {
      this.code = code;
      this.reasonPhrase = reasonPhrase;
      this.httpStatus = httpStatus;
    }

    @Override
    public int code() {
      return this.code;
    }

    @Override
    public String reasonPhrase() {
      return this.reasonPhrase;
    }

    @Override
    public HttpStatus httpStatus() {
      return this.httpStatus;
    }
  }
}
