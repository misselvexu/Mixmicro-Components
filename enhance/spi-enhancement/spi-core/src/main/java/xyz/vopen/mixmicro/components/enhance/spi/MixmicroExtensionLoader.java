package xyz.vopen.mixmicro.components.enhance.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.vopen.mixmicro.components.enhance.spi.spring.context.annotation.InjectExtensionBeanFieldElement;
import xyz.vopen.mixmicro.components.enhance.spi.util.MixmicroExceptions;
import xyz.vopen.mixmicro.kits.Assert;
import xyz.vopen.mixmicro.kits.StringUtils;
import xyz.vopen.mixmicro.components.enhance.spi.annotation.InjectExtensionBean;
import xyz.vopen.mixmicro.components.enhance.spi.exception.MixmicroSpiException;
import xyz.vopen.mixmicro.components.enhance.spi.util.MixmicroClassKit;
import xyz.vopen.mixmicro.components.enhance.spi.util.MixmicroClassLoaders;
import xyz.vopen.mixmicro.components.enhance.spi.util.MixmicroClassTypes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.springframework.util.ReflectionUtils.doWithLocalFields;

/**
 * Extension Loader
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2018-12-03.
 */
@SuppressWarnings("rawtypes")
public class MixmicroExtensionLoader<T> {

  /** 扩展点加载的路径 */
  public static final String EXTENSION_LOAD_PATH = "META-INF/mixmicro/services/";

  private static final Logger log = LoggerFactory.getLogger(MixmicroExtensionLoader.class);
  /** 当前加载的接口类名 */
  protected final Class<T> interfaceClass;

  /** 接口名字 */
  protected final String interfaceName;
  /** 加载监听器 */
  protected final MixmicroExtensionLoaderListener<T> listener;
  /** 扩展点是否单例 */
  protected final MixmicroExtensible mixmicroExtensible;

  /** 全部的加载的实现类 {"alias":ExtensionClass} */
  protected final ConcurrentMap<String, MixmicroExtensionClass<T>> all;

  /** 如果是单例，那么factory不为空 */
  protected final ConcurrentMap<String, T> factory;

  /**
   * 加载的示例填充状态记录
   */
  protected final ConcurrentMap<T, Boolean> status = new ConcurrentHashMap<>();

  /**
   * Default Construct
   *
   * @param interfaceClass interface class
   * @param listener listener for loader
   */
  public MixmicroExtensionLoader(Class<T> interfaceClass, MixmicroExtensionLoaderListener<T> listener) {

    // 接口为空，既不是接口，也不是抽象类
    if (interfaceClass == null
        || !(interfaceClass.isInterface() || Modifier.isAbstract(interfaceClass.getModifiers()))) {
      throw new IllegalArgumentException("Extensible class must be interface or abstract class!");
    }

    this.interfaceClass = interfaceClass;
    this.interfaceName = MixmicroClassTypes.getTypeStr(interfaceClass);
    this.listener = listener;

    MixmicroExtensible mixmicroExtensible = interfaceClass.getAnnotation(MixmicroExtensible.class);
    if (mixmicroExtensible == null) {
      throw new IllegalArgumentException("Error when load extensible interface " + interfaceName + ", must add annotation @IntegrateExtensible.");
    } else {
      this.mixmicroExtensible = mixmicroExtensible;
    }

    this.factory = mixmicroExtensible.singleton() ? new ConcurrentHashMap<>() : null;
    this.all = new ConcurrentHashMap<>();
    loadFromFile(EXTENSION_LOAD_PATH);
  }

  /**
   * 得到当前ClassLoader，先找线程池的，找不到就找中间件所在的ClassLoader
   *
   * @return ClassLoader
   */
  public static ClassLoader getCurrentClassLoader() {
    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    if (cl == null) {
      cl = MixmicroExtensionLoader.class.getClassLoader();
    }
    return cl == null ? ClassLoader.getSystemClassLoader() : cl;
  }

  /** @param path path必须以/结尾 */
  protected synchronized void loadFromFile(String path) {
    if (log.isDebugEnabled()) {
      log.debug("Loading extension of extensible {} from path: {}", interfaceName, path);
    }
    // 默认如果不指定文件名字，就是接口名
    mixmicroExtensible.file();
    String file = mixmicroExtensible.file().trim().length() == 0 ? interfaceName : mixmicroExtensible.file().trim();
    String fullFileName = path + file;

    try {
      ClassLoader classLoader = MixmicroClassLoaders.getClassLoader(getClass());

      loadFromClassLoader(classLoader, fullFileName);
    } catch (Throwable t) {
      if (log.isErrorEnabled()) {
        log.error("Failed to load extension of extensible " + interfaceName + " from path:" + fullFileName, t);
      }
    }
  }

