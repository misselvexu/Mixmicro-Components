package xyz.vopen.mixmicro.components.enhance.spi;

import xyz.vopen.mixmicro.components.enhance.spi.spring.context.SpringMixmicroExtensionLoader;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Nas exts factory
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class MixmicroExtensionLoaderFactory {

  /** All extension loader {Class : ExtensionLoader} */
  private static final ConcurrentMap<Class, MixmicroExtensionLoader> LOADER_MAP = new ConcurrentHashMap<>();

  private static AtomicBoolean contextJudgment = new AtomicBoolean(false);

  private static volatile boolean spring_context_enabled = false;

  private MixmicroExtensionLoaderFactory() {}

  private static <T> MixmicroExtensionLoader<T> getIntegrateExtensionLoader(Class<T> interfaceClass, MixmicroExtensionLoaderListener<T> listener) {

    if(contextJudgment.compareAndSet(false,true)) {
      try{
        Class.forName("org.springframework.context.ApplicationContext");
        spring_context_enabled = true;
      } catch (Exception ignored) {
      }
    }

    if(spring_context_enabled) {
      return new SpringMixmicroExtensionLoader<>(interfaceClass, listener);
    }

    return new MixmicroExtensionLoader<>(interfaceClass, listener);
  }

  /**
   * Get extension loader by extensible class with listener
   *
   * @param clazz Extensible class
   * @param listener Listener of ExtensionLoader
   * @param <T> Class
   * @return ExtensionLoader of this class
   */
  public static <T> MixmicroExtensionLoader<T> getExtensionLoader(Class<T> clazz, MixmicroExtensionLoaderListener<T> listener) {
    MixmicroExtensionLoader<T> loader = LOADER_MAP.get(clazz);
    if (loader == null) {
      synchronized (MixmicroExtensionLoaderFactory.class) {
        loader = LOADER_MAP.get(clazz);
        if (loader == null) {
          loader = getIntegrateExtensionLoader(clazz, listener);
          LOADER_MAP.put(clazz, loader);
        }
      }
    }
    return loader;
  }

  /**
   * Get extension loader by extensible class without listener
   *
   * @param clazz Extensible class
   * @param <T> Class
   * @return ExtensionLoader of this class
   */
  public static <T> MixmicroExtensionLoader<T> getExtensionLoader(Class<T> clazz) {
    return getExtensionLoader(clazz, null);
  }
}
