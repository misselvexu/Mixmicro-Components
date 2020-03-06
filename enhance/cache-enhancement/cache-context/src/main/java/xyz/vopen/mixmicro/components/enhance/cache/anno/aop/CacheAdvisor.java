package xyz.vopen.mixmicro.components.enhance.cache.anno.aop;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.ConfigMap;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheAdvisor extends AbstractBeanFactoryPointcutAdvisor {

  public static final String CACHE_ADVISOR_BEAN_NAME = "mixcache2.internalCacheAdvisor";

  @Autowired private ConfigMap cacheConfigMap;

  private String[] basePackages;

  @Override
  public Pointcut getPointcut() {
    CachePointcut pointcut = new CachePointcut(basePackages);
    pointcut.setCacheConfigMap(cacheConfigMap);
    return pointcut;
  }

  public void setCacheConfigMap(ConfigMap cacheConfigMap) {
    this.cacheConfigMap = cacheConfigMap;
  }

  public void setBasePackages(String[] basePackages) {
    this.basePackages = basePackages;
  }
}
