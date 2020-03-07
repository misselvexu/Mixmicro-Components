package xyz.vopen.mixmicro.components.enhance.cache.annotation.method;

import org.springframework.context.ApplicationContext;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class SpringCacheInvokeContext extends CacheInvokeContext {
  protected ApplicationContext context;

  public SpringCacheInvokeContext(ApplicationContext context) {
    this.context = context;
  }

  public Object bean(String name) {
    return context.getBean(name);
  }
}
