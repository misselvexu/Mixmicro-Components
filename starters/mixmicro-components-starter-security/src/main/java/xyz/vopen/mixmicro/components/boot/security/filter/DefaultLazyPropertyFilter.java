package xyz.vopen.mixmicro.components.boot.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertyFilter;
import xyz.vopen.mixmicro.components.boot.security.properties.MixmicroSecurityEncryptorConfigurationProperties;
import xyz.vopen.mixmicro.components.boot.security.util.Singleton;

import java.util.Optional;

import static xyz.vopen.mixmicro.components.boot.security.util.Functional.tap;

@Slf4j
public class DefaultLazyPropertyFilter implements EncryptablePropertyFilter {

  private Singleton<EncryptablePropertyFilter> singleton;

  public DefaultLazyPropertyFilter(
      ConfigurableEnvironment e, String customFilterBeanName, boolean isCustom, BeanFactory bf) {
    singleton =
        new Singleton<>(
            () ->
                Optional.of(customFilterBeanName)
                    .filter(bf::containsBean)
                    .map(name -> (EncryptablePropertyFilter) bf.getBean(name))
                    .map(
                        tap(
                            bean ->
                                log.info(
                                    "Found Custom Filter Bean {} with name: {}",
                                    bean,
                                    customFilterBeanName)))
                    .orElseGet(
                        () -> {
                          if (isCustom) {
                            throw new IllegalStateException(
                                String.format(
                                    "Property Filter custom Bean not found with name '%s'",
                                    customFilterBeanName));
                          }

                          log.info(
                              "Property Filter custom Bean not found with name '{}'. Initializing Default Property Filter",
                              customFilterBeanName);
                          return createDefault(e);
                        }));
  }

  public DefaultLazyPropertyFilter(ConfigurableEnvironment environment) {
    singleton = new Singleton<>(() -> createDefault(environment));
  }

  private DefaultPropertyFilter createDefault(ConfigurableEnvironment environment) {
    MixmicroSecurityEncryptorConfigurationProperties props =
        MixmicroSecurityEncryptorConfigurationProperties.bindConfigProps(environment);
    final MixmicroSecurityEncryptorConfigurationProperties.PropertyConfigurationProperties
            .FilterConfigurationProperties
        filterConfig = props.getProperty().getFilter();
    return new DefaultPropertyFilter(
        filterConfig.getIncludeSources(),
        filterConfig.getExcludeSources(),
        filterConfig.getIncludeNames(),
        filterConfig.getExcludeNames());
  }

  @Override
  public boolean shouldInclude(PropertySource<?> source, String name) {
    return singleton.get().shouldInclude(source, name);
  }
}
