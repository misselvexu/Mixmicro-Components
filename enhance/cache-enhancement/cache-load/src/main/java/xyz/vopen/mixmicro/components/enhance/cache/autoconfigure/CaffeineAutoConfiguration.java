package xyz.vopen.mixmicro.components.enhance.cache.autoconfigure;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import xyz.vopen.mixmicro.components.enhance.cache.CacheBuilder;
import xyz.vopen.mixmicro.components.enhance.cache.embedded.CaffeineCacheBuilder;

/**
 * Created on 2018/12/2.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
@Component
@Conditional(CaffeineAutoConfiguration.CaffeineCondition.class)
public class CaffeineAutoConfiguration extends EmbeddedCacheAutoInit {
  public CaffeineAutoConfiguration() {
    super("caffeine");
  }

  @Override
  protected CacheBuilder initCache(ConfigTree ct, String cacheAreaWithPrefix) {
    CaffeineCacheBuilder builder = CaffeineCacheBuilder.createCaffeineCacheBuilder();
    parseGeneralConfig(builder, ct);
    return builder;
  }

  public static class CaffeineCondition extends MixCacheCondition {
    public CaffeineCondition() {
      super("caffeine");
    }
  }
}
