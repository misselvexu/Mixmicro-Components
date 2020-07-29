package xyz.vopen.mixmicro.components.boot.dbm.multidatasource.connection;

import com.google.common.collect.Maps;
import lombok.Builder;
import org.springframework.lang.NonNull;
import xyz.vopen.mixmicro.components.boot.dbm.multidatasource.MultiDatsSourceRepositoryProperties;
import xyz.vopen.mixmicro.components.enhance.dbm.jdbc.JdbcConnectionSource;
import xyz.vopen.mixmicro.components.enhance.dbm.jdbc.JdbcPooledConnectionSource;
import xyz.vopen.mixmicro.components.enhance.dbm.support.ConnectionSource;
import xyz.vopen.mixmicro.kits.Assert;
import xyz.vopen.mixmicro.components.boot.dbm.multidatasource.exception.MultiDataSourceRepositoryException;

import java.util.Map;

/**
 * {@link DefaultMultiConnectionSourceFactory}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
public class DefaultMultiConnectionSourceFactory implements MultiConnectionSourceFactory {

  public static final String CONNECTION_SOURCE_FACTORY_BEAN_NAME = "defaultMultiConnectionSourceFactory";

  private final Map<String, MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties> dataSourceProperties = Maps.newHashMap();

  private final Map<String, ConnectionSource> connectionSources = Maps.newConcurrentMap();

  @Builder
  public DefaultMultiConnectionSourceFactory(Map<String, MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties> dataSourceProperties) {
    if(dataSourceProperties != null && !dataSourceProperties.isEmpty()) {
      this.dataSourceProperties.putAll(dataSourceProperties);
    }
  }

  /**
   * Get {@link MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties}
   *
   * @param name data source name
   * @return instance of {@link MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties}
   * @throws MultiDataSourceRepositoryException maybe thrown {@link MultiDataSourceRepositoryException}
   */
  @Override
  @NonNull
  public MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties getDataSourceProperties(
      String name) throws MultiDataSourceRepositoryException {
    return dataSourceProperties.get(name);
  }

  /**
   * Get {@link ConnectionSource} with {@link
   * MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties} config
   *
   * @param name data source name
   * @return instance of {@link ConnectionSource}
   * @throws MultiDataSourceRepositoryException maybe thrown {@link MultiDataSourceRepositoryException}
   */
  @Override
  @NonNull
  public ConnectionSource getConnectionSource(String name) throws MultiDataSourceRepositoryException {

    Assert.notNull(name,"DataSource name is required. ");

    if (!dataSourceProperties.containsKey(name)) {
      throw new MultiDataSourceRepositoryException("DataSource " + name + " s Properties is not found.");
    }

    MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties properties = dataSourceProperties.get(name);

    if (!connectionSources.containsKey(name)) {
      connectionSources.put(name,doInitialize(properties));
    }

    return connectionSources.get(name);
  }

  private ConnectionSource doInitialize(MultiDatsSourceRepositoryProperties.RepositoryDataSourceProperties properties) {

    try{
      if(properties.isPool()) {

        JdbcPooledConnectionSource source = new JdbcPooledConnectionSource(properties.getUrl(),properties.getUsername(),properties.getPassword());

        source.setMaxConnectionsFree(properties.getPoolConfig().getMaxConnectionsFree());
        source.setCheckConnectionsEveryMillis(properties.getPoolConfig().getCheckConnectionsEveryMillis());
        source.setMaxConnectionAgeMillis(properties.getPoolConfig().getMaxConnectionAgeMillis());
        source.setTestBeforeGet(properties.getPoolConfig().isTestBeforeGetFromPool());

        return source;

      } else {
        // NOT RECOMMEND
        return new JdbcConnectionSource(properties.getUrl(),properties.getUsername(),properties.getPassword());
      }
    } catch (Exception e) {
      throw new MultiDataSourceRepositoryException("DataSource Connection create happened exception", e);
    }
  }
}