  protected void loadFromClassLoader(ClassLoader classLoader, String fullFileName)
      throws Throwable {
    Enumeration<URL> urls =
        classLoader != null
            ? classLoader.getResources(fullFileName)
            : ClassLoader.getSystemResources(fullFileName);
    // 可能存在多个文件。
    if (urls != null) {
      while (urls.hasMoreElements()) {
        // 读取一个文件
        URL url = urls.nextElement();
        if (log.isDebugEnabled()) {
          log.debug("Loading extension of extensible {} from classloader: {} and file: {}", interfaceName, classLoader, url);
        }
        BufferedReader reader = null;
        try {
          reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
          String line;
          while ((line = reader.readLine()) != null) {
            readLine(url, line);
          }
        } catch (Throwable t) {
          if (log.isWarnEnabled()) {
            log.warn("Failed to load extension of extensible " + interfaceName + " from classloader: " + classLoader + " and file:" + url, t);
          }
        } finally {
          if (reader != null) {
            reader.close();
          }
        }
      }
    }
  }

  private String[] parseAliasAndClassName(String line) {
    if (line == null || line.trim().length() == 0) {
      return null;
    }
    line = line.trim();
    int i0 = line.indexOf('#');
    if (i0 == 0 || line.length() == 0) {
      return null; // 当前行是注释 或者 空
    }
    if (i0 > 0) {
      line = line.substring(0, i0).trim();
    }

    String alias = null;
    String className;
    int i = line.indexOf('=');
    if (i > 0) {
      alias = line.substring(0, i).trim(); // 以代码里的为准
      className = line.substring(i + 1).trim();
    } else {
      className = line;
    }
    if (className.length() == 0) {
      return null;
    }
    return new String[] {alias, className};
  }

  protected void readLine(URL url, String line) {
    String[] aliasAndClassName = parseAliasAndClassName(line);
    if (aliasAndClassName == null || aliasAndClassName.length != 2) {
      return;
    }
    String alias = aliasAndClassName[0];
    String className = aliasAndClassName[1];
    // 读取配置的实现类
    Class tmp;
    try {
      tmp = MixmicroClassKit.forName(className, false);
    } catch (Throwable e) {
      if (log.isWarnEnabled()) {
        log.warn("Extension {} of extensible {} is disabled, cause by: {}", className, interfaceName, MixmicroExceptions.toShortString(e, 2));
      }
      if (log.isDebugEnabled()) {
        log.debug("Extension " + className + " of extensible " + interfaceName + " is disabled.", e);
      }
      return;
    }
    if (!interfaceClass.isAssignableFrom(tmp)) {
      throw new IllegalArgumentException("Error when load extension of extensible " + interfaceName + " from file:" + url + ", " + className + " is not subtype of interface.");
    }
    Class<? extends T> implClass = (Class<? extends T>) tmp;

    // 检查是否有可扩展标识
    MixmicroExtension mixmicroExtension = implClass.getAnnotation(MixmicroExtension.class);
    if (mixmicroExtension == null) {
      throw new IllegalArgumentException("Error when load extension of extensible " + interfaceName + " from file:" + url + ", " + className + " must add annotation @IntegrateExtension.");
    } else {
      String aliasInCode = mixmicroExtension.value();
      if (aliasInCode == null || aliasInCode.trim().length() == 0) {
        // 扩展实现类未配置@IntegrateExtension 标签
        throw new IllegalArgumentException("Error when load extension of extensible " + interfaceClass + " from file:" + url + ", " + className + "'s alias of @IntegrateExtension is blank");
      }
      if (alias == null) {
        // spi文件里没配置，用代码里的
        alias = aliasInCode;
      } else {
        // spi文件里配置的和代码里的不一致
        if (!aliasInCode.equals(alias)) {
          throw new IllegalArgumentException("Error when load extension of extensible " + interfaceName + " from file:" + url + ", aliases of " + className + " are " + "not equal between " + aliasInCode + "(code) and " + alias + "(file).");
        }
      }
    }
    // 不可以是default和*
    if ("default".equals(alias) || "*".equals(alias)) {
      throw new IllegalArgumentException("Error when load extension of extensible " + interfaceName + " from file:" + url + ", alias of @IntegrateExtension must not \"default\" and \"*\" at " + className);
    }
    // 检查是否有存在同名的
    MixmicroExtensionClass<T> mixmicroExtensionClass = buildClass(mixmicroExtension, implClass, alias);

    loadSuccess(alias, mixmicroExtensionClass);
  }

  private MixmicroExtensionClass<T> buildClass(MixmicroExtension mixmicroExtension, Class<? extends T> implClass, String alias) {
    MixmicroExtensionClass<T> mixmicroExtensionClass = new MixmicroExtensionClass<>(implClass, alias);
    mixmicroExtensionClass.setSingleton(mixmicroExtensible.singleton());
    return mixmicroExtensionClass;
  }

