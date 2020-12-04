package xyz.vopen.mixmicro.components.enhance.schedule.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {

  private static final Logger log = LoggerFactory.getLogger(TransactionManager.class);

  ThreadLocal<Connection> currentTransaction = new ThreadLocal<>();
  private final DataSource dataSource;

  public TransactionManager(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public <T> T inTransaction(DoInTransaction<T> doInTransaction) {
    if (currentTransaction.get() != null) {
      throw new SQLRuntimeException(
          "Cannot start new transaction when there already is an ongoing transaction.");
    }

    boolean restoreAutocommit = false;
    try (Connection connection = dataSource.getConnection()) {

      if (connection.getAutoCommit()) {
        connection.setAutoCommit(false);
        restoreAutocommit = true;
      }

      final T result;
      try {
        currentTransaction.set(connection);
        result = doInTransaction.doInTransaction();
      } catch (SQLRuntimeException applicationException) {
        rollback(connection, applicationException);
        throw applicationException;
      }

      commit(connection);
      if (restoreAutocommit) {
        restoreAutocommit(connection);
      }
      return result;

    } catch (SQLException openCloseException) {
      throw new SQLRuntimeException(openCloseException);
    } finally {
      currentTransaction.remove();
    }
  }

  private void restoreAutocommit(Connection connection) {
    try {
      connection.setAutoCommit(true);
    } catch (SQLException e) {
      throw new SQLRuntimeException(
          "Exception when restoring autocommit on connection. Transaction is already committed, but connection might be broken afterwards.",
          e);
    }
  }

  private void commit(Connection connection) {
    try {
      connection.commit();
    } catch (SQLException commitException) {
      rollback(connection, commitException);
    }
  }

  private void rollback(Connection connection, Throwable originalException) {
    try {
      connection.rollback();
    } catch (SQLException rollbackException) {
      log.error(
          "Original application exception overridden by rollback-exception. Throwing rollback-exception. Original application exception: ",
          originalException);
      throw new SQLRuntimeException(rollbackException);
    } catch (RuntimeException rollbackException) {
      log.error(
          "Original application exception overridden by rollback-exception. Throwing rollback-exception. Original application exception: ",
          originalException);
      throw rollbackException;
    }
  }

  public interface DoInTransaction<T> {
    T doInTransaction();
  }
}
