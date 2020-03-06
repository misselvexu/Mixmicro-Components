package xyz.vopen.mixmicro.components.enhance.cache.autoconfigure;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class JedisPoolFactory implements FactoryBean<Pool<Jedis>> {
  private String key;
  private Class<?> poolClass;

  @Autowired private AutoConfigureBeans autoConfigureBeans;

  private boolean inited;
  private Pool<Jedis> jedisPool;

  public JedisPoolFactory(String key, Class<? extends Pool<Jedis>> poolClass) {
    this.key = key;
    this.poolClass = poolClass;
  }

  public String getKey() {
    return key;
  }

  @Override
  public Pool<Jedis> getObject() throws Exception {
    if (!inited) {
      jedisPool = (Pool<Jedis>) autoConfigureBeans.getCustomContainer().get("jedisPool." + key);
      inited = true;
    }
    return jedisPool;
  }

  @Override
  public Class<?> getObjectType() {
    return poolClass;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
