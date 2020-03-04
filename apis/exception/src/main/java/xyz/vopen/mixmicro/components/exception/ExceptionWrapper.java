package xyz.vopen.mixmicro.components.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Exception Wrapper
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 * @deprecated use ResponseEntity<T> instead of, will removed next release version.
 * @see EntityBody
 */
@Getter
@Setter
@Builder
public class ExceptionWrapper<E> {

  /**
   * Exception Native Class Type
   *
   * <p>
   */
  private Class<E> type;

  /**
   * Exception Trace Message
   *
   * <p>
   */
  private String message;

  /**
   * Exception Code
   *
   * <p>
   */
  private int code;
}
