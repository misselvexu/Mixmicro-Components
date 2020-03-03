package xyz.vopen.mixmicro.components.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Exception Wrapper
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
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
