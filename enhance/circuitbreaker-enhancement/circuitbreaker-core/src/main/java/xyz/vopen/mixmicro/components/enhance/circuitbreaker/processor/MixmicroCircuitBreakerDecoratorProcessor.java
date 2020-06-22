package xyz.vopen.mixmicro.components.enhance.circuitbreaker.processor;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import xyz.vopen.mixmicro.components.circuitbreaker.MixmicroCircuitBreaker;

/**
 * {@link MixmicroCircuitBreakerDecoratorProcessor}
 *
 * @author: siran.yao
 * @date: 2020/6/20 15:40
 */
@Component
@ConditionalOnClass(CircuitBreakerRegistry.class)
public class MixmicroCircuitBreakerDecoratorProcessor implements BeanPostProcessor {
    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof MixmicroCircuitBreaker 
                && !ScopedProxyUtils.isScopedTarget(beanName)){

            MixmicroCircuitBreaker mixmicroCircuitBreaker = (MixmicroCircuitBreaker)bean;

            ProxyFactory factory = new ProxyFactory(bean);
            factory.setProxyTargetClass(true);
            factory.addInterface(MixmicroCircuitBreaker.class);
            factory.addAdvice(
                    new MixmicroCircuitBreakerDecoratorIntercept(circuitBreakerRegistry));
            return factory.getProxy();
        }
        return bean;
    }

}
