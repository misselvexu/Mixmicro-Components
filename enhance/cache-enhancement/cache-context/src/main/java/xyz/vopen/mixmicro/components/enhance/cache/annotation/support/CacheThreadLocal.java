package xyz.vopen.mixmicro.components.enhance.cache.annotation.support;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
class CacheThreadLocal {

  private int enabledCount = 0;

  int getEnabledCount() {
    return enabledCount;
  }

  void setEnabledCount(int enabledCount) {
    this.enabledCount = enabledCount;
  }
}