  private void loadSuccess(String alias, MixmicroExtensionClass<T> mixmicroExtensionClass) {
    if (listener != null) {
      try {
        listener.onLoad(mixmicroExtensionClass); // 加载完毕，通知监听器
        all.put(alias, mixmicroExtensionClass);
      } catch (Exception e) {
        log.error("Error when load extension of extensible " + interfaceClass + " with alias: " + alias, e);
      }
    } else {
      all.put(alias, mixmicroExtensionClass);
    }
  }

  /**
   * 返回全部扩展类
   *
   * @return 扩展类对象
   */
  public ConcurrentMap<String, MixmicroExtensionClass<T>> getAllExtensions() {
    return all;
  }

  /**
   * 根据服务别名查找扩展类
   *
   * @param alias 扩展别名
   * @return 扩展类对象
   */
  public MixmicroExtensionClass<T> getExtensionClass(String alias) {
    return all == null ? null : all.get(alias);
  }

  /**
   * 得到实例
   *
   * @param alias 别名
   * @return 扩展实例（已判断是否单例）
   */
  public T getExtension(String alias) {

    MixmicroExtensionClass<T> mixmicroExtensionClass = getExtensionClass(alias);

    if (mixmicroExtensionClass == null) {
      throw new MixmicroSpiException("Not found extension of " + interfaceName + " named: \"" + alias + "\"!");
    } else {
      if (mixmicroExtensible.singleton() && factory != null) {
        T t = factory.get(alias);
        if (t == null) {
          synchronized (this) {
            t = factory.get(alias);
            if (t == null) {
              t = mixmicroExtensionClass.getExtInstance();
              factory.put(alias, t);
            }
          }
        }

        // INJECT
        inject(t);

        return t;
      } else {

        T t = mixmicroExtensionClass.getExtInstance();
        // INJECT
        inject(t);

        return t;
      }
    }
  }

  private void inject(T t) {

    if(status.containsKey(t)) {
      return;
    }

    try{

      final List<InjectExtensionBeanFieldElement> currElements = new ArrayList<>();

      doWithLocalFields(
          t.getClass(),
          field -> {
            if (field.isAnnotationPresent(InjectExtensionBean.class)) {
              InjectExtensionBean annotation = field.getAnnotation(InjectExtensionBean.class);
              if (Modifier.isStatic(field.getModifiers())) {
                if (log.isInfoEnabled()) {
                  log.warn("@InjectExtensionBean annotation is not supported on static fields: " + field);
                }
                return;
              }

              boolean required = annotation.required();
              String alias =
                  StringUtils.isNotBlank(annotation.value())
                      ? annotation.value()
                      : StringUtils.isNotBlank(annotation.extension()) ? annotation.extension() : "";

              Class<?> spiClazz = field.getType();

              boolean isIntegrateExtensibleType = false;

              if(spiClazz.isAnnotationPresent(MixmicroExtensible.class)) {

                // NON default ..
                isIntegrateExtensibleType = true;

              } else if(spiClazz.isAnnotationPresent(MixmicroExtension.class)) {

                MixmicroExtension extension = spiClazz.getAnnotation(MixmicroExtension.class);

                alias = extension.value();

              } else {
                log.warn("[@InjectExtensionBean] not found @IntegrateExtensible or @IntegrateExtension on target class: {}", spiClazz);
                return;
              }

              Assert.isTrue(StringUtils.isNotBlank(alias),"[@InjectExtensionBean] must provide one attribute of the two 'value','extension'");

              currElements.add(new InjectExtensionBeanFieldElement(isIntegrateExtensibleType, field, alias, required));
            }
          });

      for (InjectExtensionBeanFieldElement currElement : currElements) {
        currElement.inject(t);
      }

      status.put(t,Boolean.TRUE);

    } catch (Exception e) {
      throw new MixmicroSpiException("@InjectExtensionBean inject happened exception ", e);
    }
  }

  /**
   * 得到实例
   *
   * @param alias 别名
   * @param argTypes 扩展初始化需要的参数类型
   * @param args 扩展初始化需要的参数
   * @return 扩展实例（已判断是否单例）
   */
  public T getExtension(String alias, Class[] argTypes, Object[] args) {
    MixmicroExtensionClass<T> mixmicroExtensionClass = getExtensionClass(alias);
    if (mixmicroExtensionClass == null) {
      throw new MixmicroSpiException("Not found extension of " + interfaceName + " named: \"" + alias + "\"!");
    } else {
      if (mixmicroExtensible.singleton() && factory != null) {
        T t = factory.get(alias);
        if (t == null) {
          synchronized (this) {
            t = factory.get(alias);
            if (t == null) {
              t = mixmicroExtensionClass.getExtInstance(argTypes, args);
              factory.put(alias, t);
            }
          }
        }

        // INJECT
        inject(t);

        return t;
      } else {

        T t = mixmicroExtensionClass.getExtInstance(argTypes, args);

        // INJECT
        inject(t);

        return t;
      }
    }
  }
}
