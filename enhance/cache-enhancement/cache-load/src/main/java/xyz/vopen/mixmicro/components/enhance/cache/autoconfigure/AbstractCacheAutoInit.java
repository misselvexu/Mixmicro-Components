package xyz.vopen.mixmicro.components.enhance.cache.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;
import xyz.vopen.mixmicro.components.enhance.cache.AbstractCacheBuilder;
import xyz.vopen.mixmicro.components.enhance.cache.CacheBuilder;
import xyz.vopen.mixmicro.components.enhance.cache.anno.support.ConfigProvider;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public abstract class AbstractCacheAutoInit implements InitializingBean {

  private static Logger logger = LoggerFactory.getLogger(AbstractCacheAutoInit.class);

  @Autowired protected ConfigurableEnvironment environment;

  @Autowired protected AutoConfigureBeans autoConfigureBeans;

  @Autowired protected ConfigProvider configProvider;

  protected String[] typeNames;

  private boolean inited = false;

  public AbstractCacheAutoInit(String... cacheTypes) {
    Objects.requireNonNull(cacheTypes, "cacheTypes can't be null");
    Assert.isTrue(cacheTypes.length > 0, "cacheTypes length is 0");
    this.typeNames = cacheTypes;
  }

  @Override
  public void afterPropertiesSet() {
    if (!inited) {
      synchronized (this) {
        if (!inited) {
          process("mixcache.local.", autoConfigureBeans.getLocalCacheBuilders(), true);
          process("mixcache.remote.", autoConfigureBeans.getRemoteCacheBuilders(), false);
          inited = true;
        }
      }
    }
  }

  private void process(String prefix, Map cacheBuilders, boolean local) {
    ConfigTree resolver = new ConfigTree(environment, prefix);
    Map<String, Object> m = resolver.getProperties();
    Set<String> cacheAreaNames = resolver.directChildrenKeys();
    for (String cacheArea : cacheAreaNames) {
      final Object configType = m.get(cacheArea + ".type");
      boolean match = Arrays.stream(typeNames).anyMatch((tn) -> tn.equals(configType));
      if (!match) {
        continue;
      }
      ConfigTree ct = resolver.subTree(cacheArea + ".");
      logger.info("init cache area {} , type= {}", cacheArea, typeNames[0]);
      CacheBuilder c = initCache(ct, local ? "local." + cacheArea : "remote." + cacheArea);
      cacheBuilders.put(cacheArea, c);
    }
  }

  protected void parseGeneralConfig(CacheBuilder builder, ConfigTree ct) {
    AbstractCacheBuilder acb = (AbstractCacheBuilder) builder;
    acb.keyConvertor(configProvider.parseKeyConvertor(ct.getProperty("keyConvertor")));

    String expireAfterWriteInMillis = ct.getProperty("expireAfterWriteInMillis");
    if (expireAfterWriteInMillis == null) {
      // compatible with 2.1
      expireAfterWriteInMillis = ct.getProperty("defaultExpireInMillis");
    }
    if (expireAfterWriteInMillis != null) {
      acb.setExpireAfterWriteInMillis(Long.parseLong(expireAfterWriteInMillis));
    }

    String expireAfterAccessInMillis = ct.getProperty("expireAfterAccessInMillis");
    if (expireAfterAccessInMillis != null) {
      acb.setExpireAfterAccessInMillis(Long.parseLong(expireAfterAccessInMillis));
    }
  }

  protected abstract CacheBuilder initCache(ConfigTree ct, String cacheAreaWithPrefix);
}
