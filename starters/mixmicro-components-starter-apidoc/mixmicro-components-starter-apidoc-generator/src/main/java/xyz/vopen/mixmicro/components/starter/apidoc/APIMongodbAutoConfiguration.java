package xyz.vopen.mixmicro.components.starter.apidoc;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/15
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({MongoProperties.class})
@ConditionalOnProperty(
    prefix = APIDocProperties.API_DOC_PREFIX,
    name = "gen-local",
    havingValue = "false",
    matchIfMissing = true)
@ConditionalOnMissingBean(type = {"org.springframework.data.mongodb.MongoDbFactory"})
@ConditionalOnClass({MongoClient.class})
public class APIMongodbAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(type = {"com.mongodb.MongoClient", "com.mongodb.client.MongoClient"})
  public MongoClient mongo(
      MongoProperties properties,
      ObjectProvider<MongoClientOptions> options,
      Environment environment) {
    return (new MongoClientFactory(properties, environment))
        .createMongoClient((MongoClientOptions) options.getIfAvailable());
  }
}
