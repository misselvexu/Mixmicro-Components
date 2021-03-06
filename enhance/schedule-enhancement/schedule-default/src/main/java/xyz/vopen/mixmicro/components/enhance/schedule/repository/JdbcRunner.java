package xyz.vopen.mixmicro.components.enhance.schedule.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class JdbcRunner {
  private static final Logger log = LoggerFactory.getLogger(JdbcRunner.class);
  private final DataSource dataSource;

  public JdbcRunner(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public int execute(String query, PreparedStatementSetter setParameters) {
    return execute(query, setParameters, (PreparedStatement p) -> p.getUpdateCount());
  }

  public <T> List<T> query(
      String query, PreparedStatementSetter setParameters, RowMapper<T> rowMapper) {
    return execute(query, setParameters, (PreparedStatement p) -> mapResultSet(p, rowMapper));
  }

  public <T> T query(
      String query, PreparedStatementSetter setParameters, ResultSetMapper<T> resultSetMapper) {
    return execute(query, setParameters, (PreparedStatement p) -> mapResultSet(p, resultSetMapper));
  }

  private <T> T execute(
      String query, PreparedStatementSetter setParameters, AfterExecution<T> afterExecution) {
    return withConnection(
        c -> {
          PreparedStatement preparedStatement = null;
          try {

            try {
              preparedStatement = c.prepareStatement(query);
            } catch (SQLException e) {
              throw new SQLRuntimeException("Error when preparing statement.", e);
            }

            try {
              log.trace("Setting parameters of prepared statement.");
              setParameters.setParameters(preparedStatement);
            } catch (SQLException e) {
              throw new SQLRuntimeException(e);
            }
            try {
              log.trace("Executing prepared statement");
              preparedStatement.execute();
              T returnValue = afterExecution.doAfterExecution(preparedStatement);

              commitIfNecessary(c);

              return returnValue;
            } catch (SQLException e) {
              rollbackIfNecessary(c);
              throw translateException(e);
            }

          } finally {
            nonThrowingClose(preparedStatement);
          }
        });
  }

  private void commitIfNecessary(Connection c) {
    try {
      if (!c.getAutoCommit()) {
        c.commit();
      }
    } catch (SQLException e) {
      throw new SQLRuntimeException("Failed to commit.", e);
    }
  }

  private void rollbackIfNecessary(Connection c) {
    try {
      if (!c.getAutoCommit()) {
        c.rollback();
      }
    } catch (SQLException e) {
      throw new SQLRuntimeException("Failed to rollback.", e);
    }
  }

  private SQLRuntimeException translateException(SQLException ex) {
    if (ex instanceof SQLIntegrityConstraintViolationException) {
      return new IntegrityConstraintViolation(ex);
    } else {
      return new SQLRuntimeException(ex);
    }
  }

  private <T> T withConnection(Function<Connection, T> doWithConnection) {
    Connection c;
    try {
      log.trace("Getting connection from datasource");
      c = dataSource.getConnection();
    } catch (SQLException e) {
      throw new SQLRuntimeException("Unable to open connection", e);
    }

    try {
      return doWithConnection.apply(c);
    } finally {
      nonThrowingClose(c);
    }
  }

  private <T> List<T> mapResultSet(
      PreparedStatement executedPreparedStatement, RowMapper<T> rowMapper) {
    return withResultSet(
        executedPreparedStatement,
        (ResultSet rs) -> {
          List<T> results = new ArrayList<>();
          while (rs.next()) {
            results.add(rowMapper.map(rs));
          }
          return results;
        });
  }

  private <T> T mapResultSet(
      PreparedStatement executedPreparedStatement, ResultSetMapper<T> resultSetMapper) {
    return withResultSet(executedPreparedStatement, (ResultSet rs) -> resultSetMapper.map(rs));
  }

  private <T> T withResultSet(
      PreparedStatement executedPreparedStatement, DoWithResultSet<T> doWithResultSet) {
    ResultSet rs = null;
    try {
      try {
        rs = executedPreparedStatement.getResultSet();
      } catch (SQLException e) {
        throw new SQLRuntimeException(e);
      }

      try {
        return doWithResultSet.withResultSet(rs);
      } catch (SQLException e) {
        throw new SQLRuntimeException(e);
      }

    } finally {
      nonThrowingClose(rs);
    }
  }

  private void nonThrowingClose(AutoCloseable toClose) {
    if (toClose == null) {
      return;
    }
    try {
      log.trace("Closing " + toClose.getClass().getSimpleName());
      toClose.close();
    } catch (Exception e) {
      log.warn("Exception on close of " + toClose.getClass().getSimpleName(), e);
    }
  }

  interface AfterExecution<T> {
    T doAfterExecution(PreparedStatement executedPreparedStatement) throws SQLException;
  }

  interface DoWithResultSet<T> {
    T withResultSet(ResultSet rs) throws SQLException;
  }
}
