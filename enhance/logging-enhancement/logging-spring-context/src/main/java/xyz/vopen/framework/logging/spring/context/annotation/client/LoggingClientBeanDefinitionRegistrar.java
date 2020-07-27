package xyz.vopen.framework.logging.spring.context.annotation.client;

import xyz.vopen.framework.logging.client.interceptor.web.LoggingWebInterceptor;
import xyz.vopen.framework.logging.spring.util.LoggingBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Register logging client beans {@link
 * LoggingBeanUtils#registerLoggingClientBeans(BeanDefinitionRegistry)}
 * register {@link LoggingWebInterceptor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 */
public class LoggingClientBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
  /** logger instance */
  static Logger logger = LoggerFactory.getLogger(LoggingClientBeanDefinitionRegistrar.class);

  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    LoggingBeanUtils.registerLoggingClientBeans(registry);
    logger.info("Logging client beans register successfully.");
  }
}
