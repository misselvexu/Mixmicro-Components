package xyz.vopen.mixmicro.components.boot.security.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;

/** @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a> */
@Configuration
@Import({EncryptablePropertyResolverConfiguration.class, CachingConfiguration.class})
public class EncryptablePropertySourceConfiguration {

  @Bean
  public static EncryptablePropertySourceBeanFactoryPostProcessor
      encryptablePropertySourceAnnotationPostProcessor(ConfigurableEnvironment env) {
    return new EncryptablePropertySourceBeanFactoryPostProcessor(env);
  }
}
