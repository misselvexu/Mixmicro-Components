package xyz.vopen.mixmicro.components.enhance.cache.redis.lettuce;

import xyz.vopen.mixmicro.components.enhance.cache.anno.CacheConsts;
import xyz.vopen.mixmicro.components.enhance.cache.external.ExternalCacheConfig;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.api.StatefulConnection;

/**
 * @author <a href="mailto:iskp.me@gmail.com">Elve.Xu</a>
 * @version ${project.version}
 */
public class RedisLettuceCacheConfig<K, V> extends ExternalCacheConfig<K, V> {

    private AbstractRedisClient redisClient;

    private StatefulConnection connection;

    private long asyncResultTimeoutInMillis = CacheConsts.ASYNC_RESULT_TIMEOUT.toMillis();

    public AbstractRedisClient getRedisClient() {
        return redisClient;
    }

    public void setRedisClient(AbstractRedisClient redisClient) {
        this.redisClient = redisClient;
    }

    public StatefulConnection getConnection() {
        return connection;
    }

    public void setConnection(StatefulConnection connection) {
        this.connection = connection;
    }

    public long getAsyncResultTimeoutInMillis() {
        return asyncResultTimeoutInMillis;
    }

    public void setAsyncResultTimeoutInMillis(long asyncResultTimeoutInMillis) {
        this.asyncResultTimeoutInMillis = asyncResultTimeoutInMillis;
    }
}
