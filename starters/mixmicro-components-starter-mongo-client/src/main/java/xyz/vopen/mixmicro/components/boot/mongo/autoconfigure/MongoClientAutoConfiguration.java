package xyz.vopen.mixmicro.components.boot.mongo.autoconfigure;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import xyz.vopen.mixmicro.components.boot.mongo.MongoClientProperties;
import xyz.vopen.mixmicro.components.mongo.client.Datastore;
import xyz.vopen.mixmicro.components.mongo.client.MixmicroMongo;

/**
 * {@link MongoClientAutoConfiguration}
 *
 * <p>Class MongoClientAutoConfiguration Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/9/3
 */
@Configuration
@EnableConfigurationProperties(MongoClientProperties.class)
public class MongoClientAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(MongoClientAutoConfiguration.class);

  @Bean
  @Primary
  public Datastore datastore(MongoClientProperties properties) {

    MixmicroMongo mongo = new MixmicroMongo();

    mongo.mapPackage(properties.getBasePackage());

    MongoClientOptions.Builder builder =
        new MongoClientOptions.Builder()
            .connectTimeout(properties.getConnectionTimeout())
            .maxWaitTime(properties.getMaxWaitTime());

    MongoClientURI uri = new MongoClientURI(properties.getUri(), builder);

    MongoClient client = new MongoClient(uri);

    Datastore datastore = mongo.createDatastore(client, properties.getDatabaseName());

    if (properties.isEnsureIndexes()) {
      datastore.ensureIndexes();
      log.info("[MIXMICRO-MONGO] mongo model's index is ensured.");
    }

    log.info("[MIXMICRO-MONGO] mongo client is initialized , instance :{}", datastore);

    return datastore;
  }
}
