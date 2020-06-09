package xyz.vopen.mixmicro.components.enhance.spi.spring.context.annotation;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import xyz.vopen.mixmicro.components.enhance.spi.MixmicroExtensible;
import xyz.vopen.mixmicro.components.enhance.spi.MixmicroExtensionLoader;
import xyz.vopen.mixmicro.components.enhance.spi.MixmicroExtensionLoaderFactory;
import xyz.vopen.mixmicro.components.enhance.spi.exception.MixmicroSpiException;

import java.lang.reflect.Field;

/**
 * {@link InjectExtensionBeanFieldElement}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
public class InjectExtensionBeanFieldElement {

  private final Field field;

  private final boolean required;

  private final String alias;

  private final boolean isIntegrateExtensibleType;

  public InjectExtensionBeanFieldElement(boolean isIntegrateExtensibleType, Field field, String alias, boolean required) {
    this.field = field;
    this.required = required;
    this.alias = alias;
    this.isIntegrateExtensibleType = isIntegrateExtensibleType;
  }

  public void inject(Object target) {

    try {
      Class<?> injectedType = field.getType();

      if(!isIntegrateExtensibleType) {
        // parse interface class
        Class<?>[] classes = ClassUtils.getAllInterfacesForClass(injectedType);
        for (Class<?> aClass : classes) {
          if(aClass.isAnnotationPresent(MixmicroExtensible.class)) {
            injectedType = aClass;
            break;
          }
        }
      }

      MixmicroExtensionLoader<?> loader =  MixmicroExtensionLoaderFactory.getExtensionLoader(injectedType);

      Object injectedObject = loader.getExtension(alias);

      ReflectionUtils.makeAccessible(field);

      field.set(target, injectedObject);

    } catch (Exception e) {

      e.printStackTrace();

      if (e instanceof NoSuchBeanDefinitionException) {
        if (!required) {
          return;
        }
      }
      throw new MixmicroSpiException(e);
    }
  }
}
