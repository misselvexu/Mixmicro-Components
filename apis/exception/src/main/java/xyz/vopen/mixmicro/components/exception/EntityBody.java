package xyz.vopen.mixmicro.components.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.vopen.mixmicro.kits.jackson.JacksonDateFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Response Entity Body
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 22/10/2018.
 * @deprecated use <code>ResponseEntity<T></code> instead of, will removed next release version.
 */
@Getter
@Setter
@Deprecated
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class EntityBody<T, M> implements Serializable {

  private static final long serialVersionUID = 2515730780312554596L;

  @JacksonDateFormat private Date timestamp;

  private Status status;

  private T data;

  private M message;

  private ExceptionWrapper<? extends Exception> exception;

  @Builder
  public EntityBody(Status status, Date timestamp, M message, ExceptionWrapper<? extends Exception> exception, T data) {
    this.timestamp = timestamp;
    if (timestamp == null) {
      this.timestamp = new Date();
    }
    this.message = message;
    this.exception = exception;
    this.data = data;

    this.status = status;
    if (this.status == null) {
      this.status = Status.SUCCESS;
    }
  }

  public static EntityBody<Void, String> exception(Throwable e) {
    if (e != null) {
      return EntityBody.<Void, String>builder()
          .status(Status.EXCEPTION)
          .message(e.getMessage())
          .build();
    } else {
      return EntityBody.<Void, String>builder()
          .status(Status.EXCEPTION)
          .message("service is busy.")
          .build();
    }
  }

  /**
   * Entity Body Status
   *
   * @since 2.2.0-RC2
   */
  public enum Status {
    /** Success Flag */
    SUCCESS,

    FAILED,

    EXCEPTION
  }
}
