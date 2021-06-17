package xyz.vopen.mixmicro.components.boot.snowflake;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import xyz.vopen.mixmicro.kits.lang.NonNull;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Properties;

import static java.util.Collections.emptyMap;

/**
 * Snowflake Bean Builder
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 23/10/2018.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class SnowflakeBeanBuilder implements BeanFactoryAware, EnvironmentAware {

  /** The bean name of {@link SnowflakeBeanBuilder} */
  public static final String BEAN_NAME = "snowflakeBeanBuilder";

  public static final String PROPERTIES_WORKID = "workerId";

  public static final String PROPERTIES_AUTO = "auto";

  private Environment environment;

  private BeanFactory beanFactory;

  public SnowflakeBeanBuilder() {}

  public Snowflake build() {
    if (beanFactory.containsBean(SnowflakeBeanRegistrar.SNOWFLAKE_BEAN_NAME)) {
      return beanFactory.getBean(SnowflakeBeanRegistrar.SNOWFLAKE_BEAN_NAME, Snowflake.class);
    }

    try {
      Properties globalProperties = getProperties(beanFactory, SnowflakeBeanRegistrar.GLOBAL_SNOWFLAKE_PROPERTIES_BEAN_NAME);

      Long workId = Long.parseLong(globalProperties.getProperty(PROPERTIES_WORKID, "1"));
      Boolean auto = Boolean.parseBoolean(globalProperties.getProperty(PROPERTIES_AUTO, "true"));
      System.out.println("[Mixmicro Snowflake Properties , WorkId: " + workId + "] , auto : [" + auto + "]");

      Class<?> snowflakeClass = Class.forName("xyz.vopen.mixmicro.components.boot.snowflake.Snowflake");
      Constructor<?> constructor = snowflakeClass.getDeclaredConstructor(long.class, boolean.class);

      return (Snowflake) constructor.newInstance(workId, auto);
    } catch (Throwable e) {
      throw new IllegalArgumentException("Snowflake init failed with exception: " + e.getMessage());
    }
  }

  @Override
  public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  @Override
  public void setEnvironment(@NonNull Environment environment) {
    this.environment = environment;
  }

  @SuppressWarnings("SameParameterValue")
  private Properties getProperties(@NonNull BeanFactory beanFactory, @NonNull String beanName) {

    Properties properties = new Properties();

    // If Bean is absent , source will be empty.
    Map<?, ?> propertiesSource =
        beanFactory.containsBean(beanName)
            ? beanFactory.getBean(beanName, Properties.class)
            : emptyMap();
    properties.putAll(propertiesSource);

    return properties;
  }
}
