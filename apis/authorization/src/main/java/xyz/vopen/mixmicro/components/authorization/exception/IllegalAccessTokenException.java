package xyz.vopen.mixmicro.components.authorization.exception;

import lombok.Getter;

import java.util.Date;

/**
 * {@link ExpiredAccessTokenException}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-10.
 */
public class ExpiredAccessTokenException extends AuthorizationException {

  /**
   * Exported Expired Time
   *
   * <p>
   */
  @Getter private Date expiredTime;

  /**
   * Constructs a new exception with the specified detail message. The cause is not initialized, and
   * may subsequently be initialized by a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for later retrieval by the
   *     {@link #getMessage()} method.
   */
  public ExpiredAccessTokenException(String message, Date expiredTime) {
    super(message);
    this.expiredTime = expiredTime;
  }

  /**
   * Constructs a new exception with the specified detail message and cause.
   *
   * <p>Note that the detail message associated with {@code cause} is <i>not</i> automatically
   * incorporated in this exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link
   *     #getMessage()} method).
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).
   *     (A <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or
   *     unknown.)
   * @since 1.4
   */
  public ExpiredAccessTokenException(String message, Date expiredTime, Throwable cause) {
    super(message, cause);
    this.expiredTime = expiredTime;
  }
}
