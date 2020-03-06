package xyz.vopen.mixmicro.components.enhance.cache.anno.support;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
class AbstractLifecycle {
  private boolean inited;

  @PostConstruct
  public final synchronized void init() {
    if (!inited) {
      doInit();
      inited = true;
    }
  }

  protected void doInit() {}

  @PreDestroy
  public final synchronized void shutdown() {
    if (inited) {
      doShutdown();
      inited = false;
    }
  }

  protected void doShutdown() {}
}
