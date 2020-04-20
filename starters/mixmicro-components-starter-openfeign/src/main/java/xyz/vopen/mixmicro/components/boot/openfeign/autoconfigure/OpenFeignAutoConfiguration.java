package xyz.vopen.mixmicro.components.boot.openfeign.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import xyz.vopen.mixmicro.components.boot.openfeign.OpenFeignConfigProperties;
import xyz.vopen.mixmicro.components.boot.openfeign.decoder.OpenFeignInvokeErrorDecoder;
import xyz.vopen.mixmicro.components.boot.openfeign.env.ContextEnvironmentFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link OpenFeignAutoConfiguration}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/3/5
 */
@Configuration
@ConditionalOnClass(
    name = {
      "org.springframework.cloud.openfeign.FeignContext",
      "xyz.vopen.mixmicro.components.common.ResponseEntity"
    })
@EnableConfigurationProperties(OpenFeignConfigProperties.class)
public class OpenFeignAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(OpenFeignAutoConfiguration.class);

  @Bean
  public OpenFeignInvokeErrorDecoder errorDecoder() {
    return new OpenFeignInvokeErrorDecoder();
  }

  @Bean
  ContextEnvironmentFactoryInitializer contextEnvironmentFactoryInitializer(OpenFeignConfigProperties properties) {
    return new ContextEnvironmentFactoryInitializer(properties);
  }

  static class ContextEnvironmentFactoryInitializer
      implements ApplicationListener<ApplicationPreparedEvent> {

    private static final String CONTEXT_ENVIRONMENT_FACTORY_BEAN_NAME = "contextEnvironmentFactory";

    private static AtomicBoolean refreshed = new AtomicBoolean(false);

    private final OpenFeignConfigProperties properties;

    public ContextEnvironmentFactoryInitializer(OpenFeignConfigProperties properties) {
      this.properties = properties;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationPreparedEvent event) {
      if (refreshed.compareAndSet(false, true)) {
        ConfigurableListableBeanFactory beanFactory = event.getApplicationContext().getBeanFactory();
        ContextEnvironmentFactory factory = ContextEnvironmentFactory.instance();
        factory.setEnvironment(event.getApplicationContext().getEnvironment(), properties);
        beanFactory.registerSingleton(CONTEXT_ENVIRONMENT_FACTORY_BEAN_NAME, factory);
      }
    }
  }
}
