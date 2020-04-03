package xyz.vopen.mixmicro.components.enhance.dbm.repository;

import xyz.vopen.mixmicro.components.enhance.dbm.field.DataType;
import xyz.vopen.mixmicro.components.enhance.dbm.field.FieldType;
import xyz.vopen.mixmicro.components.enhance.dbm.logger.Log;
import xyz.vopen.mixmicro.components.enhance.dbm.logger.Logger;
import xyz.vopen.mixmicro.components.enhance.dbm.logger.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.dbm.stmt.*;
import xyz.vopen.mixmicro.components.enhance.dbm.support.ConnectionSource;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseConnection;
import xyz.vopen.mixmicro.components.enhance.dbm.support.DatabaseResults;
import xyz.vopen.mixmicro.components.enhance.dbm.table.DatabaseTableConfig;
import xyz.vopen.mixmicro.components.enhance.dbm.table.ObjectFactory;
import xyz.vopen.mixmicro.components.enhance.dbm.table.TableInfo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Proxy to a {@link Repository} that wraps each Exception and rethrows it as RuntimeException. You can use
 * this if your usage pattern is to ignore all exceptions. That's not a pattern that I like so it's
 * not the default.
 *
 * <pre>
 * RuntimeExceptionRepository&lt;Account, String&gt; accountRepository = RuntimeExceptionRepository.createRepository(connectionSource, Account.class);
 * </pre>
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class RuntimeExceptionRepository<T, ID> implements Repository<T, ID> {

  /*
   * We use debug here because we don't want these messages to be logged by default. The user will need to turn on
   * logging for this class to DEBUG to see the messages.
   */
  private static final Log.Level LOG_LEVEL = Log.Level.DEBUG;
  private static final Logger logger = LoggerFactory.getLogger(RuntimeExceptionRepository.class);
  private Repository<T, ID> repository;

  public RuntimeExceptionRepository(Repository<T, ID> repository) {
    this.repository = repository;
  }

  /**
   * Call through to {@link RepositoryManager#createRepository(ConnectionSource, Class)} with the returned Repository
   * wrapped in a RuntimeExceptionRepository.
   */
  public static <T, ID> RuntimeExceptionRepository<T, ID> createRepository(
      ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
    @SuppressWarnings("unchecked")
    Repository<T, ID> castRepository = (Repository<T, ID>) RepositoryManager.createRepository(connectionSource, clazz);
    return new RuntimeExceptionRepository<T, ID>(castRepository);
  }

  /**
   * Call through to {@link RepositoryManager#createRepository(ConnectionSource, DatabaseTableConfig)} with the
   * returned Repository wrapped in a RuntimeExceptionRepository.
   */
  public static <T, ID> RuntimeExceptionRepository<T, ID> createRepository(
      ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
    @SuppressWarnings("unchecked")
    Repository<T, ID> castRepository = (Repository<T, ID>) RepositoryManager.createRepository(connectionSource, tableConfig);
    return new RuntimeExceptionRepository<T, ID>(castRepository);
  }

  /** @see Repository#queryForId(Object) */
  @Override
  public T queryForId(ID id) {
    try {
      return repository.queryForId(id);
    } catch (SQLException e) {
      logMessage(e, "queryForId threw exception on: " + id);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryForFirst(PreparedQuery) */
  @Override
  public T queryForFirst(PreparedQuery<T> preparedQuery) {
    try {
      return repository.queryForFirst(preparedQuery);
    } catch (SQLException e) {
      logMessage(e, "queryForFirst threw exception on: " + preparedQuery);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryForAll() */
  @Override
  public List<T> queryForAll() {
    try {
      return repository.queryForAll();
    } catch (SQLException e) {
      logMessage(e, "queryForAll threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryForEq(String, Object) */
  @Override
  public List<T> queryForEq(String fieldName, Object value) {
    try {
      return repository.queryForEq(fieldName, value);
    } catch (SQLException e) {
      logMessage(e, "queryForEq threw exception on: " + fieldName);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryForMatching(Object) */
  @Override
  public List<T> queryForMatching(T matchObj) {
    try {
      return repository.queryForMatching(matchObj);
    } catch (SQLException e) {
      logMessage(e, "queryForMatching threw exception on: " + matchObj);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryForMatchingArgs(Object) */
  @Override
  public List<T> queryForMatchingArgs(T matchObj) {
    try {
      return repository.queryForMatchingArgs(matchObj);
    } catch (SQLException e) {
      logMessage(e, "queryForMatchingArgs threw exception on: " + matchObj);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryForFieldValues(Map) */
  @Override
  public List<T> queryForFieldValues(Map<String, Object> fieldValues) {
    try {
      return repository.queryForFieldValues(fieldValues);
    } catch (SQLException e) {
      logMessage(e, "queryForFieldValues threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryForFieldValuesArgs(Map) */
  @Override
  public List<T> queryForFieldValuesArgs(Map<String, Object> fieldValues) {
    try {
      return repository.queryForFieldValuesArgs(fieldValues);
    } catch (SQLException e) {
      logMessage(e, "queryForFieldValuesArgs threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryForSameId(Object) */
  @Override
  public T queryForSameId(T data) {
    try {
      return repository.queryForSameId(data);
    } catch (SQLException e) {
      logMessage(e, "queryForSameId threw exception on: " + data);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryBuilder() */
  @Override
  public QueryBuilder<T, ID> queryBuilder() {
    return repository.queryBuilder();
  }

  /** @see Repository#updateBuilder() */
  @Override
  public UpdateBuilder<T, ID> updateBuilder() {
    return repository.updateBuilder();
  }

  /** @see Repository#deleteBuilder() */
  @Override
  public DeleteBuilder<T, ID> deleteBuilder() {
    return repository.deleteBuilder();
  }

  /** @see Repository#query(PreparedQuery) */
  @Override
  public List<T> query(PreparedQuery<T> preparedQuery) {
    try {
      return repository.query(preparedQuery);
    } catch (SQLException e) {
      logMessage(e, "query threw exception on: " + preparedQuery);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#create(Object) */
  @Override
  public int create(T data) {
    try {
      return repository.create(data);
    } catch (SQLException e) {
      logMessage(e, "create threw exception on: " + data);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#create(Collection) */
  @Override
  public int create(Collection<T> datas) {
    try {
      return repository.create(datas);
    } catch (SQLException e) {
      logMessage(e, "create threw exception on: " + datas);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#createIfNotExists(Object) */
  @Override
  public T createIfNotExists(T data) {
    try {
      return repository.createIfNotExists(data);
    } catch (SQLException e) {
      logMessage(e, "createIfNotExists threw exception on: " + data);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#createOrUpdate(Object) */
  @Override
  public CreateOrUpdateStatus createOrUpdate(T data) {
    try {
      return repository.createOrUpdate(data);
    } catch (SQLException e) {
      logMessage(e, "createOrUpdate threw exception on: " + data);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#update(Object) */
  @Override
  public int update(T data) {
    try {
      return repository.update(data);
    } catch (SQLException e) {
      logMessage(e, "update threw exception on: " + data);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#updateId(Object, Object) */
  @Override
  public int updateId(T data, ID newId) {
    try {
      return repository.updateId(data, newId);
    } catch (SQLException e) {
      logMessage(e, "updateId threw exception on: " + data);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#update(PreparedUpdate) */
  @Override
  public int update(PreparedUpdate<T> preparedUpdate) {
    try {
      return repository.update(preparedUpdate);
    } catch (SQLException e) {
      logMessage(e, "update threw exception on: " + preparedUpdate);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#refresh(Object) */
  @Override
  public int refresh(T data) {
    try {
      return repository.refresh(data);
    } catch (SQLException e) {
      logMessage(e, "refresh threw exception on: " + data);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#delete(Object) */
  @Override
  public int delete(T data) {
    try {
      return repository.delete(data);
    } catch (SQLException e) {
      logMessage(e, "delete threw exception on: " + data);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#deleteById(Object) */
  @Override
  public int deleteById(ID id) {
    try {
      return repository.deleteById(id);
    } catch (SQLException e) {
      logMessage(e, "deleteById threw exception on: " + id);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#delete(Collection) */
  @Override
  public int delete(Collection<T> datas) {
    try {
      return repository.delete(datas);
    } catch (SQLException e) {
      logMessage(e, "delete threw exception on: " + datas);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#deleteIds(Collection) */
  @Override
  public int deleteIds(Collection<ID> ids) {
    try {
      return repository.deleteIds(ids);
    } catch (SQLException e) {
      logMessage(e, "deleteIds threw exception on: " + ids);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#delete(PreparedDelete) */
  @Override
  public int delete(PreparedDelete<T> preparedDelete) {
    try {
      return repository.delete(preparedDelete);
    } catch (SQLException e) {
      logMessage(e, "delete threw exception on: " + preparedDelete);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#iterator() */
  @Override
  public CloseableIterator<T> iterator() {
    return repository.iterator();
  }

  @Override
  public CloseableIterator<T> closeableIterator() {
    return repository.closeableIterator();
  }

  /** @see Repository#iterator(int) */
  @Override
  public CloseableIterator<T> iterator(int resultFlags) {
    return repository.iterator(resultFlags);
  }

  /** @see Repository#getWrappedIterable() */
  @Override
  public CloseableWrappedIterable<T> getWrappedIterable() {
    return repository.getWrappedIterable();
  }

  /** @see Repository#getWrappedIterable(PreparedQuery) */
  @Override
  public CloseableWrappedIterable<T> getWrappedIterable(PreparedQuery<T> preparedQuery) {
    return repository.getWrappedIterable(preparedQuery);
  }

  /** @see Repository#closeLastIterator() */
  @Override
  public void closeLastIterator() {
    try {
      repository.closeLastIterator();
    } catch (IOException e) {
      logMessage(e, "closeLastIterator threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#iterator(PreparedQuery) */
  @Override
  public CloseableIterator<T> iterator(PreparedQuery<T> preparedQuery) {
    try {
      return repository.iterator(preparedQuery);
    } catch (SQLException e) {
      logMessage(e, "iterator threw exception on: " + preparedQuery);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#iterator(PreparedQuery, int) */
  @Override
  public CloseableIterator<T> iterator(PreparedQuery<T> preparedQuery, int resultFlags) {
    try {
      return repository.iterator(preparedQuery, resultFlags);
    } catch (SQLException e) {
      logMessage(e, "iterator threw exception on: " + preparedQuery);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryRaw(String, String...) */
  @Override
  public GenericRawResults<String[]> queryRaw(String query, String... arguments) {
    try {
      return repository.queryRaw(query, arguments);
    } catch (SQLException e) {
      logMessage(e, "queryRaw threw exception on: " + query);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryRawValue(String, String...) */
  @Override
  public long queryRawValue(String query, String... arguments) {
    try {
      return repository.queryRawValue(query, arguments);
    } catch (SQLException e) {
      logMessage(e, "queryRawValue threw exception on: " + query);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryRaw(String, RawRowMapper, String...) */
  @Override
  public <UO> GenericRawResults<UO> queryRaw(
      String query, RawRowMapper<UO> mapper, String... arguments) {
    try {
      return repository.queryRaw(query, mapper, arguments);
    } catch (SQLException e) {
      logMessage(e, "queryRaw threw exception on: " + query);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryRaw(String, DataType[], RawRowObjectMapper, String...) */
  @Override
  public <UO> GenericRawResults<UO> queryRaw(
      String query, DataType[] columnTypes, RawRowObjectMapper<UO> mapper, String... arguments) {
    try {
      return repository.queryRaw(query, columnTypes, mapper, arguments);
    } catch (SQLException e) {
      logMessage(e, "queryRaw threw exception on: " + query);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryRaw(String, DataType[], String...) */
  @Override
  public GenericRawResults<Object[]> queryRaw(
      String query, DataType[] columnTypes, String... arguments) {
    try {
      return repository.queryRaw(query, columnTypes, arguments);
    } catch (SQLException e) {
      logMessage(e, "queryRaw threw exception on: " + query);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#queryRaw(String, DatabaseResultsMapper, String...) */
  @Override
  public <UO> GenericRawResults<UO> queryRaw(
      String query, DatabaseResultsMapper<UO> mapper, String... arguments) {
    try {
      return repository.queryRaw(query, mapper, arguments);
    } catch (SQLException e) {
      logMessage(e, "queryRaw threw exception on: " + query);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#executeRaw(String, String...) */
  @Override
  public int executeRaw(String statement, String... arguments) {
    try {
      return repository.executeRaw(statement, arguments);
    } catch (SQLException e) {
      logMessage(e, "executeRaw threw exception on: " + statement);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#executeRawNoArgs(String) */
  @Override
  public int executeRawNoArgs(String statement) {
    try {
      return repository.executeRawNoArgs(statement);
    } catch (SQLException e) {
      logMessage(e, "executeRawNoArgs threw exception on: " + statement);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#updateRaw(String, String...) */
  @Override
  public int updateRaw(String statement, String... arguments) {
    try {
      return repository.updateRaw(statement, arguments);
    } catch (SQLException e) {
      logMessage(e, "updateRaw threw exception on: " + statement);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#callBatchTasks(Callable) */
  @Override
  public <CT> CT callBatchTasks(Callable<CT> callable) {
    try {
      return repository.callBatchTasks(callable);
    } catch (Exception e) {
      logMessage(e, "callBatchTasks threw exception on: " + callable);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#objectToString(Object) */
  @Override
  public String objectToString(T data) {
    return repository.objectToString(data);
  }

  /** @see Repository#objectsEqual(Object, Object) */
  @Override
  public boolean objectsEqual(T data1, T data2) {
    try {
      return repository.objectsEqual(data1, data2);
    } catch (SQLException e) {
      logMessage(e, "objectsEqual threw exception on: " + data1 + " and " + data2);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#extractId(Object) */
  @Override
  public ID extractId(T data) {
    try {
      return repository.extractId(data);
    } catch (SQLException e) {
      logMessage(e, "extractId threw exception on: " + data);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#getDataClass() */
  @Override
  public Class<T> getDataClass() {
    return repository.getDataClass();
  }

  /** @see Repository#findForeignFieldType(Class) */
  @Override
  public FieldType findForeignFieldType(Class<?> clazz) {
    return repository.findForeignFieldType(clazz);
  }

  /** @see Repository#isUpdatable() */
  @Override
  public boolean isUpdatable() {
    return repository.isUpdatable();
  }

  /** @see Repository#isTableExists() */
  @Override
  public boolean isTableExists() {
    try {
      return repository.isTableExists();
    } catch (SQLException e) {
      logMessage(e, "isTableExists threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#countOf() */
  @Override
  public long countOf() {
    try {
      return repository.countOf();
    } catch (SQLException e) {
      logMessage(e, "countOf threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#countOf(PreparedQuery) */
  @Override
  public long countOf(PreparedQuery<T> preparedQuery) {
    try {
      return repository.countOf(preparedQuery);
    } catch (SQLException e) {
      logMessage(e, "countOf threw exception on " + preparedQuery);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#assignEmptyForeignCollection(Object, String) */
  @Override
  public void assignEmptyForeignCollection(T parent, String fieldName) {
    try {
      repository.assignEmptyForeignCollection(parent, fieldName);
    } catch (SQLException e) {
      logMessage(e, "assignEmptyForeignCollection threw exception on " + fieldName);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#getEmptyForeignCollection(String) */
  @Override
  public <FT> ForeignCollection<FT> getEmptyForeignCollection(String fieldName) {
    try {
      return repository.getEmptyForeignCollection(fieldName);
    } catch (SQLException e) {
      logMessage(e, "getEmptyForeignCollection threw exception on " + fieldName);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#getObjectCache() */
  @Override
  public ObjectCache getObjectCache() {
    return repository.getObjectCache();
  }

  /** @see Repository#setObjectCache(boolean) */
  @Override
  public void setObjectCache(boolean enabled) {
    try {
      repository.setObjectCache(enabled);
    } catch (SQLException e) {
      logMessage(e, "setObjectCache(" + enabled + ") threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#setObjectCache(ObjectCache) */
  @Override
  public void setObjectCache(ObjectCache objectCache) {
    try {
      repository.setObjectCache(objectCache);
    } catch (SQLException e) {
      logMessage(e, "setObjectCache threw exception on " + objectCache);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#clearObjectCache() */
  @Override
  public void clearObjectCache() {
    repository.clearObjectCache();
  }

  /** @see Repository#mapSelectStarRow(DatabaseResults) */
  @Override
  public T mapSelectStarRow(DatabaseResults results) {
    try {
      return repository.mapSelectStarRow(results);
    } catch (SQLException e) {
      logMessage(e, "mapSelectStarRow threw exception on results");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#getSelectStarRowMapper() */
  @Override
  public GenericRowMapper<T> getSelectStarRowMapper() {
    try {
      return repository.getSelectStarRowMapper();
    } catch (SQLException e) {
      logMessage(e, "getSelectStarRowMapper threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#idExists(Object) */
  @Override
  public boolean idExists(ID id) {
    try {
      return repository.idExists(id);
    } catch (SQLException e) {
      logMessage(e, "idExists threw exception on " + id);
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#startThreadConnection() */
  @Override
  public DatabaseConnection startThreadConnection() {
    try {
      return repository.startThreadConnection();
    } catch (SQLException e) {
      logMessage(e, "startThreadConnection() threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#endThreadConnection(DatabaseConnection) */
  @Override
  public void endThreadConnection(DatabaseConnection connection) {
    try {
      repository.endThreadConnection(connection);
    } catch (SQLException e) {
      logMessage(e, "endThreadConnection(" + connection + ") threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#setAutoCommit(DatabaseConnection, boolean) */
  @Override
  public void setAutoCommit(DatabaseConnection connection, boolean autoCommit) {
    try {
      repository.setAutoCommit(connection, autoCommit);
    } catch (SQLException e) {
      logMessage(e, "setAutoCommit(" + connection + "," + autoCommit + ") threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#isAutoCommit(DatabaseConnection) */
  @Override
  public boolean isAutoCommit(DatabaseConnection connection) {
    try {
      return repository.isAutoCommit(connection);
    } catch (SQLException e) {
      logMessage(e, "isAutoCommit(" + connection + ") threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#commit(DatabaseConnection) */
  @Override
  public void commit(DatabaseConnection connection) {
    try {
      repository.commit(connection);
    } catch (SQLException e) {
      logMessage(e, "commit(" + connection + ") threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#rollBack(DatabaseConnection) */
  @Override
  public void rollBack(DatabaseConnection connection) {
    try {
      repository.rollBack(connection);
    } catch (SQLException e) {
      logMessage(e, "rollBack(" + connection + ") threw exception");
      throw new RuntimeException(e);
    }
  }

  /** @see Repository#setObjectFactory(ObjectFactory) */
  @Override
  public void setObjectFactory(ObjectFactory<T> objectFactory) {
    repository.setObjectFactory(objectFactory);
  }

  /** @see Repository#getRawRowMapper() */
  @Override
  public RawRowMapper<T> getRawRowMapper() {
    return repository.getRawRowMapper();
  }

  /** @see Repository#getConnectionSource() */
  @Override
  public ConnectionSource getConnectionSource() {
    return repository.getConnectionSource();
  }

  @Override
  public void registerObserver(RepositoryObserver observer) {
    repository.registerObserver(observer);
  }

  @Override
  public void unregisterObserver(RepositoryObserver observer) {
    repository.unregisterObserver(observer);
  }

  @Override
  public void notifyChanges() {
    repository.notifyChanges();
  }

  @Override
  public String getTableName() {
    return repository.getTableName();
  }

  @Override
  public T createObjectInstance() {
    try {
      return repository.createObjectInstance();
    } catch (SQLException e) {
      logMessage(e, "createObjectInstance() threw exception");
      throw new RuntimeException(e);
    }
  }

  @Override
  public TableInfo<T, ID> getTableInfo() {
    return repository.getTableInfo();
  }

  private void logMessage(Exception e, String message) {
    logger.log(LOG_LEVEL, e, message);
  }
}
