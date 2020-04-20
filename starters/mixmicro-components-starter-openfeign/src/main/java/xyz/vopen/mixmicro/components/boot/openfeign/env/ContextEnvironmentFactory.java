package xyz.vopen.mixmicro.components.boot.openfeign.env;

import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import xyz.vopen.mixmicro.components.boot.openfeign.OpenFeignConfigProperties;

/**
 * {@link ContextEnvironmentFactory}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-17.
 */
public class ContextEnvironmentFactory {

  private Environment environment;

  private OpenFeignConfigProperties properties;

  /**
   * Set the {@code Environment} that this component runs in.
   *
   * @param environment env instance
   */
  public void setEnvironment(
      @NonNull Environment environment, OpenFeignConfigProperties properties) {
    this.environment = environment;
    this.properties = properties;
  }

  private ContextEnvironmentFactory() {}

  private static class InstanceHolder {
    private static final ContextEnvironmentFactory INSTANCE = new ContextEnvironmentFactory();
  }

  public static ContextEnvironmentFactory instance() {
    return InstanceHolder.INSTANCE;
  }

  public OpenFeignConfigProperties openFeignConfigProperties() {
    return properties;
  }

  /**
   * Return the property value associated with the given key, or {@code defaultValue} if the key
   * cannot be resolved.
   *
   * @param key the property name to resolve
   * @param defaultValue the default value to return if no value is found
   * @see Environment#getRequiredProperty(String)
   * @see Environment#getProperty(String, Class)
   */
  public String getProperty(String key, String defaultValue) {
    return getProperty(key, String.class, defaultValue);
  }

  /**
   * Return the property value associated with the given key, or {@code defaultValue} if the key
   * cannot be resolved.
   *
   * @param key the property name to resolve
   * @param targetType the expected type of the property value
   * @param defaultValue the default value to return if no value is found
   * @see Environment#getRequiredProperty(String, Class)
   */
  public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
    return environment.getProperty(key, targetType, defaultValue);
  }
}
