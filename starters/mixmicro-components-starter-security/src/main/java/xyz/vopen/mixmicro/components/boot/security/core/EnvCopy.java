package xyz.vopen.mixmicro.components.boot.security.core;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import xyz.vopen.mixmicro.components.boot.security.EncryptablePropertySource;

import java.util.Optional;

/**
 * Need a copy of the environment without the Enhanced property sources to avoid circular
 * dependencies.
 */
public class EnvCopy {
  StandardEnvironment copy;

  @SuppressWarnings({"rawtypes", "ConstantConditions"})
  public EnvCopy(final ConfigurableEnvironment environment) {
    copy = new StandardEnvironment();
    Optional.ofNullable(environment.getPropertySources())
        .ifPresent(
            sources ->
                sources.forEach(
                    ps -> {
                      final PropertySource<?> original =
                          ps instanceof EncryptablePropertySource
                              ? ((EncryptablePropertySource) ps).getDelegate()
                              : ps;
                      copy.getPropertySources().addLast(original);
                    }));
  }

  public ConfigurableEnvironment get() {
    return copy;
  }
}
