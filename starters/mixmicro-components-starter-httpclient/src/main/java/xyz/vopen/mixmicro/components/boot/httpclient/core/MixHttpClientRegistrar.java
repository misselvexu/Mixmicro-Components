package xyz.vopen.mixmicro.components.boot.httpclient.core;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import xyz.vopen.mixmicro.components.boot.httpclient.MixHttpClientScan;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class MixHttpClientRegistrar
    implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, BeanClassLoaderAware {

  private ResourceLoader resourceLoader;

  private ClassLoader classLoader;

  @Override
  public void registerBeanDefinitions(
      AnnotationMetadata metadata, @NonNull BeanDefinitionRegistry registry) {
    AnnotationAttributes attributes =
        AnnotationAttributes.fromMap(
            metadata.getAnnotationAttributes(MixHttpClientScan.class.getName()));

    Assert.notNull(attributes, "Annotation @MixHttpClient 's attributes must not be null and empty .");

    // Scan the @MixHttpClient annotated interface under the specified path and register it to the BeanDefinitionRegistry
    ClassPathMixHttpClientScanner scanner = new ClassPathMixHttpClientScanner(registry, classLoader);

    if (resourceLoader != null) {
      scanner.setResourceLoader(resourceLoader);
    }
    // Specify the base package for scanning
    String[] basePackages = getPackagesToScan(attributes);
    scanner.registerFilters();
    // Scan and register to BeanDefinition
    scanner.doScan(basePackages);
  }

  /**
   * 获取扫描的基础包路径
   *
   * @return 基础包路径
   */
  private String[] getPackagesToScan(@NonNull AnnotationAttributes attributes) {
    String[] value = attributes.getStringArray("value");
    String[] basePackages = attributes.getStringArray("basePackages");
    Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
    if (!ObjectUtils.isEmpty(value)) {
      Assert.state(
          ObjectUtils.isEmpty(basePackages),
          "@MixHttpClient basePackages and value attributes are mutually exclusive");
    }
    Set<String> packagesToScan = new LinkedHashSet<>();
    packagesToScan.addAll(Arrays.asList(value));
    packagesToScan.addAll(Arrays.asList(basePackages));
    for (Class<?> basePackageClass : basePackageClasses) {
      packagesToScan.add(ClassUtils.getPackageName(basePackageClass));
    }
    return packagesToScan.toArray(new String[0]);
  }

  @Override
  public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Override
  public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
    this.classLoader = classLoader;
  }
}
