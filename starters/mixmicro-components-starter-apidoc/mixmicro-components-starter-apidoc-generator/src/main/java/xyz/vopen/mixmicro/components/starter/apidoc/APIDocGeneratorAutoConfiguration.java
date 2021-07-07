package xyz.vopen.mixmicro.components.starter.apidoc;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
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
public class APIDocGeneratorAutoConfiguration {
  /** APIDoc boot Properties */
  private final APIDocProperties apiDocProperties;

  public APIDocGeneratorAutoConfiguration(APIDocProperties apiDocProperties) {
    this.apiDocProperties = apiDocProperties;
  }

  /**
   * register a APIDocGenRepository by MongoTemplate
   *
   * @return APIDocGenRepository
   */
  @Bean
  @ConditionalOnProperty(
      prefix = APIDocProperties.API_DOC_PREFIX,
      name = "gen-local",
      havingValue = "true",
      matchIfMissing = true)
  public APIDocGenRepository apiDocGenRepository() {
    MongoClient mongoClient = MongoClients.create(apiDocProperties.getMongoUri());
    return new APIDocGenRepositoryImpl(
        new MongoTemplate(mongoClient, apiDocProperties.getApiDatabase()));
  }

  @Bean
  @ConditionalOnBean(APIDocGenRepository.class)
  public APIDocGeneratorInitialization apiDocGeneratorInitialization(
      APIDocGenRepository apiDocGenRepository) {
    return new APIDocGeneratorInitialization(apiDocProperties, apiDocGenRepository);
  }
}
