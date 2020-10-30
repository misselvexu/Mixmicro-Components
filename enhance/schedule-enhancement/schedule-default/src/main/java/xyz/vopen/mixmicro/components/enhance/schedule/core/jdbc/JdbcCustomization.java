package xyz.vopen.mixmicro.components.enhance.schedule.core.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;

public interface JdbcCustomization {

  void setInstant(PreparedStatement preparedStatement, int index, Instant value)
      throws SQLException;

  Instant getInstant(ResultSet rs, String columnName) throws SQLException;
}
