package xyz.vopen.mixmicro.components.enhance.circuitbreaker.v2.spring;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xyz.vopen.mixmicro.components.circuitbreaker.v2.MixmicroCircuitBreakable;
import xyz.vopen.mixmicro.components.enhance.circuitbreaker.v2.autoconfigure.MixmicroCircuitBreakerProperties;
import xyz.vopen.mixmicro.components.enhance.circuitbreaker.v2.proxy.MixmicroResilience4jCircuitBreakerActionMethodInterceptor;
import xyz.vopen.mixmicro.kits.lang.NonNull;

/**
 * {@link MixmicroCircuitBreakerBeanPostProcessor}
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version} - 2020/6/22
 */
public class MixmicroCircuitBreakerBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

  private final MixmicroCircuitBreakerProperties properties;

  private ApplicationContext applicationContext;

  /**
   * Default Constructor For Bean Post Processor
   *
   * @param properties instance of {@link MixmicroCircuitBreakerProperties}
   */
  public MixmicroCircuitBreakerBeanPostProcessor(
      @NonNull MixmicroCircuitBreakerProperties properties) {
    this.properties = properties;
  }

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName)
      throws BeansException {

    if (bean instanceof MixmicroCircuitBreakable) {
      ProxyFactory factory = new ProxyFactory(bean);
      factory.setProxyTargetClass(true);
      factory.addInterface(MixmicroCircuitBreakable.class);
      factory.setExposeProxy(true);

      switch (properties.getType()) {
        case STL:
          throw new RuntimeException("Un-supported alibaba sentinel plugin.");

        case R4J:
        default:
          ObjectProvider<CircuitBreakerRegistry> provider = applicationContext.getBeanProvider(CircuitBreakerRegistry.class);
          factory.addAdvice(new MixmicroResilience4jCircuitBreakerActionMethodInterceptor((MixmicroCircuitBreakable) bean, provider));
          break;
      }

      return factory.getProxy();
    }

    return bean;
  }
}
