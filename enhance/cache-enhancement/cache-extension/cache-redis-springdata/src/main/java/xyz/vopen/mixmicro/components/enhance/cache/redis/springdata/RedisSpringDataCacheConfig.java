package xyz.vopen.mixmicro.components.enhance.cache.redis.springdata;

import xyz.vopen.mixmicro.components.enhance.cache.external.ExternalCacheConfig;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * Created on 2019/4/4.
 *
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class RedisSpringDataCacheConfig<K, V> extends ExternalCacheConfig<K, V> {

    private RedisConnectionFactory connectionFactory;

    public RedisConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
}
