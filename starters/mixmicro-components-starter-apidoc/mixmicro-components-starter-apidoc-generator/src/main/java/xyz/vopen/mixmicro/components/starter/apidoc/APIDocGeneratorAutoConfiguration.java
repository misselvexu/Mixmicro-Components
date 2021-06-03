package xyz.vopen.mixmicro.components.starter.apidoc;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import xyz.vopen.mixmicro.components.enhance.apidoc.repository.APIDocGenRepository;
import xyz.vopen.mixmicro.components.enhance.apidoc.repository.impl.APIDocGenRepositoryImpl;

/**
 * APIDoc autoConfiguration
 *
 * @author <a href="mailto:tangtongda@gmail.com">Tino.Tang</a>
 * @version ${project.version} - 2021/6/2
 */
@Configuration
@ConditionalOnClass(EnableAPIDocClient.class)
@EnableConfigurationProperties(APIDocProperties.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@ConditionalOnWebApplication
@EnableAsync
// EnableAsync@Import({
//  MongoAutoConfiguration.class,
//  MongoDataAutoConfiguration.class,
//  MongoRepositoriesAutoConfiguration.class
// })
public class APIDocGeneratorAutoConfiguration {

  /** logger instance */
  static Logger logger = LoggerFactory.getLogger(APIDocGeneratorAutoConfiguration.class);
  /** APIDoc boot Properties */
  private final APIDocProperties apiDocProperties;

  public APIDocGeneratorAutoConfiguration(APIDocProperties apiDocProperties) {
    this.apiDocProperties = apiDocProperties;
  }

  /*
   * Use the standard Mongo driver API to create a com.mongodb.client.MongoClient instance.
   */
  @Bean
  @ConditionalOnProperty(
      prefix = APIDocProperties.API_DOC_PREFIX,
      name = "gen-local",
      havingValue = "true",
      matchIfMissing = true)
  public MongoClient mongoClient() {
    return MongoClients.create("mongodb://apidoc:apidoc@dev-middle.hgj.net:27017/apidoc");
  }

  /**
   * Registering a com.mongodb.client.MongoClient object and enabling Spring’s exception translation
   * support
   *
   * @param mongoClient com.mongodb.client.MongoClient
   * @return MongoTemplate
   */
  @Bean
  @ConditionalOnBean(MongoClient.class)
  public MongoTemplate mongoTemplate(MongoClient mongoClient) {
    return new MongoTemplate(mongoClient, "apidoc");
  }

  /**
   * register a APIDocGenRepository by MongoTemplate
   *
   * @param mongoTemplate MongoTemplate
   * @return APIDocGenRepository
   */
  @Bean
  @ConditionalOnBean(MongoTemplate.class)
  public APIDocGenRepository apiDocGenRepository(MongoTemplate mongoTemplate) {
    return new APIDocGenRepositoryImpl(mongoTemplate);
  }

  @Bean
  @ConditionalOnBean(APIDocGenRepository.class)
  public APIDocGeneratorInitialization apiDocGeneratorInitialization(
      APIDocGenRepository apiDocGenRepository) {
    return new APIDocGeneratorInitialization(apiDocProperties, apiDocGenRepository);
  }
}
