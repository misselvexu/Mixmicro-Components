package xyz.vopen.mixmicro.components.boot.dbm.multidatasource;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.io.Serializable;
import java.util.Map;

import static xyz.vopen.mixmicro.components.boot.dbm.multidatasource.MultiDatsSourceRepositoryProperties.ENDPOINT_REPOSITORY_PROPERTIES_PREFIX;

/**
 * {@link MultiDatsSourceRepositoryProperties}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = ENDPOINT_REPOSITORY_PROPERTIES_PREFIX)
public class MultiDatsSourceRepositoryProperties implements Serializable {

  public static final String INTEGRATE_PROPERTIES_CONFIG_PREFIX = "mixmicro.dbm";

  public static final String ENDPOINT_REPOSITORY_PROPERTIES_PREFIX = INTEGRATE_PROPERTIES_CONFIG_PREFIX + ".repository";

  private Map<String, RepositoryDataSourceProperties> datasources = Maps.newHashMap();

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RepositoryDataSourceProperties implements Serializable {

    /**
     * DataSource Connection URL
     *
     * <p>
     */
    private String url;

    /**
     * DataBase Auth Password
     *
     * <p>
     */
    private String password;

    /**
     * DataBase Auth Username
     *
     * <p>
     */
    private String username;

    /**
     * DataBase DataSource is Enabled Pool .
     *
     * <p>default: true
     */
    private boolean pool = true;

    /**
     * DataSource Connection Pool Config Properties
     *
     * @see PoolConfig
     */
    @NestedConfigurationProperty private PoolConfig poolConfig = new PoolConfig();
  }

  @Getter
  @Setter
  public static class PoolConfig implements Serializable {

    /**
     * Set the number of connections that can be unused in the available list.
     *
     * <p>default: 5
     */
    private int maxConnectionsFree = 5;

    /**
     * There is an internal thread which checks each of the database connections as a keep-alive
     * mechanism. This set the number of milliseconds it sleeps between checks -- default is 30000.
     * To disable the checking thread, set this to 0 before you start using the connection source.
     */
    private int checkConnectionsEveryMillis = 30 * 1000;

    /**
     * Set the number of milliseconds that a connection can stay open before being closed.
     *
     * <p>default: 60 * 60 * 1000 ms
     */
    private long maxConnectionAgeMillis = 60 * 60 * 1000;

    private boolean testBeforeGetFromPool = false;
  }
}
