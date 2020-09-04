package xyz.vopen.mixmicro.components.boot.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import xyz.vopen.mixmicro.kits.StringUtils;

import static xyz.vopen.mixmicro.components.boot.mongo.MongoClientProperties.MONGO_CONFIG_PROPERTIES_PREFIX;

/**
 * {@link MongoClientProperties}
 *
 * <p>Class MongoClientProperties Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/9/3
 */
@Getter
@Setter
@ConfigurationProperties(prefix = MONGO_CONFIG_PROPERTIES_PREFIX)
public class MongoClientProperties implements InitializingBean {

  public static final String MONGO_CONFIG_PROPERTIES_PREFIX = "mixmicro.mongodb";

  private boolean enabled = true;

  /**
   * Mongo Server URI
   *
   * <p>mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]
   *
   * <p>Example:
   *
   * <p>- Replica-Set Connection URI
   *
   * <p>mongodb://root:****@server-host-01:3717,server-host-02:3717/admin?replicaSet=replica-set-name
   *
   * @see com.mongodb.MongoClientURI
   */
  private String uri;

  /**
   * Server Connection Timeout, TimeUnit: ms
   *
   * <p>default: 10 s
   */
  private int connectionTimeout = 10000;

  /**
   * Min Connections Pre Host
   * <p>default: 20</p>
   */
  private int minConnectionsPerHost = 20;

  /**
   * Max Connections Pre Host
   * <p>default: 100</p>
   */
  private int maxConnectionsPerHost = 100;

  /**
   * Server Max Wait Timeout, TimeUnit: ms
   *
   * <p>default: 10 s
   */
  private int maxWaitTime = 10000;

  /**
   * Mongo Model Mapper Scan Package.
   *
   * <p>
   */
  private String basePackage;

  /**
   * Mongo Database Name defined .
   *
   * <p>
   */
  private String databaseName;

  /**
   * Ensures (creating if necessary) the indexes found during class mapping
   *
   * <p>default: true
   */
  private boolean ensureIndexes = true;

  /**
   * Invoked by the containing {@code BeanFactory} after it has set all bean properties and
   * satisfied {@link BeanFactoryAware}, {@code ApplicationContextAware} etc.
   *
   * <p>This method allows the bean instance to perform validation of its overall configuration and
   * final initialization when all bean properties have been set.
   *
   * @throws Exception in the event of misconfiguration (such as failure to set an essential
   *     property) or if initialization fails for any other reason
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    if (enabled) {

      if (StringUtils.isBlank(uri)) {
        throw new IllegalArgumentException(
            "[MIXMICRO-MONGO] mongodb server connection uri must be supported . "
                + "template: mongodb://[username:password@]host1[:port1][,host2[:port2],...[,hostN[:portN]]][/[database][?options]]");
      }

      if (StringUtils.isBlank(databaseName)) {
        throw new IllegalArgumentException("[MIXMICRO-MONGO] mongodb server database name must be supported .");
      }

      if (StringUtils.isBlank(basePackage)) {
        throw new IllegalArgumentException("[MIXMICRO-MONGO] mongodb server mapper scan base-package must be supported .");
      }
    }
  }
}
