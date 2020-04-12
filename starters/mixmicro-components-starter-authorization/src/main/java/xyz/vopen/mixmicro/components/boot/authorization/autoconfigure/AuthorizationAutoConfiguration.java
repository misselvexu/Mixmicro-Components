package xyz.vopen.mixmicro.components.boot.authorization.autoconfigure;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import xyz.vopen.mixmicro.components.authorization.Serialization;
import xyz.vopen.mixmicro.components.authorization.api.AuthorizationService;
import xyz.vopen.mixmicro.components.authorization.api.DefaultAuthorizationService;
import xyz.vopen.mixmicro.components.boot.authorization.AuthorizationConfigProperties;

/**
 * {@link AuthorizationAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020-04-11.
 */
@Configuration
@EnableConfigurationProperties(AuthorizationConfigProperties.class)
public class AuthorizationAutoConfiguration implements BeanClassLoaderAware {

  private ClassLoader classLoader;

  // DEFAULT CONSTRUCTOR

  public AuthorizationAutoConfiguration() {}

  @Bean
  AuthorizationService authorizationService(AuthorizationConfigProperties properties) {
    return new DefaultAuthorizationService(properties, new Serialization.AuthorizationKeyLoader(classLoader));
  }

  /**
   * Callback that supplies the bean {@link ClassLoader class loader} to
   * a bean instance.
   * <p>Invoked <i>after</i> the population of normal bean properties but
   * <i>before</i> an initialization callback such as
   * {@link InitializingBean InitializingBean's}
   * {@link InitializingBean#afterPropertiesSet()}
   * method or a custom init-method.
   *
   * @param classLoader the owning class loader
   */
  @Override
  public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
    this.classLoader = classLoader;
  }
}
