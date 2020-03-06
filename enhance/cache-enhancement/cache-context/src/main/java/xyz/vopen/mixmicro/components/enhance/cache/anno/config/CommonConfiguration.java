package xyz.vopen.mixmicro.components.enhance.cache.anno.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.ConfigMap;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
@Configuration
public class CommonConfiguration {
  @Bean
  @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
  public ConfigMap mixcacheConfigMap() {
    return new ConfigMap();
  }
}
