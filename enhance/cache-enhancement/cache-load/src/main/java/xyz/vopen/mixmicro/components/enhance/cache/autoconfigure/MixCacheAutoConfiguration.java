package xyz.vopen.mixmicro.components.enhance.cache.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.GlobalCacheConfig;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.SpringConfigProvider;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
@Configuration
@ConditionalOnClass(GlobalCacheConfig.class)
@ConditionalOnMissingBean(GlobalCacheConfig.class)
@EnableConfigurationProperties(MixCacheProperties.class)
@Import({
  RedisAutoConfiguration.class,
  CaffeineAutoConfiguration.class,
  MockRemoteCacheAutoConfiguration.class,
  LinkedHashMapAutoConfiguration.class,
  RedisLettuceAutoConfiguration.class,
  RedisSpringDataAutoConfiguration.class
})
public class MixCacheAutoConfiguration {

  public static final String GLOBAL_CACHE_CONFIG_NAME = "globalCacheConfig";

  private SpringConfigProvider _springConfigProvider = new SpringConfigProvider();

  private AutoConfigureBeans _autoConfigureBeans = new AutoConfigureBeans();

  private GlobalCacheConfig _globalCacheConfig;

  @Bean
  public static BeanDependencyManager beanDependencyManager() {
    return new BeanDependencyManager();
  }

  @Bean
  @ConditionalOnMissingBean
  public SpringConfigProvider springConfigProvider() {
    return _springConfigProvider;
  }

  @Bean
  public AutoConfigureBeans autoConfigureBeans() {
    return _autoConfigureBeans;
  }

  @Bean(name = GLOBAL_CACHE_CONFIG_NAME)
  public GlobalCacheConfig globalCacheConfig(
      SpringConfigProvider configProvider,
      AutoConfigureBeans autoConfigureBeans,
      MixCacheProperties props) {
    if (_globalCacheConfig != null) {
      return _globalCacheConfig;
    }
    _globalCacheConfig = new GlobalCacheConfig();
    _globalCacheConfig.setHiddenPackages(props.getHiddenPackages());
    _globalCacheConfig.setStatIntervalMinutes(props.getStatIntervalMinutes());
    _globalCacheConfig.setAreaInCacheName(props.isAreaInCacheName());
    _globalCacheConfig.setPenetrationProtect(props.isPenetrationProtect());
    _globalCacheConfig.setEnableMethodCache(props.isEnableMethodCache());
    _globalCacheConfig.setLocalCacheBuilders(autoConfigureBeans.getLocalCacheBuilders());
    _globalCacheConfig.setRemoteCacheBuilders(autoConfigureBeans.getRemoteCacheBuilders());
    return _globalCacheConfig;
  }
}
