package xyz.vopen.mixmicro.components.boot.snowflake;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import xyz.vopen.mixmicro.kits.spring.util.BeanUtils;

/**
 * Base Bean Definition Registrar Base Class
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 23/10/2018.
 */
public class BaseBeanDefinitionRegistrar {

  /** The bean name of {@link PropertySourcesPlaceholderConfigurer} */
  public static final String PLACEHOLDER_CONFIGURER_BEAN_NAME =
      "propertySourcesPlaceholderConfigurer";

  /**
   * Register Global Snowflake Properties Bean with specified name
   *
   * @param attributes the attributes of Global Snowflake Properties may contain placeholders
   * @param registry {@link BeanDefinitionRegistry}
   * @param propertyResolver {@link PropertyResolver}
   * @param beanName Bean name
   */
  public static void registerGlobalSnowflakeProperties(
      AnnotationAttributes attributes,
      BeanDefinitionRegistry registry,
      PropertyResolver propertyResolver,
      String beanName) {
    if (attributes == null) {
      return; // Compatible with null
    }
    registerGlobalSnowflakeProperties((Map<?, ?>) attributes, registry, propertyResolver, beanName);
  }

  /**
   * Register Global Snowflake Properties Bean with specified name
   *
   * @param globalPropertiesAttributes the attributes of Global Snowflake Properties may contain
   *     placeholders
   * @param registry {@link BeanDefinitionRegistry}
   * @param propertyResolver {@link PropertyResolver}
   * @param beanName Bean name
   */
  public static void registerGlobalSnowflakeProperties(
      Map<?, ?> globalPropertiesAttributes,
      BeanDefinitionRegistry registry,
      PropertyResolver propertyResolver,
      String beanName) {
    Properties globalProperties = resolveProperties(globalPropertiesAttributes, propertyResolver);
    registerSingleton(registry, beanName, globalProperties);
  }

  /**
   * Register an object to be Singleton Bean
   *
   * @param registry {@link BeanDefinitionRegistry}
   * @param beanName bean name
   * @param singletonObject singleton object
   */
  public static void registerSingleton(
      BeanDefinitionRegistry registry, String beanName, Object singletonObject) {
    SingletonBeanRegistry beanRegistry = null;
    if (registry instanceof SingletonBeanRegistry) {
      beanRegistry = (SingletonBeanRegistry) registry;
    } else if (registry instanceof AbstractApplicationContext) {
      // Maybe AbstractApplicationContext or its sub-classes
      beanRegistry = ((AbstractApplicationContext) registry).getBeanFactory();
    }
    // Register Singleton Object if possible
    if (beanRegistry != null) {
      beanRegistry.registerSingleton(beanName, singletonObject);
    }
  }

  /**
   * Resolve placeholders of properties via specified {@link PropertyResolver} if present
   *
   * @param properties The properties
   * @param propertyResolver {@link PropertyResolver} instance, for instance, {@link Environment}
   * @return a new instance of {@link Properties} after resolving.
   */
  public static Properties resolveProperties(
      Map<?, ?> properties, PropertyResolver propertyResolver) {
    PropertiesPlaceholderResolver propertiesPlaceholderResolver =
        new PropertiesPlaceholderResolver(propertyResolver);
    return propertiesPlaceholderResolver.resolve(properties);
  }

  /**
   * Register Infrastructure Bean if absent
   *
   * @param registry {@link BeanDefinitionRegistry}
   * @param beanName the name of bean
   * @param beanClass the class of bean
   * @param constructorArgs the arguments of {@link Constructor}
   */
  public static void registerInfrastructureBeanIfAbsent(
      BeanDefinitionRegistry registry,
      String beanName,
      Class<?> beanClass,
      Object... constructorArgs) {
    if (!isBeanDefinitionPresent(registry, beanName, beanClass)) {
      registerInfrastructureBean(registry, beanName, beanClass, constructorArgs);
    }
  }

  /**
   * Register Infrastructure Bean
   *
   * @param registry {@link BeanDefinitionRegistry}
   * @param beanName the name of bean
   * @param beanClass the class of bean
   * @param constructorArgs the arguments of {@link Constructor}
   */
  public static void registerInfrastructureBean(
      BeanDefinitionRegistry registry,
      String beanName,
      Class<?> beanClass,
      Object... constructorArgs) {
    // Build a BeanDefinition for SnowflakeServiceFactory class
    BeanDefinitionBuilder beanDefinitionBuilder =
        BeanDefinitionBuilder.rootBeanDefinition(beanClass);
    for (Object constructorArg : constructorArgs) {
      beanDefinitionBuilder.addConstructorArgValue(constructorArg);
    }
    // ROLE_INFRASTRUCTURE
    beanDefinitionBuilder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    // Register
    registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
  }

  /**
   * Is {@link BeanDefinition} present in {@link BeanDefinitionRegistry}
   *
   * @param registry {@link BeanDefinitionRegistry}
   * @param beanName the name of bean
   * @param targetBeanClass the type of bean
   * @return If Present , return <code>true</code>
   */
  public static boolean isBeanDefinitionPresent(
      BeanDefinitionRegistry registry, String beanName, Class<?> targetBeanClass) {
    String[] beanNames = BeanUtils.getBeanNames((ListableBeanFactory) registry, targetBeanClass);
    return ArrayUtils.contains(beanNames, beanName);
  }

  /**
   * Register {@link PropertySourcesPlaceholderConfigurer} Bean
   *
   * @param registry {@link BeanDefinitionRegistry}
   */
  public static void registerPropertySourcesPlaceholderConfigurer(BeanDefinitionRegistry registry) {
    registerInfrastructureBeanIfAbsent(
        registry, PLACEHOLDER_CONFIGURER_BEAN_NAME, PropertySourcesPlaceholderConfigurer.class);
  }
}
