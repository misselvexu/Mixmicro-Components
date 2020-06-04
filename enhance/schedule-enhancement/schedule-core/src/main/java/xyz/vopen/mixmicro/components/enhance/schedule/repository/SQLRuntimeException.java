package xyz.vopen.mixmicro.components.enhance.schedule.repository;

import java.sql.SQLException;

public class SQLRuntimeException extends RuntimeException {

  public SQLRuntimeException() {
    super();
  }

  public SQLRuntimeException(String message) {
    super(message);
  }

  public SQLRuntimeException(Throwable ex) {
    super(ex);
  }

  public SQLRuntimeException(String message, SQLException cause) {
    super(message, cause);
  }
}
