package xyz.vopen.mixmicro.components.boot.httpclient.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;
import xyz.vopen.mixmicro.components.boot.httpclient.annotation.MixHttpClient;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

public class ClassPathMixHttpClientScanner extends ClassPathBeanDefinitionScanner {

  private final ClassLoader classLoader;

  private static final Logger logger =
      LoggerFactory.getLogger(ClassPathMixHttpClientScanner.class);

  public ClassPathMixHttpClientScanner(BeanDefinitionRegistry registry, ClassLoader classLoader) {
    super(registry, false);
    this.classLoader = classLoader;
  }

  public void registerFilters() {
    AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(MixHttpClient.class);
    this.addIncludeFilter(annotationTypeFilter);
  }

  @Override
  protected Set<BeanDefinitionHolder> doScan(@NonNull String... basePackages) {
    Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
    if (beanDefinitions.isEmpty()) {
      logger.warn(
          "No MixHttpClient was found in '"
              + Arrays.toString(basePackages)
              + "' package. Please check your configuration.");
    } else {
      processBeanDefinitions(beanDefinitions);
    }
    return beanDefinitions;
  }

  @Override
  protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
    if (beanDefinition.getMetadata().isInterface()) {
      try {
        Class<?> target =
            ClassUtils.forName(beanDefinition.getMetadata().getClassName(), classLoader);
        return !target.isAnnotation();
      } catch (Exception ex) {
        logger.error("load class exception:", ex);
      }
    }
    return false;
  }

  private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
    GenericBeanDefinition definition;
    for (BeanDefinitionHolder holder : beanDefinitions) {
      definition = (GenericBeanDefinition) holder.getBeanDefinition();
      if (logger.isDebugEnabled()) {
        logger.debug(
            "Creating MixHttpClientBean with name '"
                + holder.getBeanName()
                + "' and '"
                + definition.getBeanClassName()
                + "' Interface");
      }
      definition
          .getConstructorArgumentValues()
          .addGenericArgumentValue(Objects.requireNonNull(definition.getBeanClassName()));
      // beanClass全部设置为RetrofitFactoryBean
      definition.setBeanClass(MixHttpClientFactoryBean.class);
    }
  }
}
