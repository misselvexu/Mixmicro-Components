package xyz.vopen.mixmicro.components.enhance.cache.anno.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class DefaultSpringKeyConvertorParser extends DefaultKeyConvertorParser
    implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public Function<Object, Object> parseKeyConvertor(String convertor) {
    String beanName = DefaultSpringEncoderParser.parseBeanName(convertor);
    if (beanName == null) {
      return super.parseKeyConvertor(convertor);
    } else {
      return (Function<Object, Object>) applicationContext.getBean(beanName);
    }
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
