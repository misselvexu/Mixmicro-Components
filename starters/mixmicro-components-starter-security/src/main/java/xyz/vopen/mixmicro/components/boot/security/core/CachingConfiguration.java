package xyz.vopen.mixmicro.components.boot.security.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import xyz.vopen.mixmicro.components.boot.security.caching.RefreshScopeRefreshedEventListener;

import static xyz.vopen.mixmicro.components.boot.security.caching.RefreshScopeRefreshedEventListener.REFRESHED_EVENT_CLASS;

@Configuration
@ConditionalOnClass(name = REFRESHED_EVENT_CLASS)
public class CachingConfiguration {
  @Bean
  public RefreshScopeRefreshedEventListener refreshScopeRefreshedEventListener(
      ConfigurableEnvironment environment) {
    return new RefreshScopeRefreshedEventListener(environment);
  }
}
