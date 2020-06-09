package xyz.vopen.mixmicro.components.enhance.spi.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import xyz.vopen.mixmicro.components.enhance.spi.spring.context.annotation.InjectSpringBeanFieldElement;
import xyz.vopen.mixmicro.components.enhance.spi.MixmicroExtensionLoader;
import xyz.vopen.mixmicro.components.enhance.spi.MixmicroExtensionLoaderListener;
import xyz.vopen.mixmicro.components.enhance.spi.annotation.InjectSpringBean;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.util.ReflectionUtils.doWithLocalFields;

/**
 * {@link SpringMixmicroExtensionLoader}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SpringMixmicroExtensionLoader<T> extends MixmicroExtensionLoader<T> {

  private static final Logger log = LoggerFactory.getLogger(SpringMixmicroExtensionLoader.class);

  private static ConfigurableApplicationContext applicationContext;

  private static volatile boolean applicationStarted = false;

  public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
    // REGISTER CONTEXT
    SpringMixmicroExtensionLoader.applicationContext = applicationContext;
    // REGISTER PROCESSOR
    SpringMixmicroExtensionLoader.applicationContext
        .getBeanFactory()
        .registerSingleton(
            InjectExtensionBeanAnnotationBeanPostProcessor.INJECT_EXTENSION_ANNOTATION_BEAN_POST_PROCESSOR_BEAN_NAME,
            new InjectExtensionBeanAnnotationBeanPostProcessor());
  }

  private static void started() {
    SpringMixmicroExtensionLoader.applicationStarted = true;
  }

  private static final ConcurrentMap<String, Object> INSTANCE_MAP = new ConcurrentHashMap<>();

  private static final ConcurrentMap<Object, Boolean> INSTANCE_STATUS_MAP = new ConcurrentHashMap<>();

  /**
   * Default Construct
   *
   * @param interfaceClass interface class
   * @param listener listener for loader
   */
  public SpringMixmicroExtensionLoader(
      Class<T> interfaceClass, MixmicroExtensionLoaderListener<T> listener) {
    super(interfaceClass, listener);
  }

  public static void finishedExtensionInitialize() {
    started();
  }

  @Override
  public T getExtension(String alias, Class[] argTypes, Object[] args) {

    if (INSTANCE_MAP.containsKey(alias)) {
      return (T) INSTANCE_MAP.get(alias);
    }

    T t = super.getExtension(alias, argTypes, args);

    if(applicationStarted) {

      if (!INSTANCE_STATUS_MAP.containsKey(t)) {

        final List<InjectSpringBeanFieldElement> currElements = new ArrayList<>();

        doWithLocalFields(
            t.getClass(),
            field -> {
              if (field.isAnnotationPresent(InjectSpringBean.class)) {
                InjectSpringBean annotation = field.getAnnotation(InjectSpringBean.class);
                if (Modifier.isStatic(field.getModifiers())) {
                  if (log.isInfoEnabled()) {
                    log.warn("@InjectSpringBean annotation is not supported on static fields: " + field);
                  }
                  return;
                }

                boolean required = annotation.required();
                currElements.add(new InjectSpringBeanFieldElement(field, required));
              }
            });

        // inject
        doInject(t, currElements);

        INSTANCE_STATUS_MAP.put(t,Boolean.TRUE);

        INSTANCE_MAP.put(alias, t);
      }
    }

    return t;
  }

  @Override
  public T getExtension(String alias) {
    return getExtension(alias, null, null);
  }

  private static void doInject(Object t, List<InjectSpringBeanFieldElement> elements) {
    for (InjectSpringBeanFieldElement element : elements) {
      element.inject(t, applicationContext);
    }
  }
}
