package xyz.vopen.mixmicro.components.boot.security.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.Environment;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyDetector;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyResolver;
import xyz.vopen.mixmicro.components.boot.security.util.Singleton;
import xyz.vopen.mixmicro.components.enhance.security.encryption.StringEncryptor;

import java.util.Optional;

import static xyz.vopen.mixmicro.components.boot.security.util.Functional.tap;

/**
 * Default Resolver bean that delegates to a custom defined {@link EncryptablePropertyResolver} or
 * creates a new {@link DefaultPropertyResolver}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Slf4j
public class DefaultLazyPropertyResolver implements EncryptablePropertyResolver {

  private Singleton<EncryptablePropertyResolver> singleton;

  public DefaultLazyPropertyResolver(
      EncryptablePropertyDetector propertyDetector,
      StringEncryptor encryptor,
      String customResolverBeanName,
      boolean isCustom,
      BeanFactory bf,
      Environment environment) {
    singleton =
        new Singleton<>(
            () ->
                Optional.of(customResolverBeanName)
                    .filter(bf::containsBean)
                    .map(name -> (EncryptablePropertyResolver) bf.getBean(name))
                    .map(
                        tap(
                            bean ->
                                log.info(
                                    "Found Custom Resolver Bean {} with name: {}",
                                    bean,
                                    customResolverBeanName)))
                    .orElseGet(
                        () -> {
                          if (isCustom) {
                            throw new IllegalStateException(
                                String.format(
                                    "Property Resolver custom Bean not found with name '%s'",
                                    customResolverBeanName));
                          }
                          log.info(
                              "Property Resolver custom Bean not found with name '{}'. Initializing Default Property Resolver",
                              customResolverBeanName);
                          return createDefault(propertyDetector, encryptor, environment);
                        }));
  }

  public DefaultLazyPropertyResolver(
      EncryptablePropertyDetector propertyDetector,
      StringEncryptor encryptor,
      Environment environment) {
    singleton = new Singleton<>(() -> createDefault(propertyDetector, encryptor, environment));
  }

  private DefaultPropertyResolver createDefault(
      EncryptablePropertyDetector propertyDetector,
      StringEncryptor encryptor,
      Environment environment) {
    return new DefaultPropertyResolver(encryptor, propertyDetector, environment);
  }

  @Override
  public String resolvePropertyValue(String value) {
    return singleton.get().resolvePropertyValue(value);
  }
}
