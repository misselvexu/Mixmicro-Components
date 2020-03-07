package xyz.vopen.mixmicro.components.enhance.cache.annotation.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xyz.vopen.mixmicro.components.enhance.cache.annotation.SerialPolicy;
import xyz.vopen.mixmicro.components.enhance.cache.support.JavaValueDecoder;
import xyz.vopen.mixmicro.components.enhance.cache.support.SpringJavaValueDecoder;

import java.util.function.Function;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class DefaultSpringEncoderParser extends DefaultEncoderParser
    implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  static String parseBeanName(String str) {
    final String beanPrefix = "bean:";
    int len = beanPrefix.length();
    if (str != null && str.startsWith(beanPrefix) && str.length() > len) {
      return str.substring(len);
    } else {
      return null;
    }
  }

  @Override
  public Function<Object, byte[]> parseEncoder(String valueEncoder) {
    String beanName = parseBeanName(valueEncoder);
    if (beanName == null) {
      return super.parseEncoder(valueEncoder);
    } else {
      Object bean = applicationContext.getBean(beanName);
      if (bean instanceof Function) {
        return (Function<Object, byte[]>) bean;
      } else {
        return ((SerialPolicy) bean).encoder();
      }
    }
  }

  @Override
  public Function<byte[], Object> parseDecoder(String valueDecoder) {
    String beanName = parseBeanName(valueDecoder);
    if (beanName == null) {
      return super.parseDecoder(valueDecoder);
    } else {
      Object bean = applicationContext.getBean(beanName);
      if (bean instanceof Function) {
        return (Function<byte[], Object>) bean;
      } else {
        return ((SerialPolicy) bean).decoder();
      }
    }
  }

  @Override
  JavaValueDecoder javaValueDecoder(boolean useIdentityNumber) {
    return new SpringJavaValueDecoder(useIdentityNumber);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
