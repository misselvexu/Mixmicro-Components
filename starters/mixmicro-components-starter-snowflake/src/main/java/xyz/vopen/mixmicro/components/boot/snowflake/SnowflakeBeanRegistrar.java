package xyz.vopen.mixmicro.components.boot.snowflake;

import static org.springframework.core.annotation.AnnotationAttributes.fromMap;

import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Snowflake Bean Registrar
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 23/10/2018.
 */
public class SnowflakeBeanRegistrar extends BaseBeanDefinitionRegistrar
    implements ImportBeanDefinitionRegistrar, EnvironmentAware, BeanFactoryAware {

  /** The bean name of global Aorp {@link Properties} */
  public static final String GLOBAL_SNOWFLAKE_PROPERTIES_BEAN_NAME = "globalSnowflakeProperties";

  public static final String SNOWFLAKE_BEAN_NAME = "snowflake";

  private Environment environment;

  private BeanFactory beanFactory;

  @Override
  public void setEnvironment(Environment environment) {
    this.environment = environment;
  }

  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

    BeanDefinition annotationProcessor =
        BeanDefinitionBuilder.genericBeanDefinition(PropertySourcesPlaceholderConfigurer.class)
            .getBeanDefinition();

    registry.registerBeanDefinition(
        PropertySourcesPlaceholderConfigurer.class.getName(), annotationProcessor);

    AnnotationAttributes attributes =
        fromMap(metadata.getAnnotationAttributes(EnableSnowflake.class.getName()));

    registerGlobalSnowflakeProperties(
        attributes, registry, environment, GLOBAL_SNOWFLAKE_PROPERTIES_BEAN_NAME);

    registerPropertySourcesPlaceholderConfigurer(registry);

    registerInfrastructureBeanIfAbsent(
        registry, SnowflakeBeanBuilder.BEAN_NAME, SnowflakeBeanBuilder.class);

    SnowflakeBeanBuilder snowflakeBeanBuilder = beanFactory.getBean(SnowflakeBeanBuilder.class);
    Snowflake snowflake = snowflakeBeanBuilder.build();
    System.out.println("\r\n[Mixmicro Snowflake Inited : " + snowflake + "]");
    registerSingleton(registry, SNOWFLAKE_BEAN_NAME, snowflake);
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }
}
