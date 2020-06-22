package xyz.vopen.mixmicro.components.enhance.circuitbreaker.processor;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xyz.vopen.mixmicro.components.circuitbreaker.MixmicroCircuitBreaker;
import xyz.vopen.mixmicro.kits.lang.NonNull;

/**
 * {@link MixmicroCircuitBreakerDecoratorProcessor}
 *
 * @author siran.yao
 * @date 2020/6/20 15:40
 */
public class MixmicroCircuitBreakerDecoratorProcessor implements BeanPostProcessor, ApplicationContextAware {

  private final CircuitBreakerRegistry circuitBreakerRegistry;

  private ApplicationContext applicationContext;

  public MixmicroCircuitBreakerDecoratorProcessor(CircuitBreakerRegistry circuitBreakerRegistry) {
    this.circuitBreakerRegistry = circuitBreakerRegistry;
  }

  @Override
  public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {

    if (bean instanceof MixmicroCircuitBreaker && !ScopedProxyUtils.isScopedTarget(beanName)) {

      ProxyFactory factory = new ProxyFactory(bean);
      factory.setProxyTargetClass(true);
      factory.addInterface(MixmicroCircuitBreaker.class);
      factory.setExposeProxy(true);
      factory.addAdvice(new MixmicroCircuitBreakerDecoratorIntercept(circuitBreakerRegistry));

      Object proxy = factory.getProxy();

      ((MixmicroCircuitBreaker) bean).setSelf(proxy);

      return factory.getProxy();
    }
    return bean;
  }
}
