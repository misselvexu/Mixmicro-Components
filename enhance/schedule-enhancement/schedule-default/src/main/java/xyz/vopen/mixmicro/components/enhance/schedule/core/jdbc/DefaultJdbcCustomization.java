package xyz.vopen.mixmicro.components.enhance.schedule.core.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

public class DefaultJdbcCustomization implements JdbcCustomization {

  @Override
  public void setInstant(PreparedStatement p, int index, Instant value) throws SQLException {
    p.setTimestamp(index, value != null ? Timestamp.from(value) : null);
  }

  @Override
  public Instant getInstant(ResultSet rs, String columnName) throws SQLException {
    return Optional.ofNullable(rs.getTimestamp(columnName)).map(Timestamp::toInstant).orElse(null);
  }
}
