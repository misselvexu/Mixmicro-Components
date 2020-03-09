package xyz.vopen.mixmicro.components.boot.security.core;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyDetector;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyFilter;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyResolver;
import xyz.vopen.mixmicro.components.boot.security.detector.DefaultLazyPropertyDetector;
import xyz.vopen.mixmicro.components.boot.security.encryptor.DefaultLazyEncryptor;
import xyz.vopen.mixmicro.components.boot.security.filter.DefaultLazyPropertyFilter;
import xyz.vopen.mixmicro.components.boot.security.properties.MixmicroSecurityEncryptorConfigurationProperties;
import xyz.vopen.mixmicro.components.boot.security.resolver.DefaultLazyPropertyResolver;
import xyz.vopen.mixmicro.components.boot.security.util.Singleton;
import xyz.vopen.mixmicro.components.enhance.security.encryption.StringEncryptor;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
@Configuration
public class EncryptablePropertyResolverConfiguration {

  private static final String ENCRYPTOR_BEAN_PROPERTY = "mixmicro.security.encryptor.bean";
  private static final String ENCRYPTOR_BEAN_PLACEHOLDER =
      String.format("${%s:mixmicroSecurityStringEncryptor}", ENCRYPTOR_BEAN_PROPERTY);
  private static final String DETECTOR_BEAN_PROPERTY =
      "mixmicro.security.encryptor.property.detector-bean";
  private static final String DETECTOR_BEAN_PLACEHOLDER =
      String.format("${%s:encryptablePropertyDetector}", DETECTOR_BEAN_PROPERTY);
  private static final String RESOLVER_BEAN_PROPERTY =
      "mixmicro.security.encryptor.property.resolver-bean";
  private static final String RESOLVER_BEAN_PLACEHOLDER =
      String.format("${%s:encryptablePropertyResolver}", RESOLVER_BEAN_PROPERTY);
  private static final String FILTER_BEAN_PROPERTY =
      "mixmicro.security.encryptor.property.filter-bean";
  private static final String FILTER_BEAN_PLACEHOLDER =
      String.format("${%s:encryptablePropertyFilter}", FILTER_BEAN_PROPERTY);

  private static final String ENCRYPTOR_BEAN_NAME = "lazyMixSecurityStringEncryptor";
  private static final String DETECTOR_BEAN_NAME = "lazyEncryptablePropertyDetector";
  private static final String CONFIG_SINGLETON = "configPropsSingleton";
  static final String RESOLVER_BEAN_NAME = "lazyEncryptablePropertyResolver";
  static final String FILTER_BEAN_NAME = "lazyEncryptablePropertyFilter";

  @Bean
  public EnvCopy envCopy(final ConfigurableEnvironment environment) {
    return new EnvCopy(environment);
  }

  @Bean(name = ENCRYPTOR_BEAN_NAME)
  public StringEncryptor stringEncryptor(final EnvCopy envCopy, final BeanFactory bf) {
    final String customEncryptorBeanName =
        envCopy.get().resolveRequiredPlaceholders(ENCRYPTOR_BEAN_PLACEHOLDER);
    final boolean isCustom = envCopy.get().containsProperty(ENCRYPTOR_BEAN_PROPERTY);
    return new DefaultLazyEncryptor(envCopy.get(), customEncryptorBeanName, isCustom, bf);
  }

  @Bean(name = DETECTOR_BEAN_NAME)
  public EncryptablePropertyDetector encryptablePropertyDetector(
      final EnvCopy envCopy, final BeanFactory bf) {
    final String customDetectorBeanName =
        envCopy.get().resolveRequiredPlaceholders(DETECTOR_BEAN_PLACEHOLDER);
    final boolean isCustom = envCopy.get().containsProperty(DETECTOR_BEAN_PROPERTY);
    return new DefaultLazyPropertyDetector(envCopy.get(), customDetectorBeanName, isCustom, bf);
  }

  @Bean(name = CONFIG_SINGLETON)
  public Singleton<MixmicroSecurityEncryptorConfigurationProperties> configProps(
      final EnvCopy envCopy) {
    return new Singleton<>(
        () -> MixmicroSecurityEncryptorConfigurationProperties.bindConfigProps(envCopy.get()));
  }

  @Bean(name = FILTER_BEAN_NAME)
  public EncryptablePropertyFilter encryptablePropertyFilter(
      final EnvCopy envCopy, final ConfigurableBeanFactory bf) {
    final String customFilterBeanName =
        envCopy.get().resolveRequiredPlaceholders(FILTER_BEAN_PLACEHOLDER);
    final boolean isCustom = envCopy.get().containsProperty(FILTER_BEAN_PROPERTY);
    return new DefaultLazyPropertyFilter(envCopy.get(), customFilterBeanName, isCustom, bf);
  }

  @Bean(name = RESOLVER_BEAN_NAME)
  public EncryptablePropertyResolver encryptablePropertyResolver(
      @Qualifier(DETECTOR_BEAN_NAME) final EncryptablePropertyDetector propertyDetector,
      @Qualifier(ENCRYPTOR_BEAN_NAME) final StringEncryptor encryptor,
      final BeanFactory bf,
      final EnvCopy envCopy,
      final ConfigurableEnvironment environment) {
    final String customResolverBeanName =
        envCopy.get().resolveRequiredPlaceholders(RESOLVER_BEAN_PLACEHOLDER);
    final boolean isCustom = envCopy.get().containsProperty(RESOLVER_BEAN_PROPERTY);
    return new DefaultLazyPropertyResolver(
        propertyDetector, encryptor, customResolverBeanName, isCustom, bf, environment);
  }
}
