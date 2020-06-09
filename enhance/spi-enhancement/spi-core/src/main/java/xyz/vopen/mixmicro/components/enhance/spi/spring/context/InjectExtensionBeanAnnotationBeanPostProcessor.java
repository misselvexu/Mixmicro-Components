package xyz.vopen.mixmicro.components.enhance.spi.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.core.annotation.AnnotationAttributes;
import xyz.vopen.mixmicro.kits.Assert;
import xyz.vopen.mixmicro.kits.StringUtils;
import xyz.vopen.mixmicro.kits.spring.beans.factory.annotation.AbstractAnnotationBeanPostProcessor;
import xyz.vopen.mixmicro.components.enhance.spi.MixmicroExtensionLoader;
import xyz.vopen.mixmicro.components.enhance.spi.MixmicroExtensionLoaderFactory;
import xyz.vopen.mixmicro.components.enhance.spi.annotation.InjectExtensionBean;

/**
 * {@link InjectExtensionBeanAnnotationBeanPostProcessor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
public class InjectExtensionBeanAnnotationBeanPostProcessor extends AbstractAnnotationBeanPostProcessor {

  static final String INJECT_EXTENSION_ANNOTATION_BEAN_POST_PROCESSOR_BEAN_NAME = "injectExtensionBeanAnnotationBeanPostProcessor";

  private static final Logger log = LoggerFactory.getLogger(InjectExtensionBeanAnnotationBeanPostProcessor.class);

  public InjectExtensionBeanAnnotationBeanPostProcessor() {
    super(InjectExtensionBean.class);
  }

  /**
   * Subclass must implement this method to get injected-object. The context objects could help this
   * method if necessary :
   *
   * <ul>
   *   <li>{@link #getBeanFactory() BeanFactory}
   *   <li>{@link #getClassLoader() ClassLoader}
   *   <li>{@link #getEnvironment() Environment}
   * </ul>
   *
   * @param attributes      {@link AnnotationAttributes the annotation attributes}
   * @param bean            Current bean that will be injected
   * @param beanName        Current bean name that will be injected
   * @param injectedType    the type of injected-object
   * @param injectedElement {@link InjectionMetadata.InjectedElement}
   * @return The injected object
   * @throws Exception If resolving an injected object is failed.
   */
  @Override
  protected Object doGetInjectedBean(AnnotationAttributes attributes, Object bean, String beanName, Class<?> injectedType, InjectionMetadata.InjectedElement injectedElement) throws Exception {
    // GET EXTENSION INSTANCE FROM SPI FACTORY

    MixmicroExtensionLoader<?> loader =  MixmicroExtensionLoaderFactory.getExtensionLoader(injectedType);

    String alias =
        StringUtils.isNotBlank(attributes.getString("value"))
            ? attributes.getString("value")
            : StringUtils.isNotBlank(attributes.getString("extension")) ? attributes.getString("extension") : "";

    Assert.isTrue(StringUtils.isNotBlank(alias),"[@InjectExtensionBean] must provide one attribute of the two 'value','extension'");

    return loader.getExtension(alias);
  }

  /**
   * Build a cache key for injected-object. The context objects could help this method if necessary
   * :
   *
   * <ul>
   *   <li>{@link #getBeanFactory() BeanFactory}
   *   <li>{@link #getClassLoader() ClassLoader}
   *   <li>{@link #getEnvironment() Environment}
   * </ul>
   *
   * @param attributes      {@link AnnotationAttributes the annotation attributes}
   * @param bean            Current bean that will be injected
   * @param beanName        Current bean name that will be injected
   * @param injectedType    the type of injected-object
   * @param injectedElement {@link InjectionMetadata.InjectedElement}
   * @return Bean cache key
   */
  @Override
  protected String buildInjectedObjectCacheKey(AnnotationAttributes attributes, Object bean, String beanName, Class<?> injectedType, InjectionMetadata.InjectedElement injectedElement) {

    return injectedType.getName();
  }

}
