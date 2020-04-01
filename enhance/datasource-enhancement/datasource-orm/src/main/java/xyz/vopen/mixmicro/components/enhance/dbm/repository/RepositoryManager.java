package xyz.vopen.mixmicro.components.enhance.dbm.repository;

import xyz.vopen.mixmicro.components.enhance.dbm.db.DatabaseType;
import xyz.vopen.mixmicro.components.enhance.dbm.logger.Logger;
import xyz.vopen.mixmicro.components.enhance.dbm.logger.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.dbm.misc.SqlExceptionUtil;
import xyz.vopen.mixmicro.components.enhance.dbm.support.ConnectionSource;
import xyz.vopen.mixmicro.components.enhance.dbm.table.DatabaseTable;
import xyz.vopen.mixmicro.components.enhance.dbm.table.DatabaseTableConfig;

import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Class which caches created Repositorys. Sometimes internal Repositorys are used to support such features as
 * auto-refreshing of foreign fields or collections of sub-objects. Since instantiation of the Repository
 * is a bit expensive, this class is used in an attempt to only create a Repository once for each class.
 *
 * <p><b>NOTE:</b> To use this cache, you should make sure you've added a {@link
 * DatabaseTable#repositoryClass()} value to the annotation to the top of your class.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class RepositoryManager {

  private static Map<Class<?>, DatabaseTableConfig<?>> configMap = null;
  private static Map<ClassConnectionSource, Repository<?, ?>> classMap = null;
  private static Map<TableConfigConnectionSource, Repository<?, ?>> tableConfigMap = null;

  private static Logger logger = LoggerFactory.getLogger(RepositoryManager.class);

  /**
   * Helper method to create a Repository object without having to define a class. This checks to see if
   * the Repository has already been created. If not then it is a call through to {@link
   * BaseRepositoryImpl#createRepository(ConnectionSource, Class)}.
   */
  public static synchronized <D extends Repository<T, ?>, T> D createRepository(
      ConnectionSource connectionSource, Class<T> clazz) throws SQLException {
    if (connectionSource == null) {
      throw new IllegalArgumentException("connectionSource argument cannot be null");
    }
    ClassConnectionSource key = new ClassConnectionSource(connectionSource, clazz);
    Repository<?, ?> repository = lookupRepository(key);
    if (repository != null) {
      @SuppressWarnings("unchecked")
      D castRepository = (D) repository;
      return castRepository;
    }

    // see if we can build it from source
    repository = createRepositoryFromConfig(connectionSource, clazz);
    if (repository != null) {
      @SuppressWarnings("unchecked")
      D castRepository = (D) repository;
      return castRepository;
    }

    DatabaseTable databaseTable = clazz.getAnnotation(DatabaseTable.class);
    if (databaseTable == null
        || databaseTable.repositoryClass() == Void.class
        || databaseTable.repositoryClass() == BaseRepositoryImpl.class) {
      // see if the database type has some special table config extract method (Android)
      DatabaseType databaseType = connectionSource.getDatabaseType();
      DatabaseTableConfig<T> config =
          databaseType.extractDatabaseTableConfig(connectionSource, clazz);
      Repository<T, ?> repositoryTmp;
      if (config == null) {
        repositoryTmp = BaseRepositoryImpl.createRepository(connectionSource, clazz);
      } else {
        repositoryTmp = BaseRepositoryImpl.createRepository(connectionSource, config);
      }
      repository = repositoryTmp;
      logger.debug("created repository for class {} with reflection", clazz);
    } else {
      Class<?> repositoryClass = databaseTable.repositoryClass();
      Object[] arguments = new Object[] {connectionSource, clazz};
      // look first for the constructor with a class parameter in case it is a generic repository
      Constructor<?> repositoryConstructor = findConstructor(repositoryClass, arguments);
      if (repositoryConstructor == null) {
        // then look for the constructor with just the ConnectionSource
        arguments = new Object[] {connectionSource};
        repositoryConstructor = findConstructor(repositoryClass, arguments);
        if (repositoryConstructor == null) {
          throw new SQLException(
              "Could not find public constructor with ConnectionSource and optional Class parameters "
                  + repositoryClass
                  + ".  Missing static on class?");
        }
      }
      try {
        repository = (Repository<?, ?>) repositoryConstructor.newInstance(arguments);
        logger.debug("created repository for class {} from constructor", clazz);
      } catch (Exception e) {
        throw SqlExceptionUtil.create("Could not call the constructor in class " + repositoryClass, e);
      }
    }

    registerRepository(connectionSource, repository);
    @SuppressWarnings("unchecked")
    D castRepository = (D) repository;
    return castRepository;
  }

  /**
   * Helper method to lookup a Repository if it has already been associated with the class. Otherwise this
   * returns null.
   */
  public static synchronized <D extends Repository<T, ?>, T> D lookupRepository(
      ConnectionSource connectionSource, Class<T> clazz) {
    if (connectionSource == null) {
      throw new IllegalArgumentException("connectionSource argument cannot be null");
    }
    ClassConnectionSource key = new ClassConnectionSource(connectionSource, clazz);
    Repository<?, ?> repository = lookupRepository(key);
    @SuppressWarnings("unchecked")
    D castRepository = (D) repository;
    return castRepository;
  }

  /**
   * Helper method to create a Repository object without having to define a class. This checks to see if
   * the Repository has already been created. If not then it is a call through to {@link
   * BaseRepositoryImpl#createRepository(ConnectionSource, DatabaseTableConfig)}.
   */
  public static synchronized <D extends Repository<T, ?>, T> D createRepository(
      ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
    if (connectionSource == null) {
      throw new IllegalArgumentException("connectionSource argument cannot be null");
    }
    return doCreateRepository(connectionSource, tableConfig);
  }

  /**
   * Helper method to lookup a Repository if it has already been associated with the table-config.
   * Otherwise this returns null.
   */
  public static synchronized <D extends Repository<T, ?>, T> D lookupRepository(
      ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) {
    if (connectionSource == null) {
      throw new IllegalArgumentException("connectionSource argument cannot be null");
    }
    TableConfigConnectionSource key =
        new TableConfigConnectionSource(connectionSource, tableConfig);
    Repository<?, ?> repository = lookupRepository(key);
    if (repository == null) {
      return null;
    } else {
      @SuppressWarnings("unchecked")
      D castRepository = (D) repository;
      return castRepository;
    }
  }

  /**
   * Register the Repository with the cache. This will allow folks to build a Repository externally and then
   * register so it can be used internally as necessary.
   *
   * <p><b>NOTE:</b> By default this registers the Repository to be associated with the class that it uses.
   * If you need to register multiple repository's that use different {@link DatabaseTableConfig}s then you
   * should use {@link #registerRepositoryWithTableConfig(ConnectionSource, Repository)}.
   *
   * <p><b>NOTE:</b> You should maybe use the {@link DatabaseTable#repositoryClass()} and have the
   * RepositoryManager construct the Repository if possible.
   */
  public static synchronized void registerRepository(ConnectionSource connectionSource, Repository<?, ?> repository) {
    if (connectionSource == null) {
      throw new IllegalArgumentException("connectionSource argument cannot be null");
    }
    addRepositoryToClassMap(new ClassConnectionSource(connectionSource, repository.getDataClass()), repository);
  }

  /**
   * Remove a Repository from the cache. This is necessary if we've registered it already but it throws an
   * exception during configuration.
   */
  public static synchronized void unregisterRepository(ConnectionSource connectionSource, Repository<?, ?> repository) {
    if (connectionSource == null) {
      throw new IllegalArgumentException("connectionSource argument cannot be null");
    }
    removeRepositoryToClassMap(new ClassConnectionSource(connectionSource, repository.getDataClass()));
  }

  /** Remove all Repositorys from the cache for the connection source. */
  public static synchronized void unregisterRepositorys(ConnectionSource connectionSource) {
    if (connectionSource == null) {
      throw new IllegalArgumentException("connectionSource argument cannot be null");
    }
    removeRepositorysFromConnectionClassMap(connectionSource);
  }

  /**
   * Same as {@link #registerRepository(ConnectionSource, Repository)} but this allows you to register it just
   * with its {@link DatabaseTableConfig}. This allows multiple versions of the Repository to be configured
   * if necessary.
   */
  public static synchronized void registerRepositoryWithTableConfig(
      ConnectionSource connectionSource, Repository<?, ?> repository) {
    if (connectionSource == null) {
      throw new IllegalArgumentException("connectionSource argument cannot be null");
    }
    if (repository instanceof BaseRepositoryImpl) {
      DatabaseTableConfig<?> tableConfig = ((BaseRepositoryImpl<?, ?>) repository).getTableConfig();
      if (tableConfig != null) {
        addRepositoryToTableMap(new TableConfigConnectionSource(connectionSource, tableConfig), repository);
        return;
      }
    }
    addRepositoryToClassMap(new ClassConnectionSource(connectionSource, repository.getDataClass()), repository);
  }

  /** Clear out all of internal caches. */
  public static synchronized void clearCache() {
    if (configMap != null) {
      configMap.clear();
      configMap = null;
    }
    clearRepositoryCache();
  }

  /** Clear out our Repository caches. */
  public static synchronized void clearRepositoryCache() {
    if (classMap != null) {
      classMap.clear();
      classMap = null;
    }
    if (tableConfigMap != null) {
      tableConfigMap.clear();
      tableConfigMap = null;
    }
  }

  /**
   * This adds database table configurations to the internal cache which can be used to speed up Repository
   * construction. This is especially true of Android and other mobile platforms.
   */
  public static synchronized void addCachedDatabaseConfigs(
      Collection<DatabaseTableConfig<?>> configs) {
    Map<Class<?>, DatabaseTableConfig<?>> newMap;
    if (configMap == null) {
      newMap = new HashMap<Class<?>, DatabaseTableConfig<?>>();
    } else {
      newMap = new HashMap<Class<?>, DatabaseTableConfig<?>>(configMap);
    }
    for (DatabaseTableConfig<?> config : configs) {
      newMap.put(config.getDataClass(), config);
      logger.info("Loaded configuration for {}", config.getDataClass());
    }
    configMap = newMap;
  }

  private static void addRepositoryToClassMap(ClassConnectionSource key, Repository<?, ?> repository) {
    if (classMap == null) {
      classMap = new HashMap<ClassConnectionSource, Repository<?, ?>>();
    }
    classMap.put(key, repository);
  }

  private static void removeRepositoryToClassMap(ClassConnectionSource key) {
    if (classMap != null) {
      classMap.remove(key);
    }
  }

  private static void removeRepositorysFromConnectionClassMap(ConnectionSource connectionSource) {
    if (classMap != null) {
      Iterator<ClassConnectionSource> classIterator = classMap.keySet().iterator();
      while (classIterator.hasNext()) {
        if (classIterator.next().connectionSource == connectionSource) {
          classIterator.remove();
        }
      }
    }
  }

  private static void addRepositoryToTableMap(TableConfigConnectionSource key, Repository<?, ?> repository) {
    if (tableConfigMap == null) {
      tableConfigMap = new HashMap<TableConfigConnectionSource, Repository<?, ?>>();
    }
    tableConfigMap.put(key, repository);
  }

  private static <T> Repository<?, ?> lookupRepository(ClassConnectionSource key) {
    if (classMap == null) {
      classMap = new HashMap<ClassConnectionSource, Repository<?, ?>>();
    }
    Repository<?, ?> repository = classMap.get(key);
    if (repository == null) {
      return null;
    } else {
      return repository;
    }
  }

  private static <T> Repository<?, ?> lookupRepository(TableConfigConnectionSource key) {
    if (tableConfigMap == null) {
      tableConfigMap = new HashMap<TableConfigConnectionSource, Repository<?, ?>>();
    }
    Repository<?, ?> repository = tableConfigMap.get(key);
    if (repository == null) {
      return null;
    } else {
      return repository;
    }
  }

  private static Constructor<?> findConstructor(Class<?> repositoryClass, Object[] params) {
    for (Constructor<?> constructor : repositoryClass.getConstructors()) {
      Class<?>[] paramsTypes = constructor.getParameterTypes();
      if (paramsTypes.length == params.length) {
        boolean match = true;
        for (int i = 0; i < paramsTypes.length; i++) {
          if (!paramsTypes[i].isAssignableFrom(params[i].getClass())) {
            match = false;
            break;
          }
        }
        if (match) {
          return constructor;
        }
      }
    }
    return null;
  }

  /** Creates the Repository if we have config information cached and caches the Repository. */
  private static <D, T> D createRepositoryFromConfig(ConnectionSource connectionSource, Class<T> clazz)
      throws SQLException {
    // no loaded configs
    if (configMap == null) {
      return null;
    }

    @SuppressWarnings("unchecked")
    DatabaseTableConfig<T> config = (DatabaseTableConfig<T>) configMap.get(clazz);
    // if we don't config information cached return null
    if (config == null) {
      return null;
    }

    // else create a Repository using configuration
    Repository<T, ?> configedRepository = doCreateRepository(connectionSource, config);
    @SuppressWarnings("unchecked")
    D castRepository = (D) configedRepository;
    return castRepository;
  }

  private static <D extends Repository<T, ?>, T> D doCreateRepository(
      ConnectionSource connectionSource, DatabaseTableConfig<T> tableConfig) throws SQLException {
    TableConfigConnectionSource tableKey =
        new TableConfigConnectionSource(connectionSource, tableConfig);
    // look up in the table map
    Repository<?, ?> repository = lookupRepository(tableKey);
    if (repository != null) {
      @SuppressWarnings("unchecked")
      D castRepository = (D) repository;
      return castRepository;
    }

    // now look it up in the class map
    Class<T> dataClass = tableConfig.getDataClass();
    ClassConnectionSource classKey = new ClassConnectionSource(connectionSource, dataClass);
    repository = lookupRepository(classKey);
    if (repository != null) {
      // if it is not in the table map but is in the class map, add it
      addRepositoryToTableMap(tableKey, repository);
      @SuppressWarnings("unchecked")
      D castRepository = (D) repository;
      return castRepository;
    }

    // build the Repository using the table information
    DatabaseTable databaseTable = tableConfig.getDataClass().getAnnotation(DatabaseTable.class);
    if (databaseTable == null
        || databaseTable.repositoryClass() == Void.class
        || databaseTable.repositoryClass() == BaseRepositoryImpl.class) {
      Repository<T, ?> repositoryTmp = BaseRepositoryImpl.createRepository(connectionSource, tableConfig);
      repository = repositoryTmp;
    } else {
      Class<?> repositoryClass = databaseTable.repositoryClass();
      Object[] arguments = new Object[] {connectionSource, tableConfig};
      Constructor<?> constructor = findConstructor(repositoryClass, arguments);
      if (constructor == null) {
        throw new SQLException(
            "Could not find public constructor with ConnectionSource, DatabaseTableConfig parameters in class "
                + repositoryClass);
      }
      try {
        repository = (Repository<?, ?>) constructor.newInstance(arguments);
      } catch (Exception e) {
        throw SqlExceptionUtil.create("Could not call the constructor in class " + repositoryClass, e);
      }
    }

    addRepositoryToTableMap(tableKey, repository);
    logger.debug("created repository for class {} from table config", dataClass);

    // if it is not in the class config either then add it
    if (lookupRepository(classKey) == null) {
      addRepositoryToClassMap(classKey, repository);
    }

    @SuppressWarnings("unchecked")
    D castRepository = (D) repository;
    return castRepository;
  }

  /** Key for our class Repository map. */
  private static class ClassConnectionSource {
    ConnectionSource connectionSource;
    Class<?> clazz;

    public ClassConnectionSource(ConnectionSource connectionSource, Class<?> clazz) {
      this.connectionSource = connectionSource;
      this.clazz = clazz;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = prime + clazz.hashCode();
      result = prime * result + connectionSource.hashCode();
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      ClassConnectionSource other = (ClassConnectionSource) obj;
      if (!clazz.equals(other.clazz)) {
        return false;
      } else if (!connectionSource.equals(other.connectionSource)) {
        return false;
      } else {
        return true;
      }
    }
  }

  /** Key for our table-config Repository map. */
  private static class TableConfigConnectionSource {
    ConnectionSource connectionSource;
    DatabaseTableConfig<?> tableConfig;

    public TableConfigConnectionSource(
        ConnectionSource connectionSource, DatabaseTableConfig<?> tableConfig) {
      this.connectionSource = connectionSource;
      this.tableConfig = tableConfig;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = prime + tableConfig.hashCode();
      result = prime * result + connectionSource.hashCode();
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
        return false;
      }
      TableConfigConnectionSource other = (TableConfigConnectionSource) obj;
      if (!tableConfig.equals(other.tableConfig)) {
        return false;
      } else if (!connectionSource.equals(other.connectionSource)) {
        return false;
      } else {
        return true;
      }
    }
  }
}
