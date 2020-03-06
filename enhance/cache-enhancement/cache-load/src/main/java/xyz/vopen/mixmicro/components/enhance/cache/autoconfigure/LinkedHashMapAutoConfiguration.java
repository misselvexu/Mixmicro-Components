package xyz.vopen.mixmicro.components.enhance.cache.autoconfigure;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import xyz.vopen.mixmicro.components.enhance.cache.CacheBuilder;
import xyz.vopen.mixmicro.components.enhance.cache.embedded.LinkedHashMapCacheBuilder;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
@Component
@Conditional(LinkedHashMapAutoConfiguration.LinkedHashMapCondition.class)
public class LinkedHashMapAutoConfiguration extends EmbeddedCacheAutoInit {
  public LinkedHashMapAutoConfiguration() {
    super("linkedhashmap");
  }

  @Override
  protected CacheBuilder initCache(ConfigTree ct, String cacheAreaWithPrefix) {
    LinkedHashMapCacheBuilder builder = LinkedHashMapCacheBuilder.createLinkedHashMapCacheBuilder();
    parseGeneralConfig(builder, ct);
    return builder;
  }

  public static class LinkedHashMapCondition extends MixCacheCondition {
    public LinkedHashMapCondition() {
      super("linkedhashmap");
    }
  }
}
