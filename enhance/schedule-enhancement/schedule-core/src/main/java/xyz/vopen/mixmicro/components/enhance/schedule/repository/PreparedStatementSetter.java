package xyz.vopen.mixmicro.components.enhance.schedule.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementSetter {

  void setParameters(PreparedStatement preparedStatement) throws SQLException;

  PreparedStatementSetter NOOP =
      new PreparedStatementSetter() {
        @Override
        public void setParameters(PreparedStatement preparedStatement) throws SQLException {}
      };
}
