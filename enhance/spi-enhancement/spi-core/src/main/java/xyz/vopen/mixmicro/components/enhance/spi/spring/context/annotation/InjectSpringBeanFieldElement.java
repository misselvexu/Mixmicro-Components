package xyz.vopen.mixmicro.components.enhance.spi.spring.context.annotation;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.util.ReflectionUtils;
import xyz.vopen.mixmicro.components.enhance.spi.exception.MixmicroSpiException;

import java.lang.reflect.Field;

/**
 * {@link InjectSpringBeanFieldElement}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/2
 */
public class InjectSpringBeanFieldElement {

  private final Field field;

  private final boolean required;

  public InjectSpringBeanFieldElement(Field field, boolean required) {
    this.field = field;
    this.required = required;
  }

  public void inject(Object target, BeanFactory factory) {

    try {
      Class<?> injectedType = field.getType();

      Object injectedObject = factory.getBean(injectedType);

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
