package xyz.vopen.mixmicro.components.enhance.cache.anno.support;

import java.time.Duration;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class PenetrationProtectConfig {
  private boolean penetrationProtect;
  private Duration penetrationProtectTimeout;

  public boolean isPenetrationProtect() {
    return penetrationProtect;
  }

  public void setPenetrationProtect(boolean penetrationProtect) {
    this.penetrationProtect = penetrationProtect;
  }

  public Duration getPenetrationProtectTimeout() {
    return penetrationProtectTimeout;
  }

  public void setPenetrationProtectTimeout(Duration penetrationProtectTimeout) {
    this.penetrationProtectTimeout = penetrationProtectTimeout;
  }
}
