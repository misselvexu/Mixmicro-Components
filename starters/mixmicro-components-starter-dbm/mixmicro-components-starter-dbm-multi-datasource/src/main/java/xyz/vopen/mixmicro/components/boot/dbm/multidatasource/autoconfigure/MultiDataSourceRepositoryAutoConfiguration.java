package xyz.vopen.mixmicro.components.boot.dbm.multidatasource.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.vopen.mixmicro.components.boot.dbm.multidatasource.MultiDatsSourceRepositoryProperties;
import xyz.vopen.mixmicro.components.boot.dbm.multidatasource.connection.DefaultMultiConnectionSourceFactory;
import xyz.vopen.mixmicro.components.boot.dbm.multidatasource.connection.MultiConnectionSourceFactory;

/**
 * {@link MultiDataSourceRepositoryAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
@Configuration
@EnableConfigurationProperties(MultiDatsSourceRepositoryProperties.class)
public class MultiDataSourceRepositoryAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  MultiConnectionSourceFactoryBuilder multiConnectionSourceFactoryBuilder(ApplicationContext context, MultiDatsSourceRepositoryProperties properties) {
    return new MultiConnectionSourceFactoryBuilder(context, properties);
  }

  static class MultiConnectionSourceFactoryBuilder implements InitializingBean {

    private final ConfigurableApplicationContext context;

    private final MultiDatsSourceRepositoryProperties properties;

    MultiConnectionSourceFactoryBuilder(
        ApplicationContext context, MultiDatsSourceRepositoryProperties properties) {
      this.context = (ConfigurableApplicationContext) context;
      this.properties = properties;
    }

    @Override
    public void afterPropertiesSet() {
      MultiConnectionSourceFactory factory = new DefaultMultiConnectionSourceFactory(this.properties.getDatasources());
      this.context.getBeanFactory().registerSingleton(DefaultMultiConnectionSourceFactory.CONNECTION_SOURCE_FACTORY_BEAN_NAME, factory);
    }
  }
}
