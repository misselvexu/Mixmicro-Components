package xyz.vopen.mixmicro.components.boot.security.detector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyDetector;
import xyz.vopen.mixmicro.components.boot.security.properties.MixmicroSecurityEncryptorConfigurationProperties;
import xyz.vopen.mixmicro.components.boot.security.util.Functional;
import xyz.vopen.mixmicro.components.boot.security.util.Singleton;

import java.util.Optional;

/**
 * Default Lazy property detector that delegates to a custom {@link EncryptablePropertyDetector}
 * bean or initializes a default {@link DefaultPropertyDetector}.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
@Slf4j
public class DefaultLazyPropertyDetector implements EncryptablePropertyDetector {

  private Singleton<EncryptablePropertyDetector> singleton;

  public DefaultLazyPropertyDetector(
      ConfigurableEnvironment environment,
      String customDetectorBeanName,
      boolean isCustom,
      BeanFactory bf) {
    singleton =
        new Singleton<>(
            () ->
                Optional.of(customDetectorBeanName)
                    .filter(bf::containsBean)
                    .map(name -> (EncryptablePropertyDetector) bf.getBean(name))
                    .map(
                        Functional.tap(
                            bean ->
                                log.info(
                                    "Found Custom Detector Bean {} with name: {}",
                                    bean,
                                    customDetectorBeanName)))
                    .orElseGet(
                        () -> {
                          if (isCustom) {
                            throw new IllegalStateException(
                                String.format(
                                    "Property Detector custom Bean not found with name '%s'",
                                    customDetectorBeanName));
                          }
                          log.info(
                              "Property Detector custom Bean not found with name '{}'. Initializing Default Property Detector",
                              customDetectorBeanName);
                          return createDefault(environment);
                        }));
  }

  public DefaultLazyPropertyDetector(ConfigurableEnvironment environment) {
    singleton = new Singleton<>(() -> createDefault(environment));
  }

  private DefaultPropertyDetector createDefault(ConfigurableEnvironment environment) {
    MixmicroSecurityEncryptorConfigurationProperties props =
        MixmicroSecurityEncryptorConfigurationProperties.bindConfigProps(environment);
    return new DefaultPropertyDetector(
        props.getProperty().getPrefix(), props.getProperty().getSuffix());
  }

  @Override
  public boolean isEncrypted(String property) {
    return singleton.get().isEncrypted(property);
  }

  @Override
  public String unwrapEncryptedValue(String property) {
    return singleton.get().unwrapEncryptedValue(property);
  }
}
