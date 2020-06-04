package xyz.vopen.mixmicro.components.enhance.schedule.repository;

import java.sql.SQLException;

public class IntegrityConstraintViolation extends SQLRuntimeException {
  public IntegrityConstraintViolation(SQLException ex) {
    super(ex);
  }
}
