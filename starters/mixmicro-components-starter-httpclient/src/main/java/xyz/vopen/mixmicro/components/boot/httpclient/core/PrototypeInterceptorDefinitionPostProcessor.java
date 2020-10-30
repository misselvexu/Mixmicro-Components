package xyz.vopen.mixmicro.components.boot.httpclient.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.lang.NonNull;
import xyz.vopen.mixmicro.components.boot.httpclient.PrototypeInterceptor;

public class PrototypeInterceptorDefinitionPostProcessor
    implements BeanDefinitionRegistryPostProcessor {

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {
    String[] beanDefinitionNames = registry.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
      BeanDefinition beanDefinition = registry.getBeanDefinition(beanDefinitionName);
      String beanClassName = beanDefinition.getBeanClassName();
      if (beanClassName == null) {
        continue;
      }
      try {
        Class<?> beanClass = Class.forName(beanClassName);
        if (PrototypeInterceptor.class.isAssignableFrom(beanClass)) {
          beanDefinition.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
        }
      } catch (ClassNotFoundException ignore) {
      }
    }
  }

  @Override
  public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    // EMPTY
  }
}
