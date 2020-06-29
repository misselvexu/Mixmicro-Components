package xyz.vopen.mixmicro.components.circuitbreaker.v2.exception;

import lombok.Getter;

/**
 * @author siran.yao
 * @date 2020/6/29 21:13
 */
public class MixmicroCircuitBreakerDirectThrowException extends MixmicroCircuitBreakerException {

  @Getter private RuntimeException exception;

  /**
   * Constructs a new runtime exception with the specified detail message. The cause is not
   * initialized, and may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public MixmicroCircuitBreakerDirectThrowException(String message, RuntimeException exception) {
    super(message);
    this.exception = exception;
  }
}
