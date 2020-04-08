package xyz.vopen.mixmicro.components.boot.web.core.advice;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lang.NonNull;
import xyz.vopen.mixmicro.components.boot.web.MixmicroWebConfigProperties;
import xyz.vopen.mixmicro.kits.Assert;

/**
 * {@link AbstractAdvice}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/4/8
 */
public abstract class AbstractAdvice implements BeanFactoryAware {

  protected BeanFactory beanFactory;

  protected MixmicroWebConfigProperties configProperties;

  protected MixmicroWebConfigProperties getProperties() {
    if (configProperties == null) {
      try {
        this.configProperties = this.beanFactory.getBean(MixmicroWebConfigProperties.class);
      } catch (Exception ignore) {
      }
    }
    Assert.notNull(
        this.configProperties,
        "Context evn properties @MixmicroWebConfigProperties must be initialized with spring bean factory.");
    return this.configProperties;
  }

  /**
   * Callback that supplies the owning factory to a bean instance.
   *
   * <p>Invoked after the population of normal bean properties but before an initialization callback
   * such as {@link InitializingBean#afterPropertiesSet()} or a custom init-method.
   *
   * @param beanFactory owning BeanFactory (never {@code null}). The bean can immediately call
   *     methods on the factory.
   * @throws BeansException in case of initialization errors
   * @see BeanInitializationException
   */
  @Override
  public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }
}
