package xyz.vopen.mixmicro.components.enhance.cache.autoconfigure;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import xyz.vopen.mixmicro.components.enhance.cache.CacheBuilder;
import xyz.vopen.mixmicro.components.enhance.cache.annotation.CacheConsts;
import xyz.vopen.mixmicro.components.enhance.cache.external.MockRemoteCacheBuilder;

/**
 * Created on 2018/12/2.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
@Component
@Conditional(MockRemoteCacheAutoConfiguration.MockRemoteCacheCondition.class)
public class MockRemoteCacheAutoConfiguration extends ExternalCacheAutoInit {
  public MockRemoteCacheAutoConfiguration() {
    super("mock");
  }

  @Override
  protected CacheBuilder initCache(ConfigTree ct, String cacheAreaWithPrefix) {
    MockRemoteCacheBuilder builder = MockRemoteCacheBuilder.createMockRemoteCacheBuilder();
    parseGeneralConfig(builder, ct);
    return builder;
  }

  @Override
  protected void parseGeneralConfig(CacheBuilder builder, ConfigTree ct) {
    super.parseGeneralConfig(builder, ct);
    MockRemoteCacheBuilder b = (MockRemoteCacheBuilder) builder;
    b.limit(
        Integer.parseInt(ct.getProperty("limit", String.valueOf(CacheConsts.DEFAULT_LOCAL_LIMIT))));
  }

  public static class MockRemoteCacheCondition extends MixCacheCondition {
    public MockRemoteCacheCondition() {
      super("mock");
    }
  }
}
