package xyz.vopen.framework.logging.spring.context.annotation.client;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import xyz.vopen.framework.logging.client.interceptor.web.LoggingWebInterceptor;
import xyz.vopen.framework.logging.spring.util.LoggingBeanUtils;

/**
 * Register logging client beans {@link
 * LoggingBeanUtils#registerLoggingClientBeans(BeanDefinitionRegistry)}
 * register {@link LoggingWebInterceptor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LoggingClientBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    LoggingBeanUtils.registerLoggingClientBeans(registry);
  }
}
