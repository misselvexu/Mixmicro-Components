package xyz.vopen.mixmicro.components.boot.security.environment;

import lombok.Builder;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import xyz.vopen.mixmicro.components.boot.security.*;
import xyz.vopen.mixmicro.components.boot.security.core.EnvCopy;
import xyz.vopen.mixmicro.components.boot.security.detector.DefaultLazyPropertyDetector;
import xyz.vopen.mixmicro.components.boot.security.encryptor.DefaultLazyEncryptor;
import xyz.vopen.mixmicro.components.boot.security.filter.DefaultLazyPropertyFilter;
import xyz.vopen.mixmicro.components.boot.security.resolver.DefaultLazyPropertyResolver;
import xyz.vopen.mixmicro.components.enhance.security.encryption.StringEncryptor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A custom {@link ConfigurableEnvironment} that is useful for early access of encrypted properties
 * on bootstrap. While not required in most scenarios could be useful when customizing Spring Boot's
 * init behavior or integrating with certain capabilities that are configured very early, such as
 * Logging core. For a concrete example, this method of enabling encryptable properties is
 * the only one that works with Spring Properties replacement in logback-spring.xml files, using the
 * springProperty tag
 */
public class StandardEncryptableEnvironment extends StandardEnvironment
    implements ConfigurableEnvironment {

  private MutablePropertySources encryptablePropertySources;
  private MutablePropertySources originalPropertySources;

  public StandardEncryptableEnvironment() {
    this(null, null, null, null, null, null);
  }

  /**
   * Create a new Encryptable Environment. All arguments are optional, provide null if default value
   * is desired.
   *
   * @param interceptionMode The interception method to utilize, or null (Default is {@link
   *     InterceptionMode#WRAPPER})
   * @param skipPropertySourceClasses A list of {@link PropertySource} classes to skip from
   *     interception, or null (Default is empty)
   * @param resolver The property resolver to utilize, or null (Default is {@link
   *     DefaultLazyPropertyResolver} which will resolve to specified core)
   * @param filter The property filter to utilize, or null (Default is {@link
   *     DefaultLazyPropertyFilter} which will resolve to specified core)
   * @param encryptor The string encryptor to utilize, or null (Default is {@link
   *     DefaultLazyEncryptor} which will resolve to specified core)
   * @param detector The property detector to utilize, or null (Default is {@link
   *     DefaultLazyPropertyDetector} which will resolve to specified core)
   */
  @Builder
  public StandardEncryptableEnvironment(
      InterceptionMode interceptionMode,
      List<Class<PropertySource<?>>> skipPropertySourceClasses,
      EncryptablePropertyResolver resolver,
      EncryptablePropertyFilter filter,
      StringEncryptor encryptor,
      EncryptablePropertyDetector detector) {
    InterceptionMode actualInterceptionMode =
        Optional.ofNullable(interceptionMode).orElse(InterceptionMode.WRAPPER);
    List<Class<PropertySource<?>>> actualSkipPropertySourceClasses =
        Optional.ofNullable(skipPropertySourceClasses).orElseGet(Collections::emptyList);
    EnvCopy envCopy = new EnvCopy(this);
    EncryptablePropertyFilter actualFilter =
        Optional.ofNullable(filter).orElseGet(() -> new DefaultLazyPropertyFilter(envCopy.get()));
    StringEncryptor actualEncryptor =
        Optional.ofNullable(encryptor).orElseGet(() -> new DefaultLazyEncryptor(envCopy.get()));
    EncryptablePropertyDetector actualDetector =
        Optional.ofNullable(detector)
            .orElseGet(() -> new DefaultLazyPropertyDetector(envCopy.get()));
    EncryptablePropertyResolver actualResolver =
        Optional.ofNullable(resolver)
            .orElseGet(
                () -> new DefaultLazyPropertyResolver(actualDetector, actualEncryptor, this));
    EncryptablePropertySourceConverter.convertPropertySources(
        actualInterceptionMode,
        actualSkipPropertySourceClasses,
        actualResolver,
        actualFilter,
        originalPropertySources);
    this.encryptablePropertySources =
        EncryptablePropertySourceConverter.proxyPropertySources(
            actualInterceptionMode,
            actualSkipPropertySourceClasses,
            actualResolver,
            actualFilter,
            originalPropertySources,
            envCopy);
  }

  @Override
  protected void customizePropertySources(MutablePropertySources propertySources) {
    super.customizePropertySources(propertySources);
    this.originalPropertySources = propertySources;
  }

  @Override
  public MutablePropertySources getPropertySources() {
    return this.encryptablePropertySources;
  }
}
