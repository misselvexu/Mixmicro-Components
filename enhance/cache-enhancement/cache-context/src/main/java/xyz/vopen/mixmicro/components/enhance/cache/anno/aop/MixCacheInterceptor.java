package xyz.vopen.mixmicro.components.enhance.cache.anno.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import xyz.vopen.mixmicro.components.enhance.cache.anno.method.CacheHandler;
import xyz.vopen.mixmicro.components.enhance.cache.anno.method.CacheInvokeConfig;
import xyz.vopen.mixmicro.components.enhance.cache.anno.method.CacheInvokeContext;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.ConfigMap;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.ConfigProvider;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.GlobalCacheConfig;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class MixCacheInterceptor implements MethodInterceptor, ApplicationContextAware {

  // private static final Logger logger = LoggerFactory.getLogger(MixCacheInterceptor.class);

  ConfigProvider configProvider;
  @Autowired private ConfigMap cacheConfigMap;
  private ApplicationContext applicationContext;
  private GlobalCacheConfig globalCacheConfig;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public Object invoke(final MethodInvocation invocation) throws Throwable {
    if (configProvider == null) {
      configProvider = applicationContext.getBean(ConfigProvider.class);
    }
    if (configProvider != null && globalCacheConfig == null) {
      globalCacheConfig = configProvider.getGlobalCacheConfig();
    }
    if (globalCacheConfig == null || !globalCacheConfig.isEnableMethodCache()) {
      return invocation.proceed();
    }

    Method method = invocation.getMethod();
    Object obj = invocation.getThis();
    CacheInvokeConfig cac = null;
    if (obj != null) {
      String key = CachePointcut.getKey(method, obj.getClass());
      cac = cacheConfigMap.getByMethodInfo(key);
    }

    /*
    if(logger.isTraceEnabled()){
        logger.trace("MixCacheInterceptor invoke. foundMixCacheConfig={}, method={}.{}(), targetClass={}",
                cac != null,
                method.getDeclaringClass().getName(),
                method.getName(),
                invocation.getThis() == null ? null : invocation.getThis().getClass().getName());
    }
    */

    if (cac == null || cac == CacheInvokeConfig.getNoCacheInvokeConfigInstance()) {
      return invocation.proceed();
    }

    CacheInvokeContext context =
        configProvider.getCacheContext().createCacheInvokeContext(cacheConfigMap);
    context.setTargetObject(invocation.getThis());
    context.setInvoker(invocation::proceed);
    context.setMethod(method);
    context.setArgs(invocation.getArguments());
    context.setCacheInvokeConfig(cac);
    context.setHiddenPackages(globalCacheConfig.getHiddenPackages());
    return CacheHandler.invoke(context);
  }

  public void setCacheConfigMap(ConfigMap cacheConfigMap) {
    this.cacheConfigMap = cacheConfigMap;
  }
}
