package xyz.vopen.mixmicro.components.boot.dbm.multidatasource;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import xyz.vopen.mixmicro.components.enhance.dbm.repository.Repository;
import xyz.vopen.mixmicro.components.enhance.dbm.repository.RepositoryManager;
import xyz.vopen.mixmicro.components.enhance.dbm.support.ConnectionSource;
import xyz.vopen.mixmicro.components.boot.dbm.multidatasource.exception.MultiDataSourceRepositoryException;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link AbstractMultiDataSourceRepository}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/3
 */
@SuppressWarnings("unchecked")
public abstract class AbstractMultiDataSourceRepository extends AbstractNativeConnectionSource
    implements MultiDataSourceRepository {

  protected static final Logger log = LoggerFactory.getLogger(AbstractMultiDataSourceRepository.class);

  private static Map<String, Map<Class<?>, Repository<?, ?>>> repositories = new ConcurrentHashMap<>();

  /**
   * This method will return connection source instance of {@link ConnectionSource}
   *
   * @return return initialized instance of {@link ConnectionSource}
   */
  @Override
  public ConnectionSource getConnection(String schema) {
    return super.getConnection(schema);
  }

  public <T, ID> Repository<T, ID> getRepository() throws MultiDataSourceRepositoryException {
    return (Repository<T, ID>) getRepository(modelClass());
  }

  @NonNull
  public <T, ID> Repository<T, ID> getRepository(Class<T> clazz)
      throws MultiDataSourceRepositoryException {

    try {

      String schema = schema();

      if (repositories.containsKey(schema)) {

        Map<Class<?>, Repository<?, ?>> map = repositories.get(schema);

        if (map.containsKey(clazz)) {

          return (Repository<T, ID>) map.get(clazz);

        } else {

          Repository<T, ID> repository = RepositoryManager.createRepository(getConnection(schema()), clazz);

          map.put(clazz, repository);

          return repository;
        }

      } else {

        Repository<T, ID> repository = RepositoryManager.createRepository(getConnection(schema()), clazz);

        Map<Class<?>, Repository<?, ?>> temp = Maps.newHashMap();

        temp.put(clazz, repository);

        repositories.put(schema, temp);

        return repository;
      }

    } catch (SQLException e) {

      throw new MultiDataSourceRepositoryException(e);
    }
  }
}
