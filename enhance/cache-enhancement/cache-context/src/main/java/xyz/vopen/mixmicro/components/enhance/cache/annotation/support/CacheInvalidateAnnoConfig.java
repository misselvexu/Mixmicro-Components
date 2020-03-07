package xyz.vopen.mixmicro.components.enhance.cache.annotation.support;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class CacheInvalidateAnnoConfig extends CacheAnnoConfig {
  private boolean multi;

  public boolean isMulti() {
    return multi;
  }

  public void setMulti(boolean multi) {
    this.multi = multi;
  }
}
