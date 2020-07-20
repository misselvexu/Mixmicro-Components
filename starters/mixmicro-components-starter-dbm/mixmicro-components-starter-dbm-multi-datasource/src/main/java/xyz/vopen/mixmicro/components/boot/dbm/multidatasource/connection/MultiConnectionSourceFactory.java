package xyz.vopen.mixmicro.components.boot.dbm.multidatasource.connection;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import xyz.vopen.mixmicro.components.boot.dbm.multidatasource.MultiDatsSourceRepositoryProperties;
import xyz.vopen.mixmicro.components.enhance.dbm.support.ConnectionSource;
import xyz.vopen.mixmicro.components.boot.dbm.multidatasource.exception.MultiDataSourceRepositoryException;

/**
 * {@link MultiConnectionSourceFactory}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
public interface MultiConnectionSourceFactory {

  /**
   * Get {@link MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties}
   *
   * @param name data source name
   * @return instance of {@link MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties}
   * @throws MultiDataSourceRepositoryException maybe thrown {@link MultiDataSourceRepositoryException}
   */
  @Nullable
  MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties getDataSourceProperties(String name)
      throws MultiDataSourceRepositoryException;

  /**
   * Get {@link ConnectionSource} with {@link
   * MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties} config
   *
   * @param name data source name
   * @return instance of {@link ConnectionSource}
   * @throws MultiDataSourceRepositoryException maybe thrown {@link MultiDataSourceRepositoryException}
   */
  @NonNull
  ConnectionSource getConnectionSource(String name) throws MultiDataSourceRepositoryException;
}
