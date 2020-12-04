package xyz.vopen.mixmicro.components.boot.mongo.autoconfigure;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import xyz.vopen.mixmicro.components.boot.mongo.MongoClientProperties;
import xyz.vopen.mixmicro.components.mongo.client.MongoRepository;
import xyz.vopen.mixmicro.components.mongo.client.MixMongo;
import xyz.vopen.mixmicro.components.mongo.client.mapping.Mapper;
import xyz.vopen.mixmicro.components.mongo.client.mapping.MapperOptions;

import static xyz.vopen.mixmicro.components.boot.mongo.MongoClientProperties.MONGO_CONFIG_PROPERTIES_PREFIX;

/**
 * {@link MongoClientAutoConfiguration}
 *
 * <p>Class MongoClientAutoConfiguration Definition
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/9/3
 */
@Configuration
@ConditionalOnProperty(
    prefix = MONGO_CONFIG_PROPERTIES_PREFIX,
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@EnableConfigurationProperties(MongoClientProperties.class)
public class MongoClientAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(MongoClientAutoConfiguration.class);

  @Bean
  @Primary
  public MongoRepository mongoRepository(MongoClientProperties properties) {

    Mapper mapper = new Mapper();
    mapper.setOptions(MapperOptions.legacy().mapSubPackages(true).build());

    MixMongo mongo = new MixMongo(mapper);

    mongo.mapPackage(properties.getBasePackage());

    MongoClientOptions.Builder builder =
        new MongoClientOptions.Builder()
            .minConnectionsPerHost(properties.getMinConnectionsPerHost())
            .connectionsPerHost(properties.getMaxConnectionsPerHost())
            .connectTimeout(properties.getConnectionTimeout())
            .maxWaitTime(properties.getMaxWaitTime());

    MongoClientURI uri = new MongoClientURI(properties.getUri(), builder);

    MongoClient client = new MongoClient(uri);

    MongoRepository mongoRepository = mongo.createMongoRepository(client, properties.getDatabaseName());

    if (properties.isEnsureIndexes()) {
      mongoRepository.ensureIndexes();
      log.info("[MIXMICRO-MONGO] mongo model's index is ensured.");
    }

    log.info("[MIXMICRO-MONGO] mongo client is initialized , instance :{}", mongoRepository);

    return mongoRepository;
  }
}
